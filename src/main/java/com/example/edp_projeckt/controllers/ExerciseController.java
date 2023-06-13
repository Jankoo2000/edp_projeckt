package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.api.ApiFetch;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ExerciseController {
    @FXML
    Label  label1;
    @FXML
    Label label2;

    @FXML
    Button fetchDataButton;
    @FXML
    Button showText;

    private ApiFetch apiFetch = new ApiFetch();


    @FXML
    public void initialize()
    {
        System.out.println(apiFetch.fetchExercises().toString());

        showText.setOnAction(e -> {
            label2.setText("Zupa ziemniakowa");
        });
        fetchDataButton.setOnAction(e -> {
            label1.setText("Waiting for data...");
            // Tworzenie zadania Task dla pobierania danych z API
            Task<String> fetchDataTask = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    // Pobieranie danych z API - operacje w tle
                    return fetchDataFromAPI();
                }
            };

            // Obsługa zdarzenia po zakończeniu zadania
            fetchDataTask.setOnSucceeded(event -> {
                String data = fetchDataTask.getValue();
                updateUIWithData(data);
            });

            // Uruchomienie zadania w oddzielnym wątku
            Thread fetchDataThread = new Thread(fetchDataTask);
            fetchDataThread.start();
        });
    }

    private String fetchDataFromAPI() {
//        // Symulacja opóźnienia pobierania danych
//        System.out.println("ZUPA12");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("ZUPA");
//        System.out.println("ZUPA");
//        System.out.println("ZUPA");

        //
//        // Zwrócenie pobranych danych
        System.out.println(apiFetch.fetchExercises().toString());
        return "Sample Data";
    }

    // Metoda do aktualizacji interfejsu użytkownika z danymi
    private void updateUIWithData(String data) {
        // Aktualizacja etykiety z pobranymi danymi
        label1.setText(data);
    }
}
