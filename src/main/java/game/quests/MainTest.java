package game.quests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    @DisplayName("R1-Check to see if both decks are initialized")
    void R_1_test(){
        Game game = new Game();
        game.initializeDecks();

        //Test 1 - checking event deck and adventure deck
        int eventDeckSize = game.getEventDeckSize();
        int adventureDeckSize = game.getAdventureDeckSize();

        //R1 - Checking total number of cards for both decks

        System.out.print("R1, Checking if Event deck size is correct: ");
        assertEquals(17,eventDeckSize);
        System.out.println("Expected 17, found " + eventDeckSize);

        System.out.print("R1, Checking if Adventure deck size is correct: ");
        assertEquals(100,adventureDeckSize);
        System.out.println("Expected 100, found " + adventureDeckSize);

    }
    @Test
    @DisplayName("R2-Check to see player(s) are created, given 12 Adventure cards each, " +
            "and check if cards are taken out of Adventure deck")
    void R_2_test() {
        Game game = new Game();
        game.initializeDecks();

        //Initialize 4 players
        game.initializePlayers(4);

        //Get players
        Player[] players = game.getPlayers();

        //R2 - Check if players are created
        System.out.print("R2, Checking if players are created: ");
        assertNotNull(players);
        assertEquals(4, players.length);
        System.out.println("Passed.");

        //Distribute 12 cards to each player
        for (Player player : players) {
            game.distributeCards(player, "Adventure" ,12);
        }

        //R2 - Check if each player is given 12 Adventure cards
        System.out.print("R2, Checking if each player has 12 adventure cards: ");
        for (Player player : players) {
            assertEquals(12, player.getHandSize());
        }
        System.out.println("Passed.");

        //R2 - Check if the cards are taken out from the Adventure deck
        int remainingAdventureDeckSize = game.getAdventureDeckSize();
        System.out.print("R2, Checking if cards were taken out from the Adventure deck: ");
        assertEquals(100 - (player.size() * 12), remainingAdventureDeckSize);
        System.out.println("Expected 52, found " + remainingAdventureDeckSize);
    }

}