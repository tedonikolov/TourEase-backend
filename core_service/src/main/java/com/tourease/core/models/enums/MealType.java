package com.tourease.core.models.enums;

public enum MealType {
    BREAKFAST("Breakfast"),
    HALFBORD("Half board"),
    FULLBORD("Full board"),
    ALLINCLUSIVE("All inclusive");

    private final String name;

    MealType(String name) {
        this.name = name;
    }
}
