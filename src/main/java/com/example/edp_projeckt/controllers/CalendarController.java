package com.example.edp_projeckt.controllers;

import com.example.edp_projeckt.Main;
import com.example.edp_projeckt.TrainingExercise;
import com.example.edp_projeckt.data_base.SqlManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
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
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("exercise-view.fxml"));
            try {
                stage.setScene(new Scene(fxmlLoader.load(), 1150, 400));
            } catch (IOException e) {
                throw new RuntimeException(e);
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

}
