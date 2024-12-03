package com.quests;

public class Card {
    private final String type;  // Type of card
    private final int value;    // Value of  card

    public Card(String type, int value) {
        this.type = type;
        this.value = value;
    }

    //Getters
    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return type + " " + value;
    }
}
