package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.TimeZone;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/sample.fxml"));
        primaryStage.setTitle("Task manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(460);
        primaryStage.setHeight(680);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
