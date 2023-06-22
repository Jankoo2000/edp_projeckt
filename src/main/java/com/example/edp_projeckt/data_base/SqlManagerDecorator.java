package com.example.edp_projeckt.data_base;

import com.example.edp_projeckt.utils.TrainingExercise;

import java.util.List;

class SqlManagerDecorator implements SqlManagerInterface {
    private SqlManagerInterface sqlManager;

    public SqlManagerDecorator(SqlManagerInterface sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Override
    public void insertExercise(String name, String type, String muscle, String equipment, String difficulty,
                               String instructions, String sets, String reps, String date) {
        // Dodatkowe operacje przed wstawieniem ćwiczenia do bazy danych
        System.out.println("Przed wstawieniem ćwiczenia do bazy danych");

        sqlManager.insertExercise(name, type, muscle, equipment, difficulty, instructions, sets, reps, date);

        // Dodatkowe operacje po wstawieniu ćwiczenia do bazy danych
        System.out.println("Po wstawieniu ćwiczenia do bazy danych");
    }

    @Override
    public List<TrainingExercise> retrieveExercisesByDate(String date) {
        // Dodatkowe operacje przed pobraniem ćwiczeń z bazy danych
        System.out.println("Przed pobraniem ćwiczeń z bazy danych");

        List<TrainingExercise> exercises = sqlManager.retrieveExercisesByDate(date);

        // Dodatkowe operacje po pobraniu ćwiczeń z bazy danych
        System.out.println("Po pobraniu ćwiczeń z bazy danych");

        return exercises;
    }

    @Override
    public void deleteFromDB(String name, String muscle, String sets, String reps) {
        // Dodatkowe operacje przed usunięciem ćwiczenia z bazy danych
        System.out.println("Przed usunięciem ćwiczenia z bazy danych");

        sqlManager.deleteFromDB(name, muscle, sets, reps);

        // Dodatkowe operacje po usunięciu ćwiczenia z bazy danych
        System.out.println("Po usunięciu ćwiczenia z bazy danych");
    }
}
