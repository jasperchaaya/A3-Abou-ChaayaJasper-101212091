Feature: Quests Game

  Scenario: Players play a quest in the A1 scenario
    Given the game has started with 4 players
    When the game is played just like the A1 compulsive scenario
    Then Player 1 should have no shields
    And Player 1 has a hand with the following cards "F5,F10,F15,F15,F15,H10,A15,A15,L20"
    And Player 2 should have 12 cards in hand
    And Player 3 should have no shields
    And Player 3 has a hand with the following cards "F5,F5,F15,F30,S10"
    And Player 4 should have 4 shields
    And Player 4 has a hand with the following cards "F15,F15,F40"
    And Player 4 wins the quest

