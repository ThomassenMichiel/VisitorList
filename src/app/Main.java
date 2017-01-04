package app;

import app.gui.MainController;
import db.DbHandler;
import elements.Visitor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        DbHandler db = new DbHandler("Voeren2000.db");
        db.createTable();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/app/gui/visitorList.fxml"));
        Parent root = loader.load();
        MainController MainController = loader.getController();
        MainController.populateList();
        MainController.populateMuniCBox();

        primaryStage.setTitle("Voeren 2000");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
