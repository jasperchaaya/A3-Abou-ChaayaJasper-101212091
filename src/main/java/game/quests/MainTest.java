package game.quests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    @DisplayName("R1-Check to see if both decks are initialized")
    void R_1_test(){
        Game game = new Game(4);
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
        // the game constructor will initialize decks and players
        Game game = new Game(4);

        //Get players
        Player[] players = game.getPlayers();

        //R2 - Check if players are created
        System.out.print("R2, Checking if players are created: ");
        assertNotNull(players);
        assertEquals(4, players.length);
        System.out.println("Passed.");

        //R2 - Check if each player is given 12 Adventure cards
        System.out.print("R2, Checking if each player has 12 adventure cards: ");
        for (Player player : players) {
            assertEquals(12, player.getHandSize());
        }
        System.out.println("Passed.");

        //R2 - Check if the cards are taken out from the Adventure deck
        int remainingAdventureDeckSize = game.getAdventureDeckSize();
        System.out.print("R2, Checking if cards were taken out from the Adventure deck: ");
        assertEquals(100 - (players.length * 12), remainingAdventureDeckSize);
        System.out.println("Expected 52, found " + remainingAdventureDeckSize);
    }
    @Test
    @DisplayName("R3 - Checking if shuffle deck function works.")
    void R_3_test() {

        // the game constructor will initialize decks and players
        Game game = new Game(4);

        //shuffle the decks

        Deck advDeck = game.getAdventureDeck();
        advDeck.shuffle();

        int n = 0;
        //checking first 8 cards if still F5
        List<Card> cards = advDeck.getCards();
        for (int i = 0; i < 8; i++) {
            if (cards.get(i).toString().equals("F 5")) {
                n++;
            }
        }
        //Check that not all 8 cards are "F 5"
        assertNotEquals(8, n, "R3 - Adventure Deck Shuffle Failed.");
        System.out.println("R3 - Adventure Deck Shuffle Passed.");


        //checking if event deck shuffle works
        Deck eveDeck = game.getEventDeck();
        eveDeck.shuffle();
        n = 0;
        //checking first 8 cards if still F5
        cards = eveDeck.getCards();
        for (int i = 0; i < 3; i++) {
            if (cards.get(i).toString().equals("Q 2")) {
                n++;
            }
        }
        //Check that not all 3 cards are "Q 2"
        assertNotEquals(3, n, "R3 - Event Deck Shuffle Failed.");
        System.out.println("R3 - Event Deck Shuffle Passed.");


        //R3 - Current player drawing from Event deck
        Card card = game.getEventDeck().draw();
        System.out.println("Drawn card: " + card.toString());

        game.handleEvent(card);
        Player currentPlayer = game.getCurrentPlayer();

        //R3 - Testing handling player's draw from event deck
        if(card.getType().equals("Plague")){
            System.out.print("R3, Checking if Plague card effect works: ");
            assertEquals(Math.max(0, currentPlayer.getShields() - 2), currentPlayer.getShields());
            System.out.println("Passed.");

        }else if(card.getType().equals("Queen's Favor")){
            System.out.print("R3, Checking if Queen's Favor card effect works: ");
            assertTrue(currentPlayer.getHandSize() <= 12);
            System.out.println("Passed.");

        }else if(card.getType().equals("Prosperity")){
            System.out.print("R3, Checking if Prosperity card effect works: ");
            for (Player player : game.getPlayers()) {
                assertTrue(currentPlayer.getHandSize() <= 12);
            }
            System.out.println("Passed.");

        }else if (card.getType().equals("Q")){
            System.out.println("R3, Checking if Quest card offers sponsorship: ");
            assertTrue(game.isSponsorshipOffered());
            System.out.println("Passed.");

        }else{
            System.out.println("R3- Test failed.");
        }
    }
}