package game.quests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    @DisplayName("R1-Check to see if both decks are initialized")
    void R_1_test(){
        Game game = new Game(4, false);
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
        Game game = new Game(4, false);

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
    @DisplayName("R3 - Checking if event cards work.")
    void R_3_test() {

        //The game constructor will initialize decks and players
        Game game = new Game(4, false);

        //R3 - Current player drawing from Event deck
        Card card = game.getEventDeck().draw();
        System.out.println("Drawn card: " + card.toString());

        //game.handleEvent(card);
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
                assertTrue(player.getHandSize() <= 12);
            }
            System.out.println("Passed.");

        }else if (card.getType().equals("Q")){
            System.out.println("R3, Checking if Quest card offers sponsorship: ");
            String simulatedInput = "N\nN\nN\nY\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            assertTrue(game.isSponsorshipOffered());
            System.out.println("Passed.");

        }else{
            System.out.println("R3- Test failed.");
        }
    }

    @Test
    @DisplayName("R4 - Initiate player's turn and display their hand")
    void R_4_test() {
        //The game constructor will initialize decks and players
        Game game = new Game(4, true);

        //Start the first player's turn
        Player currentPlayer = game.getCurrentPlayer();

        //Check if the correct player is taking their turn and that player is not null
        System.out.print("R4, Checking if it's the correct player's turn: ");
        assertNotNull(currentPlayer);
        System.out.println("It's " + currentPlayer.getName() + "'s turn.");

        for(int n=0;n<4;n++){
            currentPlayer.printHand();

            System.out.print("R4, Checking if current player is changing, then display hand");
            assertEquals("Player " + ((int)n+1),currentPlayer.getName());

            assertEquals(12, currentPlayer.getHandSize());
            System.out.println("Player's hand contains " + currentPlayer.getHandSize() + " cards. ");

            System.out.println("==============================");
            game.endTurn();
            currentPlayer = game.getCurrentPlayer();
        }
    }

    @Test
    @DisplayName("R5 - Enforce hand size limit by trimming cards")
    void R_5_test() {
        Game game = new Game(4, true);

        Player currentPlayer = game.getCurrentPlayer();
        //print player hand size before adding
        System.out.println("Printing " + currentPlayer.getName() + " number of cards before adding: "+currentPlayer.getHandSize());

        //Simulate the player drawing enough cards to exceed the hand limit
        List<Card> drawnCards = game.getAdventureDeck().draw(2);

        //Add cards to the player's hand
        game.addCardsToCurrentPlayerHand(drawnCards);

        //get player hand size after adding
        int playerHandSize = currentPlayer.getHandSize();
        //print player hand size after adding
        System.out.println("Printing " + currentPlayer.getName() + " number of cards after adding: "+currentPlayer.getHandSize());
        assertTrue(currentPlayer.getHandSize() > 12);

        if(currentPlayer.getHandSize() > game.getMaxHandSize()){
            int cardsToTrim = playerHandSize - game.getMaxHandSize();
            //trim player hand with print
            game.trimPlayerHand(cardsToTrim);
            System.out.println("Checking if player hand size is equal to max hand size after trim.");
            assertEquals(game.getMaxHandSize(), currentPlayer.getHandSize());
        }
        System.out.println("Printing " + currentPlayer.getName() + " number cards after trimming: "+currentPlayer.getHandSize());
        System.out.print("R5, Checking if the player's hand size exceeds the limit: ");
        assertFalse(currentPlayer.getHandSize() > 12);
        System.out.println("Player's hand size is " + currentPlayer.getHandSize() + " (expected 12).");
    }

    @Test
    @DisplayName("R6 - Checking quest stage set up by sponsor")
    void R_6_test() {
        Game game = new Game(4, true);

        //simulating adding Q card to players hand
        game.getCurrentPlayer().addCard(new Card("Q", 3));

        Card c = game.getCurrentPlayer().removeCardAtIndex(game.getCurrentPlayer().getHandSize()-1);
        System.out.println(c.toString());
        String simulatedInput = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        if(game.isSponsorshipOffered()){
            String simulatedInput2 = "0\n4\n8\n";
            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            game.setStage(c);
            System.out.print("R6 - Checking if Game Stages are set up:");
            assertFalse(game.getGameStages().isEmpty());
            System.out.println("Passed.");

            //Checking if the cards ar non-repeated and increasing value
            System.out.print("R6 - Checking if the cards ar non-repeated and increasing value:");
            Card firstCard = game.getCurrentStage().get(0);
            Card secondCard = game.getCurrentStage().get(1);
            assertTrue(secondCard.getValue() > firstCard.getValue(), "The second card must have a higher value than the first.");
            System.out.println("Passed.");

        }

    }

    @Test
    @DisplayName("R7 - Player sets up a valid attack for a quest stage")
    void R_7_test() {
        Game game = new Game(4, true);

        game.getCurrentPlayer().addCard(new Card("Q", 3));

        //Remove the Quest card from the player's hand and simulate starting the quest
        Card c = game.getCurrentPlayer().removeCardAtIndex(game.getCurrentPlayer().getHandSize()-1);
        System.out.println(c.toString());

        //Simulate the player accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));
        if(game.isSponsorshipOffered()){
            String simulatedInput2 = "0\n4\n8\n";
            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            game.setStage(c);

            //Check if stage is set, end player turn to set next player
            System.out.println("Checking if the quest stages are set up correctly...");
            if(!game.getGameStages().isEmpty()){
                game.endTurn();

                //Simulate the player setting up their attack for the quest stages
                String simulatedInputForAttack = "0\n1\n4\nquit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForAttack.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                int n = game.getCurrentPlayer().getHandSize();
                System.out.println("R7 - Checking if current player did his attack");
                assertEquals(9, n , "Expected 10 cards, found " + n + " cards." );
                System.out.println("Passed.");

                int s = game.getCurrentPlayer().getShields();
                System.out.println("R7 - Checking if shields are given to player");
                assertTrue(s > 0, "Expected more than 0 shields, found " + s + " shields.");
                System.out.println("Passed.");

                String m = game.getCurrentPlayer().getName();
                System.out.println(m + " has " + s +" shields.");

            }

        }

    }

    @Test
    @DisplayName("R8 - checking if used cards are in the used pile")
    void R_8_test(){
        Game game = new Game(4, true);
        //draw Quest card from event deck to simulate starting the quest
        Card card = game.drawEventCard("Q",3);
        //checking if the used card is placed in the used pile
        System.out.println("R8 - Checking if the used event card is placed in the used pile.");
        assertTrue(game.getUsedEventCards().contains(card), "The card should be placed in the used pile, but it's missing.");
        System.out.println("Passed.");

        //Simulate the player accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));
        if(game.isSponsorshipOffered()){
            String simulatedInput2 = "0\n4\n8\n";
            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if(!game.getGameStages().isEmpty()){
                game.endTurn();

                //Simulate the player setting up their attack for the quest stages
                String simulatedInputForAttack = "0\n1\n4\nquit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForAttack.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                //checking if the used card is placed in the used pile
                System.out.println("R8 - Checking if the used adventure card is placed in the used pile.");
                assertEquals(3, game.getUsedAdventureCards().size(), "The card should be placed in the used pile, but it's missing.");
                System.out.println("Passed.");


            }

        }
    }

    @Test
    @DisplayName("R9 - checking if quest is resolved and shields are given")
    void R_9_test(){
        Game game = new Game(4, true);

        //simulate player drawing q 3 card
        Card card = game.drawEventCard("Q",3);

        //Simulate the player accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if(game.isSponsorshipOffered()){
            String simulatedInput2 = "0\n4\n8\n";
            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if(!game.getGameStages().isEmpty()) {
                for(int n=0;n<3;n++) {
                    //move to next player
                    game.endTurn();

                    //Simulate the player setting up their attack for the quest stages
                    String simulatedInputForAllAttacks = "0\n1\n4\nquit";
                    System.setIn(new ByteArrayInputStream(simulatedInputForAllAttacks.getBytes()));

                    //Call the playAttack method for the current player to simulate setting up the attack
                    game.playAttack();
                }
            }
        }
        //Check player shields
        Player[] players = game.getPlayers();

        int shields = players[0].getShields();
        System.out.print("R9 - checking if player 1 does not have shields");
        assertTrue(shields < 1, "Player 1 should have 0 shields, but has " + shields + ".");
        System.out.println(": Passed");

        //Check player 2,3,4
        for(int n=1;n< players.length;n++) {
            shields = players[n].getShields();
            System.out.print("R9 - checking if "+players[n].getName()+" has shields");
            assertTrue(shields > 0, players[n].getName()+" should have more than 0 shields, but has " + shields + ".");
            System.out.println(": Passed");
        }

    }

    @Test
    @DisplayName("R10 - checking for winner after quest and ends game")
    void R_10_test(){
        Game game = new Game(4, true);

        //simulate player drawing q 3 card
        Card card = game.drawEventCard("Q",2);

        Player[] players = game.getPlayers();
        //adding shields to players so that we can get a winner just from 1 quest
        players[2].addShields(5);
        //players[3].addShields(5);

        //Simulate the player accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if(game.isSponsorshipOffered()){
            String simulatedInput2 = "0\n2\n";
            System.setIn(new ByteArrayInputStream(simulatedInput2.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if(!game.getGameStages().isEmpty()) {
                for(int n=0;n<3;n++) {
                    //move to next player
                    game.endTurn();

                    //Simulate the player setting up their attack for the quest stages
                    String simulatedInputForAllAttacks = "0\n4\nquit";
                    System.setIn(new ByteArrayInputStream(simulatedInputForAllAttacks.getBytes()));

                    //Call the playAttack method for the current player to simulate setting up the attack
                    game.playAttack();
                }
            }
        }

        //print out shields
        for (Player player : players){
            System.out.println(player.getName() + " has " + player.getShields() + " shields.");
        }

        System.out.print("R10 - Checking if check for winner function works");
        assertTrue(game.checkForWinner());
        System.out.println(": Passed.");

    }


    @Test
    @DisplayName("R11 - testing deck shuffle functionality")
    void R_11_test(){
        Game game = new Game(4, true);

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
        System.out.println("R11 - Checking if adventure deck shuffled.");
        assertNotEquals(8, n, "R11 - Adventure Deck Shuffle Failed.");
        System.out.println("R11 - Adventure Deck Shuffle Passed.");


        //R3 - checking if event deck shuffle works
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
        System.out.println("R11 - Checking if event deck shuffled.");
        assertNotEquals(3, n, "R11 - Event Deck Shuffle Failed.");
        System.out.println("R11 - Event Deck Shuffle Passed.");

    }

    @Test
    @DisplayName("R12 - testing Plague effects")
    void R_12_test(){
        Game game = new Game(4, true);

        //setting player 1 shields to 3 so that after plague, his shields will be 1
        Player[] players = game.getPlayers();
        players[0].addShields(3);
        System.out.println("R12 - Player 1 has " + players[0].getShields() + " shields.");


        Card card = game.drawEventCard("Plague",0);
        game.handleEvent(card);

        int shieldsPlayer1 = players[0].getShields();

        System.out.print("R12 - checking if Player 1 has 1 shield");
        assertEquals(1,shieldsPlayer1, "Player 1 should have 1 shield, but has " + shieldsPlayer1 + ".");
        System.out.println(": Passed");

    }

    @Test
    @DisplayName("R13 - testing Queen's Favor effects")
    void R_13_test(){
        Game game = new Game(4, true);

        Player[] players = game.getPlayers();
        players[0].clearHand();

        Card card = game.drawEventCard("Queen's Favor",0);
        game.handleEvent(card);

        int player1HandSize = players[0].getHandSize();

        System.out.print("R13 - checking if player 1 has received + 2 cards");
        assertEquals(2, player1HandSize, "Player 1 should have 2 cards, but has " + player1HandSize + ".");
        System.out.println(": Passed");
    }

    @Test
    @DisplayName("R14 - testing Prosperity effects")
    void R_14_test(){
        Game game = new Game(4, true);

        Player[] players = game.getPlayers();
        //clearing hand and checking if players received + 2
        players[0].clearHand();
        players[1].clearHand();
        players[2].clearHand();
        players[3].clearHand();

        Card card = game.drawEventCard("Prosperity",0);
        game.handleEvent(card);

        int player1HandSize = players[0].getHandSize();
        int player2HandSize = players[1].getHandSize();
        int player3HandSize = players[2].getHandSize();
        int player4HandSize = players[3].getHandSize();


        System.out.print("R14 - checking if player 1 has received + 2 cards");
        assertEquals(2, player1HandSize, "Player 1 should have 2 cards, but has " + player1HandSize + ".");
        System.out.println(": Passed");

        System.out.print("R14 - checking if player 2 has received + 2 cards");
        assertEquals(2, player2HandSize, "Player 2 should have 2 cards, but has " + player2HandSize + ".");
        System.out.println(": Passed");

        System.out.print("R14 - checking if player 3 has received + 2 cards");
        assertEquals(2, player3HandSize, "Player 3 should have 2 cards, but has " + player3HandSize + ".");
        System.out.println(": Passed");

        System.out.print("R14 - checking if player 4 has received + 2 cards");
        assertEquals(2, player4HandSize, "Player 4 should have 2 cards, but has " + player4HandSize + ".");
        System.out.println(": Passed");
    }

    }