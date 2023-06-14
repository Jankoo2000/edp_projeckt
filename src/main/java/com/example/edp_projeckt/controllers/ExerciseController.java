package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.api.ApiFetch;
import com.example.edp_projeckt.api.Exercise;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.replaceAll;

public class ExerciseController {
    @FXML
    private Label label1, label2;
    @FXML
    private Button fetchDataButton, showText;
    @FXML
    private TableView<Exercise> tableViewExercises;
    @FXML
    private TableColumn<Exercise, String> name, type, muscle, equipment, difficulty, instructions;
    @FXML
    private ChoiceBox<String> choiceBoxType, choiceBoxMuscle, choiceBoxDifficulty;
    private String[] types = {null, "cardio", "olympic_weightlifting", "plyometrics", "powerlifting", "strength", "stretching", "strongman"};
    private String[] muslces = {null, "abdominals", "abductors", "biceps", "calves", "chest", "forearms", "glutes", "hamstrings", "lats", "lower_back", "middle_back", "neck", "quadriceps", "traps", "triceps"};
    private String[] difficulties = {null, "beginner", "intermediate", "expert"};
    private ApiFetch apiFetch = new ApiFetch();


    @FXML
    public void initialize() {

        initTableView();
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
            Task<List<Exercise>> fetchDataTask = new Task<List<Exercise>>() {
                @Override
                protected List<Exercise> call() throws Exception {
                    return fetchDataFromAPI();
                }
            };

            fetchDataTask.setOnSucceeded(event -> {
                List<Exercise> exercises = fetchDataTask.getValue();
                updateUIWithData(exercises);
            });

            Thread fetchDataThread = new Thread(fetchDataTask);
            fetchDataThread.start();
            System.out.println("Thread ID: " + fetchDataThread.threadId());
        });
    }


    private List<Exercise> fetchDataFromAPI() {

        List<Exercise> exerciseList = new LinkedList<Exercise>();
        var resultChoiceBoxType = setCheckboxAction(choiceBoxType);
        var resultChoiceBoxMuscle = setCheckboxAction(choiceBoxMuscle);
        var resultChoiceBoxDifficulty = setCheckboxAction(choiceBoxDifficulty);

        if (resultChoiceBoxType.isEmpty() && resultChoiceBoxMuscle.isEmpty() && resultChoiceBoxDifficulty.isEmpty()) {
//            System.out.println(choiceBoxType.getId() + " : " + apiFetch.fetchExercises("", "").toString());
            exerciseList.addAll(apiFetch.fetchExercises("", ""));
            System.out.println("size " + exerciseList.size());
        } else {
            if (resultChoiceBoxType.isPresent()) {
//                System.out.println(choiceBoxType.getId() + " : " + apiFetch.fetchExercises(resultChoiceBoxType.get().getLeft(), resultChoiceBoxType.get().getRight()).toString());
                exerciseList.addAll(apiFetch.fetchExercises("", ""));
                System.out.println("size " + exerciseList.size());

            }
            if (resultChoiceBoxMuscle.isPresent()) {
//                System.out.println(choiceBoxMuscle.getId() + " : " + apiFetch.fetchExercises(resultChoiceBoxMuscle.get().getLeft(), resultChoiceBoxMuscle.get().getRight()).toString());
                exerciseList.addAll(apiFetch.fetchExercises(resultChoiceBoxMuscle.get().getLeft(), resultChoiceBoxMuscle.get().getRight()));
                System.out.println("size " + exerciseList.size());

            }
            if (resultChoiceBoxDifficulty.isPresent()) {
//                System.out.println(choiceBoxDifficulty.getId() + " : " + apiFetch.fetchExercises(resultChoiceBoxDifficulty.get().getLeft(), resultChoiceBoxDifficulty.get().getRight()).toString());
                exerciseList.addAll(apiFetch.fetchExercises(resultChoiceBoxDifficulty.get().getLeft(), resultChoiceBoxDifficulty.get().getRight()));
                System.out.println("size " + exerciseList.size());

            }
        }

        System.out.println("total size " + exerciseList.size());
        System.out.println(exerciseList);

        return exerciseList.stream().distinct().collect(Collectors.toList());
    }

    private void updateUIWithData(List<Exercise> exerciseList) {
        System.out.println("Received total size " + exerciseList.size());
        for (var x : exerciseList) {
            System.out.println((x.toString()));
            System.out.println("--------------");
        }
        ObservableList<Exercise> data = FXCollections.observableArrayList(exerciseList);
        tableViewExercises.setItems(data);
    }

    private Optional<Pair<String, String>> setCheckboxAction(ChoiceBox<String> choiceBox) {
        String selectedOption = choiceBox.getValue();
        System.out.println("S " + selectedOption);
        if (selectedOption != null) {
            System.out.println("Selected option: " + selectedOption);
            return Optional.of(Pair.of(choiceBox.getId().replaceAll("choiceBox", "").toLowerCase(), choiceBox.getValue()));
        } else {
            System.out.println("No option selected");
            return Optional.empty();
        }
    }

    public void initTableView() {
        TableColumn<Exercise, Number> indexColumn = new TableColumn<Exercise, Number>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(tableViewExercises.getItems().indexOf(column.getValue())));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        muscle.setCellValueFactory(new PropertyValueFactory<>("muscle"));
        equipment.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        instructions.setCellValueFactory(new PropertyValueFactory<>("instructions"));
    }

}
