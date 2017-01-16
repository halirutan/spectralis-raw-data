package de.halirutan.spectralis.gui.test;/**
 * Created by patrick on 14.01.17.
 * (c) Patrick Scheibe 2017
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
        primaryStage.setTitle("Test");
        primaryStage.setScene(new Scene(root, root.prefWidth(400), root.prefHeight(300)));
        primaryStage.show();
    }
}
