package app.gui.popups.editvisitor;/**
 * Created by Michiel Thomassen on 9/01/2017 at 22:52.
 * Project name: VisitorList
 * Package name: app.gui.popups.editvisitor
 * File name: EditVisitor
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditVisitor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("app/gui/popups/editivisitor/EditVisitor.fxml"));
        primaryStage.setTitle("Edit Visitor");
        primaryStage.setScene(new Scene(root, 400, 150));
        primaryStage.show();
    }
}
