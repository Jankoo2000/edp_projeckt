package com.example.edp_projeckt.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Exercise {
    private final String name;
    private final String type;
    private final String muscle;
    private final String equipment;
    private final String difficulty;
    private final String instructions;


    public Exercise(
            @JsonProperty("name")
            String name,
            @JsonProperty("type")
            String type,
            @JsonProperty("muscle")
            String muscle,
            @JsonProperty("equipment")
            String equipment,
            @JsonProperty("difficulty")
            String difficulty,
            @JsonProperty("instructions")
            String instructions
    ) {
        this.name = name;
        this.type = type;
        this.muscle = muscle;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.instructions = instructions;
    }

    // jesli konstrunktor nazywa sie inaczej niz getNazwaPola to nie dziala refleksja do TableView
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getMuscle() {
        return muscle;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getInstructions() {
        return instructions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Exercise) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.muscle, that.muscle) &&
                Objects.equals(this.equipment, that.equipment) &&
                Objects.equals(this.difficulty, that.difficulty) &&
                Objects.equals(this.instructions, that.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, muscle, equipment, difficulty, instructions);
    }

    @Override
    public String toString() {
        return "Exercise[" +
                "name=" + name + ", " +
                "type=" + type + ", " +
                "muscle=" + muscle + ", " +
                "equipment=" + equipment + ", " +
                "difficulty=" + difficulty + ", " +
                "instructions=" + instructions + ']';
    }


}
