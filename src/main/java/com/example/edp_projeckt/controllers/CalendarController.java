package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.Main;
import com.example.edp_projeckt.utils.TrainingExercise;
import com.example.edp_projeckt.data_base.SqlManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    @FXML
    private TableView<TrainingExercise> tableViewCalendar;
    @FXML
    private TableColumn<String, String> name, muscle, sets, reps;
    @FXML
    private DatePicker dataPickerCalendar;
    @FXML
    private Button addExercise;
    private String selectedDate;
    private SqlManager sqlManager = new SqlManager();

    public CalendarController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTableView();

        dataPickerCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedDate = String.valueOf(dataPickerCalendar.getValue());

            updateUIWithData(sqlManager.retrieveExercisesByDate(selectedDate));

        });

        addExercise.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("exercise-view.fxml"));
            try {
                stage.setScene(new Scene(fxmlLoader.load(), 1100, 370));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tableViewCalendar.setOnMouseClicked(mouseEvent -> {
            TrainingExercise selectedExercise = tableViewCalendar.getSelectionModel().getSelectedItem();
            if (selectedExercise != null) {
                String name = selectedExercise.getName();
                String muscle = selectedExercise.getMuscle();
                String sets = selectedExercise.getSets();
                String reps = selectedExercise.getReps();
                showConfirmationDialog(name, muscle, sets, reps);
            }
        });
    }

    public void initTableView() {

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        muscle.setCellValueFactory(new PropertyValueFactory<>("muscle"));
        muscle.setCellValueFactory(new PropertyValueFactory<>("muscle"));
        sets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        reps.setCellValueFactory(new PropertyValueFactory<>("reps"));
    }

    private void updateUIWithData(List<TrainingExercise> exercises) {

        ObservableList<TrainingExercise> exerciseList = FXCollections.observableArrayList(exercises);
        tableViewCalendar.setItems(exerciseList);
    }


    private void showConfirmationDialog(String name, String muscle, String sets, String reps) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText("Czy na pewno chcesz usunąć pierwszy rekord?");
        alert.setContentText("Ta operacja jest nieodwracalna.");

        // Dodawanie przycisków Tak i Nie
        ButtonType buttonTypeYes = new ButtonType("Tak");
        ButtonType buttonTypeNo = new ButtonType("Nie");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Obsługa wybranego przycisku
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            sqlManager.deleteFromDB(name, muscle, sets, reps);
            updateUIWithData(sqlManager.retrieveExercisesByDate(selectedDate));
        }
    }

}
