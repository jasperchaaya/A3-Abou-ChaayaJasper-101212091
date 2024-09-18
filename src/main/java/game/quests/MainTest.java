package game.quests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    @DisplayName("Check to see if both decks are initialized")
    void R_1_test(){
        Game game = new Game();
        game.initializeDecks();

        //Test 1 - checking event deck and adventure deck
        int eventDeckSize = game.getEventDeckSize();
        int adventureDeckSize = game.getAdventureDeckSize();

        //R1 - Checking total number of cards for both decks

        System.out.print("R1, Checking if Event deck size is correct: ");
        assertEquals(17,eventDeckSize);
        System.out.println("Passed.");

        System.out.print("R1, Checking if Adventure deck size is correct: ");
        assertEquals(100,adventureDeckSize);
        System.out.println("Passed.");

    }

}