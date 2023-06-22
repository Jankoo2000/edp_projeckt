package com.example.edp_projeckt;

import com.example.edp_projeckt.utils.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        AppConfig appConfig = new AppConfig();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calendar-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Integer.parseInt(appConfig.getProperty("app.resolution_h")), Integer.parseInt(appConfig.getProperty("app.resolution_w")));
        stage.setTitle(appConfig.getProperty("app.title"));
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}

