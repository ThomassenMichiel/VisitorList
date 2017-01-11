package app;

import db.DbHandler;
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

        Parent root = FXMLLoader.load(getClass().getResource("/app/gui/VisitorList.fxml"));
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
