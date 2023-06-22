package com.example.edp_projeckt.data_base;

import com.example.edp_projeckt.TrainingExercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlManager {
    private final Connection connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
    private final String url = "jdbc:sqlite:D://EDP_projekt//edp_projeckt//mydatabase.db";

    public SqlManager() throws SQLException {
        try (connection) {

            String createExerciseTableQuery = "CREATE TABLE IF NOT EXISTS ExerciseTable (" +
                    "id INTEGER  PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(255)," +
                    "type VARCHAR(255)," +
                    "muscle VARCHAR(255)," +
                    "equipment VARCHAR(255)," +
                    "difficulty VARCHAR(255)," +
                    "instructions VARCHAR(255)," +
                    "sets VARCHAR(255)," +
                    "reps VARCHAR(255)," +
                    "date VARCHAR(255)" +
                    ")";

            executeQuery(connection, createExerciseTableQuery);


            System.out.println("Baza danych została utworzona.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void executeQuery(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }


    public void insertExercise(String name, String type, String muscle, String equipment, String difficulty,
                               String instructions, String sets, String reps, String date) {
        // Connection parameters
        // SQL query for inserting data
        String insertQuery = "INSERT INTO ExerciseTable(name, type, muscle, equipment, difficulty, instructions, sets, reps, date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            // Set the values for the prepared statement
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, muscle);
            pstmt.setString(4, equipment);
            pstmt.setString(5, difficulty);
            pstmt.setString(6, instructions);
            pstmt.setString(7, sets);
            pstmt.setString(8, reps);
            pstmt.setString(9, date);

            // Execute the insert statement
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }


    public List<TrainingExercise> retrieveExercisesByDate(String date) {
        String selectQuery = "SELECT * FROM ExerciseTable WHERE DATE = '" + date + "'";
        List<TrainingExercise> trainingExercises = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String muscle = rs.getString("muscle");
                String equipment = rs.getString("equipment");
                String difficulty = rs.getString("difficulty");
                String instructions = rs.getString("instructions");
                String sets = rs.getString("sets");
                String reps = rs.getString("reps");
                String exerciseDate = rs.getString("date");
                trainingExercises.add(new TrainingExercise(name, muscle,sets,reps));
//                // Perform any desired actions with the retrieved data
//                System.out.println("Exercise ID: " + id);
//                System.out.println("Name: " + name);
//                System.out.println("Type: " + type);
//                System.out.println("Muscle: " + muscle);
//                System.out.println("Equipment: " + equipment);
//                System.out.println("Difficulty: " + difficulty);
//                System.out.println("Instructions: " + instructions);
//                System.out.println("Sets: " + sets);
//                System.out.println("Reps: " + reps);
//                System.out.println("Date: " + exerciseDate);
//                System.out.println("----------------------------------");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }
        return trainingExercises;

    }
}


