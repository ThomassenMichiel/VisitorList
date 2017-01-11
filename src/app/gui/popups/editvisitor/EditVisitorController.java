package app.gui.popups.editvisitor;

import app.gui.MainController;
import db.DbHandler;
import elements.Visitor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Michiel Thomassen on 9/01/2017 at 22:55.
 * Project name: VisitorList
 * Package name: app.gui.popups.editvisitor
 * File name: EditVisitorController
 */
public class EditVisitorController implements Initializable {
    public ComboBox<String> muniCombo;
    public CheckBox copiesCBox;
    public CheckBox mailCBox;
    public CheckBox taxesCBox;
    public CheckBox otherCBox;
    public TextField nameTextField;
    public Button addMuniBtn;
    public Button saveBtn;
    public Button clearBtn;
    private Visitor oldVisitor;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty municipality = new SimpleStringProperty();
    private StringProperty copies = new SimpleStringProperty();
    private StringProperty mail = new SimpleStringProperty();
    private StringProperty taxes = new SimpleStringProperty();
    private StringProperty others = new SimpleStringProperty();
    private int rowId;
    private MainController mainController;

    public EditVisitorController(String name, String municipality, String copies, String mail, String taxes, String others, MainController mainController) {
        this.name.set(name);
        this.municipality.set(municipality);
        this.copies.set(copies);
        this.mail.set(mail);
        this.taxes.set(taxes);
        this.others.set(others);
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNameTextField(name.get());
        setMuniCombo(municipality.get());
        setCopiesCBox(copies.get());
        setMailCBox(mail.get());
        setTaxesCBox(taxes.get());
        setOtherCBox(others.get());
        mainController.populateMuniCBox();
        oldVisitor = createVisitor();
    }

    public void setCopiesCBox(String copiesCBox) {
        this.copiesCBox.setSelected(Boolean.parseBoolean(copiesCBox));
    }

    public void setMailCBox(String mailCBox) {
        this.mailCBox.setSelected(Boolean.parseBoolean(mailCBox));
    }

    public void setTaxesCBox(String taxesCBox) {
        this.taxesCBox.setSelected(Boolean.parseBoolean(taxesCBox));
    }

    public void setOtherCBox(String otherCBox) {
        this.otherCBox.setSelected(Boolean.parseBoolean(otherCBox));
    }

    public void setNameTextField(String nameTextField) {
        this.nameTextField.setText(nameTextField);
    }

    public void setMuniCombo(String muniCombo) {
        this.muniCombo.getSelectionModel().select(muniCombo);
    }

    public Visitor createVisitor() {
        Visitor visitor = new Visitor(null,
                nameTextField.getText(),
                muniCombo.getSelectionModel().getSelectedItem(),
                copiesCBox.isSelected(),
                mailCBox.isSelected(),
                taxesCBox.isSelected(),
                otherCBox.isSelected());
        return visitor;
    }

    public void updateVisitor(ActionEvent e) throws IOException {
        DbHandler db = new DbHandler("Voeren2000.db");
        db.updateVisitor(oldVisitor, createVisitor());

        mainController.populateList();

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }


}
