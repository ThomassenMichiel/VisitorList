package app.gui;

import app.gui.popups.editvisitor.EditVisitorController;
import db.DbHandler;
import elements.Visitor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.border.StrokeBorder;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController {
    public CheckBox copiesCBox;
    public CheckBox mailCBox;
    public CheckBox taxesCBox;
    public CheckBox otherCBox;
    public TextField nameTextField;
    public ComboBox<String> muniCombo;
    public Button saveBtn;
    public Button clearBtn;
    public TableView<Visitor> visitorTableView;
    public TableColumn<Visitor, String> dateCol;
    public TableColumn<Visitor, String> nameCol;
    public TableColumn<Visitor, String> muniCol;
    public TableColumn<Visitor, String> copiesCol;
    public TableColumn<Visitor, String> mailCol;
    public TableColumn<Visitor, String> taxesCol;
    public TableColumn<Visitor, String> othersCol;
    public Button addMuniBtn;
    public BarChart reasonsBarChart;
    public BarChart municipalityBarChart;

    @FXML
    public void initialize() {
        populateList();
        populateMuniCBox();
    }

    public void addVisitor(ActionEvent e) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateFormatted = sdf.format(date);
        DbHandler db = new DbHandler("Voeren2000.db");
        if (!nameTextField.getText().isEmpty()) {
            db.addVisitor(dateFormatted, nameTextField.getText(), muniCombo.getSelectionModel().getSelectedItem(), copiesCBox.isSelected(), mailCBox.isSelected(), taxesCBox.isSelected(), otherCBox.isSelected());
            clearButtons();
        } else {
            // TODO: color field and border red to indicate a name's required.
        }
        visitorTableView.refresh();
        populateList();
    }

    public void populateList() {
        DbHandler db = new DbHandler("Voeren2000.db");
        ResultSet rs = db.selectVisitor();
        ObservableList<Visitor> data = FXCollections.observableArrayList();

        try {
            while (rs.next()) {
                Visitor visitor = new Visitor(rs.getString("Date"),
                        rs.getString("Name"),
                        rs.getString("Municipality"),
                        Boolean.parseBoolean(rs.getString("Copies")),
                        Boolean.parseBoolean(rs.getString("Mail")),
                        Boolean.parseBoolean(rs.getString("Taxes")),
                        Boolean.parseBoolean(rs.getString("Others")));
                data.add(visitor);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        dateCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Date"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Name"));
        muniCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Municipality"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Copies"));
        mailCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Mail"));
        taxesCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Taxes"));
        othersCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("Others"));
        visitorTableView.setItems(data);
        visitorTableView.refresh();
    }

    private void clearButtons() {
        copiesCBox.setSelected(false);
        mailCBox.setSelected(false);
        taxesCBox.setSelected(false);
        otherCBox.setSelected(false);
        nameTextField.setText("");
    }

    public void clearButtons(ActionEvent e) {
        this.clearButtons();
    }

    public void addMunicipalityPrompt(ActionEvent e) throws IOException {
        Stage stage;
        Parent root;

        stage = new Stage();
        root = FXMLLoader.load(getClass().getResource("popups/addmunicipalities/AddMunicipalities.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Add municipality");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addMuniBtn.getScene().getWindow());
        stage.show();
    }

    public void populateMuniCBox() {
        ObservableList<String> municipalities = FXCollections.observableArrayList();
        DbHandler db = new DbHandler("Voeren2000.db");
        ResultSet rs = db.selectMunicipalities();
        try {
            while (rs.next()) {
                municipalities.add(rs.getString("Municipality"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        municipalities.sorted();
        muniCombo.setItems(municipalities);
        muniCombo.getSelectionModel().select(0);
    }

    public void populateReasonsBarChart() {
        reasonsBarChart.getData().clear();
        DbHandler db = new DbHandler("Voeren2000.db");
        ResultSet rs = db.selectVisitor();
        String[] dbLabel = {"Copies", "Mail", "Taxes", "Others"};
        int[] reasonsCounter = {0,0,0,0};


        try {
            while (rs.next()) {
                for (int i = 0; i < dbLabel.length; i++) {
                    if (Boolean.parseBoolean(rs.getString(dbLabel[i]))) {
                        reasonsCounter[i]++;
                    }
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        XYChart.Series reasons = new XYChart.Series();
        for (int i = 0; i < dbLabel.length;i++){
            reasons.getData().add(new XYChart.Data<>(dbLabel[i],reasonsCounter[i]));
        }

        reasonsBarChart.getData().addAll(reasons);

    }

    public void populateMunicipalityBarChart() {
        municipalityBarChart.getData().clear();
        DbHandler db = new DbHandler("Voeren2000.db");
        List<String> muniList = new ArrayList<>(muniCombo.getItems());

        Map<String, Integer> counts = db.countItems(muniList, "Municipality" , "Visitorlist");

        XYChart.Series municipalities = new XYChart.Series();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() != 0) {
                municipalities.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        }

        municipalityBarChart.getData().addAll(municipalities);
    }

    public void populateAllBarCharts() {
        populateReasonsBarChart();
        populateMunicipalityBarChart();
    }

    public void editVisitorStart() throws IOException {
        Stage stage;
        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("popups/editvisitor/EditVisitor.fxml"));
        EditVisitorController controller = new EditVisitorController(visitorTableView.getSelectionModel().getSelectedItem().getName(),
                visitorTableView.getSelectionModel().getSelectedItem().getMunicipality(),
                visitorTableView.getSelectionModel().getSelectedItem().getCopies(),
                visitorTableView.getSelectionModel().getSelectedItem().getMail(),
                visitorTableView.getSelectionModel().getSelectedItem().getTaxes(),
                visitorTableView.getSelectionModel().getSelectedItem().getOthers(),
                this);
        loader.setController(controller);
        GridPane gridPane = loader.load();
        stage.setScene(new Scene(gridPane));
        stage.setTitle("Edit visitor");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
