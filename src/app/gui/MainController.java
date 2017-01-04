package app.gui;

import db.DbHandler;
import elements.Visitor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public void addVisitor(ActionEvent e) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateFormatted = sdf.format(date);
        DbHandler db = new DbHandler("Voeren2000.db");
        if (!nameTextField.getText().isEmpty()) {
            db.addVisitor(dateFormatted, nameTextField.getText(), muniCombo.getSelectionModel().getSelectedItem(), copiesCBox.isSelected(), mailCBox.isSelected(), taxesCBox.isSelected(), otherCBox.isSelected());
            clearButtons();
        } else {

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
        root = FXMLLoader.load(getClass().getResource("popups/addmunicipalities/addMunicipalities.fxml"));
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

    public void editVisitorStart() {
        int row = visitorTableView.getSelectionModel().getSelectedCells().get(0).getRow();

        editVisitorCommit(row);
    }

    public void editVisitorCommit(int row) {
        String sql = "update visitorlist set name = \"" + nameCol.getCellData(row) + "\","
                + " municipality = \"" + muniCol.getCellData(row) + "\","
                + " copies = \"" + copiesCol.getCellData(row) + "\","
                + " mail = \"" + mailCol.getCellData(row) + "\","
                + " taxes = \"" + taxesCol.getCellData(row) + "\","
                + " others = \"" + othersCol.getCellData(row) + "\"";
        System.out.println(sql);
    }
}
