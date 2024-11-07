
import java.io.ByteArrayInputStream;
import game.quests.Card;
import game.quests.Game;
import game.quests.Player;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.Assert.*;


public class GameSteps {

    private Game game;
    private Player[] players;

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

    @Given("the game has started with {int} players")
    public void the_game_has_started_with_players(int numPlayers) {
        game = new Game(numPlayers, true);
        players = game.getPlayers();
    }

    //Q1
    @When("the game is played just like the A1 compulsive scenario")
    public void the_game_is_played_just_like_the_a1_compulsive_scenario() {
        //Setup for Player 1
        players[0].clearHand();
        playerAddCards(players[0], "F", 5, 2);
        playerAddCards(players[0], "F", 10, 2);
        playerAddCards(players[0], "F", 15, 2);
        playerAddCards(players[0], "F", 30, 1);
        playerAddCards(players[0], "H", 10, 1);
        playerAddCards(players[0], "A", 15, 2);
        playerAddCards(players[0], "L", 20, 2);

        //Setup for Player 2
        players[1].clearHand();
        playerAddCards(players[1], "F", 5, 3);
        playerAddCards(players[1], "F", 10, 2);
        playerAddCards(players[1], "F", 20, 1);
        playerAddCards(players[1], "F", 35, 1);
        playerAddCards(players[1], "F", 60, 1);
        playerAddCards(players[1], "S", 10, 2);
        playerAddCards(players[1], "L", 20, 2);


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
        playerAddCards(players[3], "F", 10, 2);
        playerAddCards(players[3], "F", 15, 3);
        playerAddCards(players[3], "F", 40, 1);
        playerAddCards(players[3], "F", 50, 1);
        playerAddCards(players[3], "F", 70, 1);
        playerAddCards(players[3], "L", 20, 1);
        playerAddCards(players[3], "E", 30, 1);
        playerAddCards(players[3], "A", 15, 2);


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

                //End quest
                game.endQuest();
            }
        }
    }

    //Q2 1st quest
    @When("the first of two games is played where there are two quest winners and game winners")
    public void the_first_of_two_games_is_played_where_there_are_two_quest_winners_and_game_winners(){
        //Setup for Player 1
        players[0].clearHand();
        playerAddCards(players[0], "F", 5, 2);
        playerAddCards(players[0], "F", 10, 2);
        playerAddCards(players[0], "F", 15, 2);
        playerAddCards(players[0], "F", 30, 1);
        playerAddCards(players[0], "H", 10, 1);
        playerAddCards(players[0], "A", 15, 2);
        playerAddCards(players[0], "L", 20, 2);

        //Setup for Player 2
        players[1].clearHand();
        playerAddCards(players[1], "F", 5, 3);
        playerAddCards(players[1], "F", 10, 2);
        playerAddCards(players[1], "F", 20, 1);
        playerAddCards(players[1], "F", 35, 1);
        playerAddCards(players[1], "F", 60, 1);
        playerAddCards(players[1], "S", 10, 2);
        playerAddCards(players[1], "L", 20, 2);


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
        playerAddCards(players[3], "F", 10, 2);
        playerAddCards(players[3], "F", 15, 3);
        playerAddCards(players[3], "F", 40, 1);
        playerAddCards(players[3], "F", 50, 1);
        playerAddCards(players[3], "F", 70, 1);
        playerAddCards(players[3], "L", 20, 1);
        playerAddCards(players[3], "E", 30, 1);
        playerAddCards(players[3], "A", 15, 2);


        //simulate player drawing q 4 card
        Card card = game.drawEventCard("Q", 4);

        //Simulate player 1 accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if (game.isSponsorshipOffered()) {
            String simulatedInputP1 = "0\n2\n3\n6\n";
            System.setIn(new ByteArrayInputStream(simulatedInputP1.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if (!game.getGameStages().isEmpty()) {
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP2Attacks = "1\n3\n3\n6\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP2Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP3Attacks = "0\nquit\n";
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

            }
        }
    }

    //Q2 2nd quest
    @Then("the second of two games is played where there are two quest winners and game winners")
    public void the_second_of_two_games_is_played_where_there_are_two_quest_winners_and_game_winners(){

        //simulate player drawing q 3 card
        Card card = game.drawEventCard("Q", 3);

        //Simulate player 3 accepting sponsorship for the quest
        String simulatedInputSponsorship = "N\nN\nY\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if (game.isSponsorshipOffered()) {
            String simulatedInputP1 = "0\n2\n3\n";
            System.setIn(new ByteArrayInputStream(simulatedInputP1.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if (!game.getGameStages().isEmpty()) {
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP2Attacks = "1\n3\n3\n6\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP2Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP3Attacks = "0\nquit\n";
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

            }
        }
    }

    //Q3 1st quest
    @When("the first of two quests is played to have 3 winners")
    public void the_first_of_two_quests_is_played_to_have_3_winners(){

        //setting up player 1 hand
        players[0].clearHand();
        playerAddCards(players[0], "F", 5, 2);
        playerAddCards(players[0], "F", 10, 2);
        playerAddCards(players[0], "F", 15, 2);
        playerAddCards(players[0], "F", 30, 1);
        playerAddCards(players[0], "H", 10, 1);
        playerAddCards(players[0], "A", 15, 2);
        playerAddCards(players[0], "L", 20, 2);


        //simulate player drawing q 3 card
        Card card = game.drawEventCard("Q", 4);

        //Simulate player 1 accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if (game.isSponsorshipOffered()) {
            String simulatedInputP1 = "0\n3\n4\n6\n";
            System.setIn(new ByteArrayInputStream(simulatedInputP1.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if (!game.getGameStages().isEmpty()) {
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP2Attacks = "1\n3\n3\n6\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP2Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                //Simulate player 3 setting up their attack for the quest stages
                String simulatedInputForP3Attacks = "1\n3\n3\n6\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP3Attacks.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                //move to next player
                game.endTurn();

                //Simulate player 4 setting up their attack for the quest stages
                String simulatedInputForP4Attacks = "1\n3\n3\n6\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP4Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                game.endQuest();

            }
        }
    }

    //Q3 2nd quest
    @Then("the second of two quests is played")
    public void the_second_of_two_quests_is_played(){

        //simulate player drawing q 3 card
        Card card = game.drawEventCard("Q", 3);

        //Simulate player 1 accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if (game.isSponsorshipOffered()) {
            String simulatedInputP1 = "0\n6\n8\n";
            System.setIn(new ByteArrayInputStream(simulatedInputP1.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if (!game.getGameStages().isEmpty()) {
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP2Attacks = "1\n3\n6\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP2Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                //Simulate player 3 setting up their attack for the quest stages
                String simulatedInputForP3Attacks = "1\n3\n3\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP3Attacks.getBytes()));
                //Call the playAttack method for th current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                //Simulate player 4 setting up their attack for the quest stages
                String simulatedInputForP4Attacks = "quit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP4Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                game.endQuest();

            }
        }
    }


    //Q3 p2 draws Plague event card
    @Then("Player 2 draws a Plague card")
    public void player_2_draws_a_plague_card(){
        Card card = game.drawEventCard("Plague",0);
        game.handleEvent(card);
        game.endTurn();
    }

    //Q3 p3 draws Prosperity event card
    @Then("Player 3 draws a Prosperity card")
    public void player_3_draws_a_Prosperity_card(){
        Card card = game.drawEventCard("Prosperity",0);
        game.handleEvent(card);
        game.endTurn();
    }

    //Q3 p4 gets Queen’s Favor event card
    @Then("Player 4 draws a Queen's Favor card")
    public void player_4_draws_a_plague_card(){
        Card card = game.drawEventCard("Queen's Favor",0);
        game.handleEvent(card);
        game.endTurn();
    }

    //Q4
    @When("the quest is played where no one wins")
    public void the_quest_is_played_where_no_one_wins(){

        //setting up player 1 hand
        players[0].clearHand();
        playerAddCards(players[0], "F", 5, 2);
        playerAddCards(players[0], "F", 10, 2);
        playerAddCards(players[0], "F", 15, 2);
        playerAddCards(players[0], "F", 30, 1);
        playerAddCards(players[0], "H", 10, 1);
        playerAddCards(players[0], "A", 15, 2);
        playerAddCards(players[0], "L", 20, 2);



        //simulate player drawing q 2 card
        Card card = game.drawEventCard("Q", 2);

        //Simulate player 1 accepting sponsorship for the quest
        String simulatedInputSponsorship = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInputSponsorship.getBytes()));

        //setting up stages
        if (game.isSponsorshipOffered()) {
            String simulatedInputP1 = "0\n5\n";
            System.setIn(new ByteArrayInputStream(simulatedInputP1.getBytes()));
            game.setStage(card);

            //Check if stage is set, end player turn to set next player
            if (!game.getGameStages().isEmpty()) {
                //move to next player
                game.endTurn();

                //Simulate player 2 setting up their attack for the quest stages
                String simulatedInputForP2Attacks = "quit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP2Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                //Simulate player 3 setting up their attack for the quest stages
                String simulatedInputForP3Attacks = "quit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP3Attacks.getBytes()));

                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();

                //move to next player
                game.endTurn();

                //Simulate player 4 setting up their attack for the quest stages
                String simulatedInputForP4Attacks = "quit\n";
                System.setIn(new ByteArrayInputStream(simulatedInputForP4Attacks.getBytes()));
                //Call the playAttack method for the current player to simulate setting up the attack
                game.playAttack();
                //move to next player
                game.endTurn();

                game.endQuest();

            }
        }
    }


    @Then("Player {int} should have {int} Adventure Cards")
    public void player_should_have_adventure_cards(int playerIndex, int expectedAdvCards) {
        int actualAdvCards = players[playerIndex - 1].getHandSize();
        assertEquals(expectedAdvCards, actualAdvCards, "Player " + playerIndex + " did not have the correct number of Adventure Cards.");
    }


    @Then("Player {int} should have no shields")
    public void player_should_have_no_shields(int playerIndex) {
        int actualShields = players[playerIndex - 1].getShields();
        assertEquals(0, actualShields, "Player " + playerIndex + " should have no shields.");
    }

    @Then("Player {int} has a hand with the following cards {string}")
    public void player_has_a_hand_with_the_following_cards(int playerIndex, String hand) {
        //Split the card list
        String[] cards = hand.split(",");
        for (String cardInfo : cards) {
            String type = cardInfo.substring(0,1);  //Get the card type
            int value = Integer.parseInt(cardInfo.substring(1));  //Get the card value
            assertTrue(playerHasCard(players[playerIndex-1],
                    new Card(type, value)),
                    players[playerIndex-1].getName() + " should have " + type + value +".");
        }
    }

    @Then("Player {int} should have {int} shields")
    public void player_should_have_shields(int playerIndex, int expectedShields) {
        assertEquals(expectedShields, players[playerIndex - 1].getShields(), "Player " + playerIndex + " did not have the correct number of shields.");
    }


    @Then("Player {int} wins the quest")
    public void player_wins_the_quest(int playerIndex) {
        assertTrue(players[playerIndex - 1].getQuestWinner(),
                players[playerIndex - 1].getName() + " expected to win quest, got false.");
    }

    @Then("Player {int} wins the game")
    public void player_wins_the_game(int playerIndex) {
        assertTrue(game.isWinner(playerIndex),
                players[playerIndex - 1].getName() + " expected to win game, got false.");
    }

}