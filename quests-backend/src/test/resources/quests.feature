Feature: Quests Game

#Question 1
  Scenario: Players play a quest in the A1 scenario
    Given the game has started with 4 players
    When the game is played just like the A1 compulsive scenario
    Then Player 1 should have no shields
    And Player 1 has a hand with the following cards "F5,F10,F15,F15,F15,H10,A15,A15,L20"
    And Player 2 should have 12 Adventure Cards
    And Player 3 should have no shields
    And Player 3 has a hand with the following cards "F5,F5,F15,F30,S10"
    And Player 4 should have 4 shields
    And Player 4 has a hand with the following cards "F15,F15,F40"
    And Player 4 wins the quest

#Question 2
  Scenario: Players play 2 quests and have 2 game winners
    Given the game has started with 4 players
    When the first of two games is played where there are two quest winners and game winners
    Then Player 3 should have no shields
    And Player 2 wins the quest
    And Player 4 wins the quest
    And Player 2 should have 4 shields
    And Player 2 should have 4 shields

    Then the second of two games is played where there are two quest winners and game winners
    Then Player 1 should have no shields
    And Player 2 wins the game
    And Player 4 wins the game
    And Player 2 should have 7 shields
    And Player 4 should have 7 shields

#Question 3
  Scenario: Players play with event cards and there is a winner
    Given the game has started with 4 players
    When the first of two quests is played to have 3 winners
    Then Player 2 wins the quest
    And Player 2 should have 4 shields
    And Player 3 wins the quest
    And Player 3 should have 4 shields
    And Player 4 wins the quest
    And Player 4 should have 4 shields

    Then Player 2 draws a Plague card
    And Player 2 should have 2 shields

    Then Player 3 draws a Prosperity card
    And Player 1 should have 12 Adventure Cards
    And Player 2 should have 10 Adventure Cards
    And Player 3 should have 10 Adventure Cards
    And Player 4 should have 10 Adventure Cards

    Then Player 4 draws a Queen's Favor card
    Then Player 4 should have 12 Adventure Cards

    Then the second of two quests is played
    And Player 4 should have 4 shields
    And Player 2 should have 5 shields
    And Player 3 should have 7 shields
    And Player 3 wins the game


#Question 4
  Scenario: Quest played with no winners
    Given the game has started with 4 players
    When the quest is played where no one wins
    Then Player 1 should have no shields
    And Player 2 should have no shields
    And Player 3 should have no shields
    And Player 4 should have no shields
    And Player 1 should have 12 Adventure Cards
    And Player 2 should have 12 Adventure Cards
    And Player 3 should have 12 Adventure Cards
    And Player 4 should have 12 Adventure Cards

