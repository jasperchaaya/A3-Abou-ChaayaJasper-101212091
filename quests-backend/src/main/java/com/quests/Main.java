package com.quests;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Quests game!");
        Game game = new Game(4, true); // 4 players, shuffled decks
        System.out.println("Game initialized with 4 players.");
        game.playGame();
    }

}