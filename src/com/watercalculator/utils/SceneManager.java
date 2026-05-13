package com.watercalculator.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    public static void switchScene(Stage stage, String fxmlPath, String title) {
        try {
            URL resource = SceneManager.class.getResource(fxmlPath);
            
            
            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(root);

            URL cssResource = SceneManager.class.getResource("/resources/css/style.css");
            
                scene.getStylesheets().add(cssResource.toExternalForm());

            stage.setTitle(title);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println(" Erreur lors du switchScene : " + e.getMessage());
            e.printStackTrace();
        }
    }
}