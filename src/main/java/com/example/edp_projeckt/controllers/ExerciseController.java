package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.api.ApiFetch;
import javafx.scene.control.CheckBox;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.util.function.Function;

import static java.util.Collections.replaceAll;

public class ExerciseController {
    @FXML
    private Label label1, label2;
    @FXML
    private Button fetchDataButton, showText;
    @FXML
    private ChoiceBox<String> choiceBoxType, choiceBoxMuscle, choiceBoxDifficulty;
    private String[] types = {null ,"cardio", "olympic_weightlifting", "plyometrics", "powerlifting", "strength", "stretching", "strongman"};
    private String[] muslces = {null, "abdominals", "abductors", "biceps", "calves", "chest", "forearms", "glutes", "hamstrings", "lats", "lower_back", "middle_back", "neck", "quadriceps", "traps", "triceps"};
    private String[] difficulties = {null , "beginner", "intermediate", "expert"};
    private ApiFetch apiFetch = new ApiFetch();


    @FXML
    public void initialize() {

        choiceBoxType.getItems().addAll(types);
        choiceBoxMuscle.getItems().addAll(muslces);
        choiceBoxDifficulty.getItems().addAll(difficulties);

        choiceBoxType.setOnAction(e -> {
            String selectedOption = choiceBoxType.getValue();
            System.out.println("Selected option: " + selectedOption);
        });

        showText.setOnAction(e -> {
            label2.setText("Zupa ziemniakowa");
        });

        fetchDataButton.setOnAction(e -> {
            label1.setText("Waiting for data...");
            Task<String> fetchDataTask = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return fetchDataFromAPI();
                }
            };

            fetchDataTask.setOnSucceeded(event -> {
                String data = fetchDataTask.getValue();
                updateUIWithData(data);
            });

            Thread fetchDataThread = new Thread(fetchDataTask);
            fetchDataThread.start();
            System.out.println("Thread ID: " + fetchDataThread.threadId());
        });
    }



    private String fetchDataFromAPI() {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        var x = setCheckboxAction(choiceBoxType);
        var y = setCheckboxAction(choiceBoxMuscle);
        var z = setCheckboxAction(choiceBoxDifficulty);
        System.out.println("ZUPA " + x.getLeft() + " : " + x.getRight());
        System.out.println(choiceBoxType.getId() + " : " + apiFetch.fetchExercises(x.getLeft(), x.getRight()).toString());
        System.out.println(choiceBoxMuscle.getId() + " : " + apiFetch.fetchExercises(y.getLeft(), y.getRight()).toString());
        System.out.println(choiceBoxDifficulty.getId() + " : " + apiFetch.fetchExercises(z.getLeft(), z.getRight()).toString());
        return "Sample Data";
    }

    private void updateUIWithData(String data) {
        // Aktualizacja etykiety z pobranymi danymi
        label1.setText(data);
    }

    private Pair<String, String> setCheckboxAction(ChoiceBox<String> choiceBox) {
        String selectedOption =  choiceBox.getValue();
        System.out.println("S " + selectedOption);
        if (selectedOption != null) {
            System.out.println("Selected option: " + selectedOption);
            return Pair.of(choiceBox.getId().replaceAll("choiceBox", "").toLowerCase(),choiceBox.getValue());
        } else {
            System.out.println("No option selected");
            return null;
        }
    }

}
