package com.tourease.hotel.models.enums;

public enum Stars {
    ONE,TWO,THREE,FOUR,FIVE,NONE;

    public static Stars setValue(int value){
        return switch (value) {
            case 1 -> Stars.ONE;
            case 2 -> Stars.TWO;
            case 3 -> Stars.THREE;
            case 4 -> Stars.FOUR;
            case 5 -> Stars.FIVE;
            default -> Stars.NONE;
        };
    }
}
