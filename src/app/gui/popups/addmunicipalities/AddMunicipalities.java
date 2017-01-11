package app.gui.popups.addmunicipalities;/**
 * Created by Michiel Thomassen on 15/12/2016 at 01:13.
 * Project name: VisitorList
 * Package name: app.gui
 * File name: AddMunicipalities
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddMunicipalities extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("app/gui/popups/addmunicipalities/AddMunicipalities.fxml"));
        primaryStage.setTitle("Add municipality");
        primaryStage.setScene(new Scene(root, 400, 150));
        primaryStage.show();
    }
}
