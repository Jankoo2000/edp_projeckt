package com.example.edp_projeckt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calendar-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 590, 340);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }

}

