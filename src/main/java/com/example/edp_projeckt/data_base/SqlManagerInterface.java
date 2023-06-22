package com.example.edp_projeckt.data_base;

import com.example.edp_projeckt.TrainingExercise;

import java.util.List;

interface SqlManagerInterface {
    void insertExercise(String name, String type, String muscle, String equipment, String difficulty,
                        String instructions, String sets, String reps, String date);

    List<TrainingExercise> retrieveExercisesByDate(String date);

    void deleteFromDB(String name, String muscle, String sets, String reps);
}
