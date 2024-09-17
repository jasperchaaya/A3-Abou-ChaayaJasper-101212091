package org.example;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Maintest {

    @Test
    @DisplayName("Check to see if decks are initialized")
    void R_1_test(){
        Main game = new Main();
        game.initializeDecks();

        //Test 1 - checking event deck and adventure deck
        int eventDeckSize = game.getEventDeckSize();
        int adventureDeckSize = game.getAdventureDeckSize();

            //checking total cards
        assertEquals(17,eventDeckSize);
        assertEquals(100,adventureDeckSize);

    }

}