package com.brunodunbar.maquinaturing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MaquinaTuring extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/MaquinaTuring.fxml"));
        Scene scene = new Scene(root, 800, 500);

        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Maquina Turing");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
