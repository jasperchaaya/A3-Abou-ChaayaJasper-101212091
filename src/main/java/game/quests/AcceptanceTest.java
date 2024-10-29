package game.quests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptanceTest {
    //private helper, check if player has specific card
    private boolean playerHasCard(Player player, Card card){
        return player.getHand().stream().anyMatch(c -> c.getType().equals(card.getType()) && c.getValue() == card.getValue());
    }
    //private helper, add cards to player
    private void playerAddCards(Player player, String type, int value, int count){
        for(int n=0;n<count;n++){
            if (player.getHandSize() < 12) {
                player.addCard(new Card(type,value));  // Add 12 dummy cards
            }
        }
    }

    @Test
    @DisplayName("A-TEST JP-Scenario")
    void A_Test() {
        //Initializing game with 4 players and shuffled decks
        Game game = new Game(4, true);

        Player[] players = game.getPlayers();

        //Setup for Player 1
        players[0].clearHand();
        playerAddCards(players[0],"F",5,2);
        playerAddCards(players[0],"F",10,2);
        playerAddCards(players[0],"F",15,2);
        playerAddCards(players[0],"F",30,1);
        playerAddCards(players[0],"H",10,1);
        playerAddCards(players[0],"A",15,2);
        playerAddCards(players[0],"L",20,2);

        //Setup for Player 2
        players[1].clearHand();
        playerAddCards(players[1],"F",5,3);
        playerAddCards(players[1],"F",10,2);
        playerAddCards(players[1],"F",20,1);
        playerAddCards(players[1],"F",35,1);
        playerAddCards(players[1],"F",60,1);
        playerAddCards(players[1],"S",10,2);
        playerAddCards(players[1],"L",20,2);



        //Setup for Player 3
        players[2].clearHand();
        playerAddCards(players[2], "F", 5, 3);
        playerAddCards(players[2], "F", 15, 2);
        playerAddCards(players[2], "F", 30, 1);
        playerAddCards(players[2], "F", 40, 1);
        playerAddCards(players[2], "L", 20, 1);
        playerAddCards(players[2], "S", 10, 2);
        playerAddCards(players[2], "H", 10, 1);
        playerAddCards(players[2], "F", 50, 1);


        //Setup for Player 4
        players[3].clearHand();
        playerAddCards(players[3],"F",10,2);
        playerAddCards(players[3],"F",15,3);
        playerAddCards(players[3],"F",40,1);
        playerAddCards(players[3],"F",50,1);
        playerAddCards(players[3],"F",70,1);
        playerAddCards(players[3],"L",20,1);
        playerAddCards(players[3],"E",30,1);
        playerAddCards(players[3],"A",15,2);



        //simulate player drawing q 4 card
        Card card = game.drawEventCard("Q", 4);


        //Simulate player 2 accepting sponsorship for the quest
        String simulatedInputSponsorship = "N\nY\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if (game.isSponsorshipOffered()) {
            String simulatedInputP2 = "0\n2\n5\n7\n";
            System.setIn(new ByteArrayInputStream(simulatedInputP2.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if (!game.getGameStages().isEmpty()) {
                //move to next player
                game.endTurn();

                //Simulate player 3 setting up their attack for the quest stages
                String simulatedInputForP3Attacks = "1\n3\n3\nquit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP3Attacks.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                //move to next player
                game.endTurn();

                //Simulate player 4 setting up their attack for the quest stages
                String simulatedInputForP4Attacks = "1\n2\n4\n7\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP4Attacks.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                //move to next player
                game.endTurn();

                //Simulate the player 1 setting up their attack for the quest stages
                String simulatedInputForP1Attacks = "1\n2\nquit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP1Attacks.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                //move to next player

                //End quest
                game.endQuest();


                //assert after stage 2
                System.out.print("A-test - Checking if Player 1 has no shields:");
                assertEquals(0, players[0].getShields(),"Player 1 expected to have 0 shields but found " + players[0].getShields() + " shields");
                System.out.println(" Passed.");

                System.out.println("A-test - Checking if Player 1 has the cards: F5 FIO F15 F15 F30 H A A L");
                assertTrue(playerHasCard(players[0],new Card("F", 5)), "P1 should have F 5.");
                assertTrue(playerHasCard(players[0],new Card("F", 10)), "P1 should have F 10.");
                assertTrue(playerHasCard(players[0],new Card("F", 15)), "P1 should have F 15.");
                assertTrue(playerHasCard(players[0],new Card("F", 15)), "P1 should have F 15.");
                assertTrue(playerHasCard(players[0],new Card("F", 15)), "P1 should have F 30.");
                assertTrue(playerHasCard(players[0],new Card("H", 10)), "P1 should have H 10.");
                assertTrue(playerHasCard(players[0],new Card("A", 15)), "P1 should have A 15.");
                assertTrue(playerHasCard(players[0],new Card("A", 15)), "P1 should have A 15.");
                assertTrue(playerHasCard(players[0],new Card("L", 20)), "P1 should have L 20.");
                System.out.println(" P1 cards check Passed.");


                }

                //After stage 4 asserts

                //Player 3 asserts
                System.out.println("A-test - Checking if Player 3 has no shields and has F5 F5 F15 F30 S");
                assertEquals(0, players[2].getShields(), "Player 3 did not have 0 shields, found " + players[2].getShields() + " shields.");
                assertTrue(playerHasCard(players[2],new Card("F", 5)), "P3 should have F 5.");
                assertTrue(playerHasCard(players[2],new Card("F", 5)), "P3 should have F 5.");
                assertTrue(playerHasCard(players[2],new Card("F", 15)), "P3 should have F 15.");
                assertTrue(playerHasCard(players[2],new Card("F", 30)), "P3 should have F 30.");
                assertTrue(playerHasCard(players[2],new Card("S", 10)), "P3 should have S 10.");
                System.out.println("P3  asserts passed.");

                //Player 4 asserts
                System.out.println("A-test - Checking if Player 4 has 4 shields and has F15 F15 F40");
                assertEquals(4, players[3].getShields(), "Player 4 did not have 4 shields, found " + players[3].getShields() + " shields.");
                assertTrue(playerHasCard(players[3],new Card("F", 15)), "P3 should have F 15.");
                assertTrue(playerHasCard(players[3],new Card("F", 15)), "P3 should have F 15.");
                assertTrue(playerHasCard(players[3],new Card("F", 40)), "P3 should have F 40.");
                System.out.println("P4 asserts passed.");

                //Player 2 asserts
                System.out.println("A-test - Checking if Player 2 has 12 cards in hand.");
                assertEquals(12, players[1].getHandSize(), "Player 2 did not have 12 cards, found " + players[1].getHandSize() + " cards.");
                System.out.println("P2 asserts passed.");



            }
        }

}
