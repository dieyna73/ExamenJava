
        package com.diti.examenapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML depuis src/main/resources/views/produit.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/produit.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        primaryStage.setTitle("Gestion des Produits");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
