//package com.example.edp_projeckt.controllers;
//
//import com.example.edp_projeckt.Main;
//import com.example.edp_projeckt.api.ApiFetch;
//import com.example.edp_projeckt.api.Exercise;
//import com.example.edp_projeckt.data_base.SqlManager;
//import com.google.common.eventbus.EventBus;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.stage.Stage;
//import org.apache.commons.lang3.tuple.Pair;
//import javafx.concurrent.Task;
//import javafx.fxml.FXML;
//
//import java.io.IOException;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//
//public class ExerciseController {
//    @FXML
//    private Button fetchDataButton, calendarButton;
//    @FXML
//    private TableView<Exercise> tableViewExercises;
//    @FXML
//    private TableColumn<Exercise, String> name, type, muscle, equipment, difficulty, instructions;
//    @FXML
//    private ChoiceBox<String> choiceBoxType, choiceBoxMuscle, choiceBoxDifficulty;
//    private String[] types = {null, "cardio", "olympic_weightlifting", "plyometrics", "powerlifting", "strength", "stretching", "strongman"};
//    private String[] muslces = {null, "abdominals", "abductors", "biceps", "calves", "chest", "forearms", "glutes", "hamstrings", "lats", "lower_back", "middle_back", "neck", "quadriceps", "traps", "triceps"};
//    private String[] difficulties = {null, "beginner", "intermediate", "expert"};
//    private ApiFetch apiFetch = new ApiFetch();
//    SqlManager sqlManager = new SqlManager();
//    Date selectedDate;
//    String sets, reps;
//    private EventBus eventBus;
//
//    public ExerciseController() throws SQLException {
//    }
//
//
//    @FXML
//    public void initialize() {
//        initTableView();
//        choiceBoxType.getItems().addAll(types);
//        choiceBoxMuscle.getItems().addAll(muslces);
//        choiceBoxDifficulty.getItems().addAll(difficulties);
//
//        tableViewExercises.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2 && !tableViewExercises.getSelectionModel().isEmpty()) {
//                Exercise selectedPerson = tableViewExercises.getSelectionModel().getSelectedItem();
//                showDetailsWindow(selectedPerson);
//            }
//        });
//
//        choiceBoxType.setOnAction(e -> {
//            String selectedOption = choiceBoxType.getValue();
//        });
//
//
//        fetchDataButton.setOnAction(e -> {
//
//            Task<List<Exercise>> fetchDataTask = new Task<List<Exercise>>() {
//                @Override
//                protected List<Exercise> call() throws Exception {
//                    return fetchDataFromAPI();
//                }
//            };
//
//            fetchDataTask.setOnSucceeded(event -> {
//                List<Exercise> exercises = fetchDataTask.getValue();
//                updateUIWithData(exercises);
//            });
//
//            Thread fetchDataThread = new Thread(fetchDataTask);
//            fetchDataThread.start();
//        });
//
//        calendarButton.setOnAction(event -> {
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calendar-view.fxml"));
//            try {
//                stage.setScene(new Scene(fxmlLoader.load(), 590, 340));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    private List<Exercise> fetchDataFromAPI() {
//        List<Exercise> exerciseList = new LinkedList<>();
//        var resultChoiceBoxType = setCheckboxAction(choiceBoxType);
//        var resultChoiceBoxMuscle = setCheckboxAction(choiceBoxMuscle);
//        var resultChoiceBoxDifficulty = setCheckboxAction(choiceBoxDifficulty);
//
//        if (resultChoiceBoxType.isEmpty() && resultChoiceBoxMuscle.isEmpty() && resultChoiceBoxDifficulty.isEmpty()) {
//            exerciseList.addAll(apiFetch.fetchExercises("", ""));
//        } else {
//            exerciseList.addAll(apiFetch.fetchExercises(
//                    resultChoiceBoxType.map(Pair::getLeft).orElse(""),
//                    resultChoiceBoxType.map(Pair::getRight).orElse("")
//            ));
//            exerciseList.addAll(apiFetch.fetchExercises(
//                    resultChoiceBoxMuscle.map(Pair::getLeft).orElse(""),
//                    resultChoiceBoxMuscle.map(Pair::getRight).orElse("")
//            ));
//            exerciseList.addAll(apiFetch.fetchExercises(
//                    resultChoiceBoxDifficulty.map(Pair::getLeft).orElse(""),
//                    resultChoiceBoxDifficulty.map(Pair::getRight).orElse("")
//            ));
//        }
//
//        exerciseList = exerciseList.stream()
//                .filter(e -> resultChoiceBoxType.isEmpty() || e.getType().equals(resultChoiceBoxType.get().getRight()))
//                .filter(e -> resultChoiceBoxMuscle.isEmpty() || e.getMuscle().equals(resultChoiceBoxMuscle.get().getRight()))
//                .filter(e -> resultChoiceBoxDifficulty.isEmpty() || e.getDifficulty().equals(resultChoiceBoxDifficulty.get().getRight()))
//                .distinct()
//                .collect(Collectors.toList());
//
//        return exerciseList;
//    }
//
//
//    private void updateUIWithData(List<Exercise> exerciseList) {
//
//        ObservableList<Exercise> data = FXCollections.observableArrayList(exerciseList);
//        tableViewExercises.setItems(data);
//
//    }
//
//    private Optional<Pair<String, String>> setCheckboxAction(ChoiceBox<String> choiceBox) {
//        String selectedOption = choiceBox.getValue();
//        if (selectedOption != null) {
//            return Optional.of(Pair.of(choiceBox.getId().replaceAll("choiceBox", "").toLowerCase(), choiceBox.getValue()));
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    public void initTableView() {
//
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        type.setCellValueFactory(new PropertyValueFactory<>("type"));
//        muscle.setCellValueFactory(new PropertyValueFactory<>("muscle"));
//        equipment.setCellValueFactory(new PropertyValueFactory<>("equipment"));
//        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
//        instructions.setCellValueFactory(new PropertyValueFactory<>("instructions"));
//        instructions.setResizable(true);
//
//    }
//
//    private void showDetailsWindow(Exercise exercise) {
//        Stage detailsStage = new Stage();
//        detailsStage.setTitle("Person Details");
//
//        ListView<String> detailsListView = new ListView<>();
//        detailsListView.setPrefHeight(200);
//
//        List<String> exerciseDetails = Arrays.asList(
//                "Name: " + exercise.getName(),
//                "Type: " + exercise.getType(),
//                "Muscle: " + exercise.getMuscle(),
//                "Equipment: " + exercise.getEquipment(),
//                "Difficulty: " + exercise.getDifficulty(),
//                "Instructions: " + exercise.getInstructions()
//        );
//
//        detailsListView.getItems().addAll(exerciseDetails);
//        detailsListView.setCellFactory(param -> new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (!empty) {
//                    TextFlow textFlow = new TextFlow();
//                    Text text = new Text(item);
//                    text.wrappingWidthProperty().bind(detailsListView.widthProperty().subtract(20));
//                    textFlow.getChildren().add(text);
//                    setGraphic(textFlow);
//                } else {
//                    setGraphic(null);
//                }
//            }
//        });
//
//        Button addToPlanButton = new Button("Add to training plan");
//
//        // Dodaj podpisy nad polami tekstowymi i kalendarzem
//        Label value1Label = new Label("Sets");
//        Label value2Label = new Label("Reps");
//        Label dateLabel = new Label("Date");
//
//        TextField value1TextField = new TextField();
//        TextField value2TextField = new TextField();
//        DatePicker datePicker = new DatePicker();
//
//        // Wysrodkuj przycisk
//        VBox buttonContainer = new VBox(addToPlanButton);
//        buttonContainer.setAlignment(Pos.CENTER);
//
//        GridPane inputGrid = new GridPane();
//        inputGrid.setVgap(11);
//        inputGrid.setHgap(11);
//        inputGrid.addRow(0, value1Label, value1TextField);
//        inputGrid.addRow(1, value2Label, value2TextField);
//        inputGrid.addRow(2, dateLabel, datePicker);
//
//        VBox vbox = new VBox(detailsListView, inputGrid, buttonContainer);
//        vbox.setSpacing(11);
//        vbox.setPadding(new Insets(20));
//
//        Scene scene = new Scene(vbox);
//        scene.getStylesheets().add(getClass().getResource("/com.example.css/style.css").toExternalForm());
//        detailsStage.setScene(scene);
//        detailsStage.show();
//
//
//        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            selectedDate = Date.valueOf(datePicker.getValue());
//            System.out.println("Selected date: " + selectedDate);
//        });
//
//        value1TextField.setOnAction(event -> {
//            sets = value1TextField.getText();
//            System.out.println("SETS :" + sets);
//        });
//
//        value2TextField.setOnAction(event -> {
//            reps = value2TextField.getText();
//            System.out.println("REPS :" + reps);
//        });
//
//        addToPlanButton.setOnAction(event -> {
//            try {
//                addToTrainingPlan(exercise, sets, reps, selectedDate.toString());
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//    }
//
//
//    private void addToTrainingPlan(Exercise exercise, String sets, String reps, String date) throws SQLException {
//        sqlManager.insertExercise(
//                exercise.getName(),
//                exercise.getType(),
//                exercise.getMuscle(),
//                exercise.getEquipment(),
//                exercise.getDifficulty(),
//                exercise.getInstructions(),
//                sets,
//                reps,
//                date);
//    }
//}









package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.Main;
import com.example.edp_projeckt.api.ApiFetch;
import com.example.edp_projeckt.api.Exercise;
import com.example.edp_projeckt.data_base.SqlManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ExerciseController {
    @FXML
    private Button fetchDataButton, calendarButton;
    @FXML
    private TableView<Exercise> tableViewExercises;
    @FXML
    private TableColumn<Exercise, String> name, type, muscle, equipment, difficulty, instructions;
    @FXML
    private ChoiceBox<String> choiceBoxType, choiceBoxMuscle, choiceBoxDifficulty;
    private String[] types = {null, "cardio", "olympic_weightlifting", "plyometrics", "powerlifting", "strength", "stretching", "strongman"};
    private String[] muscles = {null, "abdominals", "abductors", "biceps", "calves", "chest", "forearms", "glutes", "hamstrings", "lats", "lower_back", "middle_back", "neck", "quadriceps", "traps", "triceps"};
    private String[] difficulties = {null, "beginner", "intermediate", "expert"};
    private ApiFetch apiFetch = new ApiFetch();
    private SqlManager sqlManager = new SqlManager();
    private Date selectedDate;
    private String sets, reps;
    private EventBus eventBus;

    public ExerciseController() throws SQLException {
    }

    @FXML
    public void initialize() {
        initTableView();
        choiceBoxType.getItems().addAll(types);
        choiceBoxMuscle.getItems().addAll(muscles);
        choiceBoxDifficulty.getItems().addAll(difficulties);

        eventBus = new EventBus();
        eventBus.register(this);

        tableViewExercises.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableViewExercises.getSelectionModel().isEmpty()) {
                Exercise selectedPerson = tableViewExercises.getSelectionModel().getSelectedItem();
                showDetailsWindow(selectedPerson);
            }
        });

        choiceBoxType.setOnAction(e -> {
            String selectedOption = choiceBoxType.getValue();
            eventBus.post(selectedOption);
        });

        fetchDataButton.setOnAction(e -> {
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
        });

        calendarButton.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calendar-view.fxml"));
            try {
                stage.setScene(new Scene(fxmlLoader.load(), 590, 340));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    private void updateUIWithData(List<Exercise> exerciseList) {
        ObservableList<Exercise> data = FXCollections.observableArrayList(exerciseList);
        tableViewExercises.setItems(data);
    }

    private Optional<Pair<String, String>> setCheckboxAction(ChoiceBox<String> choiceBox) {
        String selectedOption = choiceBox.getValue();
        if (selectedOption != null) {
            return Optional.of(Pair.of(choiceBox.getId().replaceAll("choiceBox", "").toLowerCase(), choiceBox.getValue()));
        } else {
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

        ListView<String> detailsListView = new ListView<>();
        detailsListView.setPrefHeight(200);

        List<String> exerciseDetails = Arrays.asList(
                "Name: " + exercise.getName(),
                "Type: " + exercise.getType(),
                "Muscle: " + exercise.getMuscle(),
                "Equipment: " + exercise.getEquipment(),
                "Difficulty: " + exercise.getDifficulty(),
                "Instructions: " + exercise.getInstructions()
        );

        detailsListView.getItems().addAll(exerciseDetails);
        detailsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    TextFlow textFlow = new TextFlow();
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(detailsListView.widthProperty().subtract(15));
                    textFlow.getChildren().add(text);
                    setGraphic(textFlow);
                }
            }
        });

        Button addToPlanButton = new Button("Add to training plan");

        // Dodaj podpisy nad polami tekstowymi i kalendarzem
        Label value1Label = new Label("Sets");
        Label value2Label = new Label("Reps");
        Label dateLabel = new Label("Date");

        TextField value1TextField = new TextField();
        TextField value2TextField = new TextField();
        DatePicker datePicker = new DatePicker();

        // Wysrodkuj przycisk
        VBox buttonContainer = new VBox(addToPlanButton);
        buttonContainer.setAlignment(Pos.CENTER);

        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(11);
        inputGrid.setHgap(11);
        inputGrid.addRow(0, value1Label, value1TextField);
        inputGrid.addRow(1, value2Label, value2TextField);
        inputGrid.addRow(2, dateLabel, datePicker);

        VBox vbox = new VBox(detailsListView, inputGrid, buttonContainer);
        vbox.setSpacing(11);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(getClass().getResource("/com.example.css/style.css").toExternalForm());
        detailsStage.setScene(scene);
        detailsStage.show();

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedDate = Date.valueOf(datePicker.getValue());
            System.out.println("Selected date: " + selectedDate);
        });

        value1TextField.setOnAction(event -> {
            sets = value1TextField.getText();
            System.out.println("SETS :" + sets);
        });

        value2TextField.setOnAction(event -> {
            reps = value2TextField.getText();
            System.out.println("REPS :" + reps);
        });

        addToPlanButton.setOnAction(event -> {
            try {
                addToTrainingPlan(exercise, sets, reps, selectedDate.toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void addToTrainingPlan(Exercise exercise, String sets, String reps, String date) throws SQLException {
        sqlManager.insertExercise(
                exercise.getName(),
                exercise.getType(),
                exercise.getMuscle(),
                exercise.getEquipment(),
                exercise.getDifficulty(),
                exercise.getInstructions(),
                sets,
                reps,
                date);
    }

    @Subscribe
    public void handleChoiceBoxEvent(String selectedOption) {
        tableViewExercises.getItems().clear();

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
    }
}
