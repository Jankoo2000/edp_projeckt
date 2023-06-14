package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.api.ApiFetch;
import com.example.edp_projeckt.api.Exercise;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public ExerciseController() {
    }


    @FXML
    public void initialize() {

        initTableView();
        choiceBoxType.getItems().addAll(types);
        choiceBoxMuscle.getItems().addAll(muslces);
        choiceBoxDifficulty.getItems().addAll(difficulties);

        tableViewExercises.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableViewExercises.getSelectionModel().isEmpty()) {
                Exercise selectedPerson = tableViewExercises.getSelectionModel().getSelectedItem();
                showDetailsWindow(selectedPerson);
            }
        });


        choiceBoxType.setOnAction(e -> {
            String selectedOption = choiceBoxType.getValue();
            System.out.println("Selected option: " + selectedOption);
        });

        showText.setOnAction(e -> {

            label2.setText(String.valueOf((Math.random())));
        });

        fetchDataButton.setOnAction(e -> {
            label1.setText("Waiting for data...");
            Task<List<Exercise>> fetchDataTask = new Task<List<Exercise>>() {
                @Override
                protected List<Exercise> call() throws Exception {
                    var x = fetchDataFromAPI();
                    System.out.println("SIZE after filter " + x.size());
                    return x;
//                    return fetchDataFromAPI();
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
        List<Exercise> exerciseList = new LinkedList<>();
        var resultChoiceBoxType = setCheckboxAction(choiceBoxType);
        var resultChoiceBoxMuscle = setCheckboxAction(choiceBoxMuscle);
        var resultChoiceBoxDifficulty = setCheckboxAction(choiceBoxDifficulty);

        if (resultChoiceBoxType.isEmpty() && resultChoiceBoxMuscle.isEmpty() && resultChoiceBoxDifficulty.isEmpty()) {
            exerciseList.addAll(apiFetch.fetchExercises("", ""));
        } else {
            exerciseList.addAll(apiFetch.fetchExercises(
                    resultChoiceBoxType.map(Pair::getLeft).orElse(""),
                    resultChoiceBoxType.map(Pair::getRight).orElse("")
            ));
            exerciseList.addAll(apiFetch.fetchExercises(
                    resultChoiceBoxMuscle.map(Pair::getLeft).orElse(""),
                    resultChoiceBoxMuscle.map(Pair::getRight).orElse("")
            ));
            exerciseList.addAll(apiFetch.fetchExercises(
                    resultChoiceBoxDifficulty.map(Pair::getLeft).orElse(""),
                    resultChoiceBoxDifficulty.map(Pair::getRight).orElse("")
            ));
        }

        exerciseList = exerciseList.stream()
                .filter(e -> resultChoiceBoxType.isEmpty() || e.getType().equals(resultChoiceBoxType.get().getRight()))
                .filter(e -> resultChoiceBoxMuscle.isEmpty() || e.getMuscle().equals(resultChoiceBoxMuscle.get().getRight()))
                .filter(e -> resultChoiceBoxDifficulty.isEmpty() || e.getDifficulty().equals(resultChoiceBoxDifficulty.get().getRight()))
                .distinct()
                .collect(Collectors.toList());

        return exerciseList;
    }


//    private List<Exercise> fetchDataFromAPI() {
//
//        List<Exercise> exerciseList = new LinkedList<Exercise>();
//        var resultChoiceBoxType = setCheckboxAction(choiceBoxType);
//        var resultChoiceBoxMuscle = setCheckboxAction(choiceBoxMuscle);
//        var resultChoiceBoxDifficulty = setCheckboxAction(choiceBoxDifficulty);
//
//        if (resultChoiceBoxType.isEmpty() && resultChoiceBoxMuscle.isEmpty() && resultChoiceBoxDifficulty.isEmpty()) {
////            System.out.println(choiceBoxType.getId() + " : " + apiFetch.fetchExercises("", "").toString());
//            exerciseList.addAll(apiFetch.fetchExercises("", ""));
//            System.out.println("size aa " + exerciseList.size());
//        } else {
//            if (resultChoiceBoxType.isPresent()) {
////                System.out.println(choiceBoxType.getId() + " : " + apiFetch.fetchExercises(resultChoiceBoxType.get().getLeft(), resultChoiceBoxType.get().getRight()).toString());
//                exerciseList.addAll(apiFetch.fetchExercises(resultChoiceBoxType.get().getLeft(), resultChoiceBoxType.get().getRight()));
//                System.out.println("size " + exerciseList.size());
//
//            }
//            if (resultChoiceBoxMuscle.isPresent()) {
////                System.out.println(choiceBoxMuscle.getId() + " : " + apiFetch.fetchExercises(resultChoiceBoxMuscle.get().getLeft(), resultChoiceBoxMuscle.get().getRight()).toString());
//                exerciseList.addAll(apiFetch.fetchExercises(resultChoiceBoxMuscle.get().getLeft(), resultChoiceBoxMuscle.get().getRight()));
//                System.out.println("size " + exerciseList.size());
//
//            }
//            if (resultChoiceBoxDifficulty.isPresent()) {
////                System.out.println(choiceBoxDifficulty.getId() + " : " + apiFetch.fetchExercises(resultChoiceBoxDifficulty.get().getLeft(), resultChoiceBoxDifficulty.get().getRight()).toString());
//                exerciseList.addAll(apiFetch.fetchExercises(resultChoiceBoxDifficulty.get().getLeft(), resultChoiceBoxDifficulty.get().getRight()));
//                System.out.println(exerciseList);
//                System.out.println("size " + exerciseList.size());
//
//            }
//        }
//
//        if (resultChoiceBoxType.isPresent()) {
//            exerciseList = exerciseList
//                    .stream()
//                    .filter(e -> e.getType().equals(resultChoiceBoxType.get().getRight()))
//                    .collect(Collectors.toList());
//        }
//        if (resultChoiceBoxMuscle.isPresent()) {
//            exerciseList = exerciseList
//                    .stream()
//                    .filter(e -> e.getMuscle().equals(resultChoiceBoxMuscle.get().getRight()))
//                    .collect(Collectors.toList());
//        }
//        if (resultChoiceBoxDifficulty.isPresent()) {
//            exerciseList = exerciseList
//                    .stream()
//                    .filter(e -> e.getDifficulty().equals(resultChoiceBoxDifficulty.get().getRight()))
//                    .collect(Collectors.toList());
//        }
//
//
//        // jak filtruje po null czy "" to lista nie jest napisywana wiec zostaje ta stara
//        return exerciseList
//                .stream()
//                .distinct()
//                .collect(Collectors.toList());
//    }

    private void updateUIWithData(List<Exercise> exerciseList) {
        System.out.println("Received total size " + exerciseList.size());
        for (var x : exerciseList) {
            System.out.println((x.toString()));
            System.out.println("--------------");
        }
        ObservableList<Exercise> data = FXCollections.observableArrayList(exerciseList);
        tableViewExercises.setItems(data);
        label1.setText("Loaded");
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

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        muscle.setCellValueFactory(new PropertyValueFactory<>("muscle"));
        equipment.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        instructions.setCellValueFactory(new PropertyValueFactory<>("instructions"));
        instructions.setResizable(true);

    }

    private void showDetailsWindow(Exercise exercise) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Person Details");

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label name = new Label(exercise.getName());
        Label type = new Label(exercise.getType());
        Label muscle = new Label(exercise.getMuscle());
        Label equipment = new Label(exercise.getEquipment());
        Label difficulty = new Label(exercise.getDifficulty());
        Label instructions = new Label(exercise.getInstructions());
        instructions.setWrapText(true);

        vbox.getChildren().addAll(
                name, type, muscle, equipment, difficulty, instructions
        );

        Scene scene = new Scene(vbox, 200, 150);
        detailsStage.setScene(scene);
        detailsStage.show();
    }
}
