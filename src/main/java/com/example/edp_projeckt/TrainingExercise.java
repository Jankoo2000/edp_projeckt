package com.example.edp_projeckt;

public class TrainingExercise {
    private String name;
    private String muscle;
    private String sets;
    private String reps;


    public TrainingExercise(String name, String muscle, String sets, String reps) {
        this.name = name;
        this.muscle = muscle;
        this.sets = sets;
        this.reps = reps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}
