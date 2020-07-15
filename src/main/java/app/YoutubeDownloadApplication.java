package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class YoutubeDownloadApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MenuView.fxml"));
        primaryStage.setTitle("Youtube dl-manager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 395, 537));
        primaryStage.show();
    }
}
