package app.gui.popups.addmunicipalities;

import db.DbHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Michiel Thomassen on 15/12/2016 at 15:22.
 * Project name: VisitorList
 * Package name: app.gui
 * File name: AddMunicipalitiesController
 */
public class AddMunicipalitiesController {
    public TextField newMuniTextField;
    public Button addBtn;

    public void addMunicipality(ActionEvent e) {
        DbHandler db = new DbHandler("Voeren2000.db");
        db.addMunicipality(newMuniTextField.getText());
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();
    }
}
