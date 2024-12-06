package com.quests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8081")
public class QuestsController {

    private final ConcurrentHashMap<String, Game> gameCache = new ConcurrentHashMap<>();

    public QuestsController() { }

    private String StartGame(String sessionId){
        if (gameCache.containsKey(sessionId)) {
            return "Game already started with session id: " + sessionId;
        }
        StringBuilder result = new StringBuilder();
        try {
            Game game = new Game(4, true);  // Initialize the game with 4 players
            gameCache.put(sessionId, game);  // Cache the game instance with the user ID
            String player = game.getCurrentPlayer().getName();
            result.append("Quests Game started for session: ").append(sessionId).append("\nCurrent player:  ").append(player);
            return result.toString();
        } catch (Exception e) {
            return result.toString() + "\nFailed to start the game: " + e.getMessage();
        }
    }
    @PostMapping("/start")
    public ResponseEntity<String> startGame(@RequestParam String sessionId) {
        return ResponseEntity.ok(StartGame(sessionId));
    }

    @PostMapping("/yes")
    public ResponseEntity<String> handleYes(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        StringBuilder result = new StringBuilder();
        try {
            List<Card> hand = game.getCurrentPlayer().getHand();
            for (Card c : hand) {
                result.append(c.toString()).append(",");
            }
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result.toString() + "Error processing 'Yes' action: " + e.getMessage());
        }
    }

    @PostMapping("/no")
    public ResponseEntity<String> handleNo(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        StringBuilder result = new StringBuilder("\nQuest Declined");
        try {
            game.endTurn();
            String player = game.getCurrentPlayer().getName();
            result.append("\n").append(player).append(", Do you wish to sponsor? Yes/No");
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing 'No' action: " + e.getMessage());
        }
    }

    @DeleteMapping("/reset")
    public ResponseEntity<String> resetGame(@RequestParam String sessionId) {
        if (gameCache.remove(sessionId) != null) {
            return ResponseEntity.ok("Game cleared for session: " + sessionId);
        } else {
            return ResponseEntity.badRequest().body("No game found for session: " + sessionId);
        }
    }

    @PostMapping("/setStage")
    public ResponseEntity<String> setStage(@RequestParam String sessionId,@RequestParam String selected) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        try {
            Player player = game.getCurrentPlayer();
            game.setStageOwner(player);
            List<Card> stage = new ArrayList<>();
            System.out.println("stage owner: " + game.getStageOwner());//or player
            int[] intArray = Arrays.stream(selected.split(",")).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.toString(intArray));
            for (int j : intArray) {
                Card c = player.getCardAtIndex(j);
                stage.add(c);
            }
            game.addGameStage(stage);

            for(Card c : stage){player.removeCard(c);}

            System.out.println(player.getHandSize());
            player.printHand();
            System.out.println(stage);

            game.endTurn();
            //get info for next player
            StringBuilder result = new StringBuilder(game.getCurrentPlayer().getName()+"'s turn|");
            List<Card> hand = game.getCurrentPlayer().getHand();
            for (Card c : hand) {
                result.append(c.toString()).append(",");
            }
            //save game in cache
            gameCache.put(sessionId, game);
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing 'setStage' action: " + e.getMessage());
        }
    }

    @PostMapping("/playAttack")
    public ResponseEntity<String> playAttack(@RequestParam String sessionId, @RequestParam String selected) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        try {
            int[] intArray = Arrays.stream(selected.split(",")).mapToInt(Integer::parseInt).toArray();
            List<Card> attackCards = new ArrayList<>();
            Player player = game.getCurrentPlayer();
            for (int index : intArray) {
                // Validate index
                if (index < 0 || index > player.getHandSize()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Selected index " + index + " is out of bounds.");
                }
                // Retrieve the card
                Card selectedCard = player.getCardAtIndex(index);
                // Validate card
                if (selectedCard == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: No card found at index " + index);
                }
                // Check if the card is already used in the attack
                if (attackCards.contains(selectedCard)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Duplicate card selected for attack.");
                }

                // Add the card to attack and the used pile,
                attackCards.add(selectedCard);
                game.addUsedAdventureCard(selectedCard);
            }
            // remove played cards from the player's hand
            for (int index : intArray) {
                player.removeCardAtIndex(index);
                player.addCard(game.getAdventureDeck().draw());
            }
            game.trimPlayerHand(player);

            // Check if attack setup satisfies the stage
            if (attackCards.size() >= game.getCurrentStage().size()) {
                //player.addShields(game.getCurrentStage().size()); // Award shields
                player.addShields(game.getStageLevel()); // Award shields
                player.setQuestWinner(true); // Set quest winner
            }

            // Prepare response with updated hand
            StringBuilder result = new StringBuilder("Attack Cards: ");
            for (Card c : attackCards) {
                result.append(c.toString()).append(",");
            }
            result.setLength(result.length() - 1);//remove last comma
            game.endTurn();

            if (game.getStageOwner().equals(game.getCurrentPlayer().getName())) {
                game.endQuest();
                result.append("\nQuest ended......\n");
                result.append(questWinners(game));
                String gameWinners = gameWinners(game);
                if(gameWinners.isEmpty()){
                    result.append("\nCurrent Player: ").append(game.getCurrentPlayer().getName()).append("|");
                    result.append(getHand(game));
                }else{
                    result.append("\n").append(gameWinners);
                }
            }else {
                result.append('\n').append(game.getCurrentPlayer().getName()).append(" turn, select cards to attack|");
                result.append(getHand(game));
            }
            gameCache.put(sessionId, game);
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in 'playAttack' action: " + e.getMessage());
        }
    }

    @PostMapping("/skipAttack")
    public ResponseEntity<String> skipAttack(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        try {
            StringBuilder result = new StringBuilder();
            String currentPlayer = game.getCurrentPlayer().getName();
            result.append(currentPlayer).append(" skipped attack\n");
            game.endTurn();
            currentPlayer = game.getCurrentPlayer().getName();
            // Prepare response
            if (game.getStageOwner().equals(currentPlayer)) {
                game.endQuest();
                result.append("\nQuest ended......\n");
                result.append(questWinners(game));
                String gameWinners = gameWinners(game);
                if(gameWinners.isEmpty()){
                    result.append("\nCurrent Player: ").append(currentPlayer).append("|");
                    result.append(getHand(game));
                }else{
                    result.append("\n").append(gameWinners);
                }
            }else {
                result.append('\n').append(currentPlayer).append(" turn, select cards to attack|");
                result.append(getHand(game));
            }
            gameCache.put(sessionId, game);
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in 'playAttack' action: " + e.getMessage());
        }
    }

    @PostMapping("/getCurrentPlayer")
    public ResponseEntity<String> getCurrentPlayer(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        return ResponseEntity.ok(game.getCurrentPlayer().getName());
    }

    @PostMapping("/getCurrentPlayerHand")
    public ResponseEntity<String> getCurrentPlayerHand(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        StringBuilder result = new StringBuilder();
        List<Card> hand = game.getCurrentPlayer().getHand();
        for (Card c : hand) {
            result.append(c.toString()).append(",");
        }
        return ResponseEntity.ok(result.substring(0, result.lastIndexOf(",")));
    }

    @PostMapping("/drawEventCard")
    public ResponseEntity<String> drawEventCard(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        try {
            Card drawnCard = game.getEventDeck().draw();
            if (drawnCard == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No cards left in the event deck.");
            }
            if(drawnCard.getType().equals("Q")){
                game.setStageLevel(drawnCard.getValue());
            }
            game.addUsedEventCard(drawnCard);
            gameCache.put(sessionId, game);
            return ResponseEntity.ok(drawnCard.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error drawing event card: " + e.getMessage());
        }
    }

    @PostMapping("/getPlayersShields")
    public ResponseEntity<String> getPlayersShields(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            StringBuilder result = new StringBuilder();
            for (Player p : game.getPlayers()) {
                result.append(p.getName()).append(" Shields: ").append(p.getShields()).append(" \r\n");
            }
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/updatePlayerShields")
    public ResponseEntity<String> updatePlayerShields(@RequestParam String sessionId, @RequestParam int shieldCount) {
        Game game = gameCache.get(sessionId);
        if (game != null) {
            if(shieldCount < 0){
                game.getCurrentPlayer().removeShields(Math.abs(shieldCount));
            }else{
                game.getCurrentPlayer().addShields(shieldCount);
            }
            gameCache.put(sessionId, game);
        }
        return getPlayersShields(sessionId);
    }

    @PostMapping("/endTurn")
    public String endTurn(@RequestParam String sessionId) {
        try {
            Game game = gameCache.get(sessionId);
            if (game != null) {
                game.endTurn();
                gameCache.put(sessionId, game);
                return game.getCurrentPlayer().getName();
            }
        }catch(Exception exception){
            return exception.getMessage();
        }
        return "";
    }

    @PostMapping("/handleEventCard")
    public String handleEventCard(@RequestParam String sessionId,@RequestParam String cardType) {
        try {
            Game game = gameCache.get(sessionId);
            if (game != null) {
                game.handleEvent(cardType);
            }
        }catch(Exception exception){
            return exception.getMessage();
        }
        return "";
    }

    //private helpers
    private String getHand(Game game){
        StringBuilder result = new StringBuilder();
        // Include the current player's hand
        List<Card> hand = game.getCurrentPlayer().getHand();
        for (Card c : hand) {
            result.append(c.toString()).append(",");
        }
        return result.toString();
    }

    private String questWinners(Game game){
        StringBuilder result = new StringBuilder();
        for(Player p : game.getPlayers()){
            if(p.getQuestWinner()){
                result.append("Quest Winner: ").append(p.getName()).append("\n");
            }
            p.setQuestWinner(false);
        }
        return result.toString();
    }

    private String gameWinners(Game game){
        StringBuilder result = new StringBuilder();
        for(Player p : game.getPlayers()){
            if(p.getShields() >= game.getMaxShields()){
                result.append("Game Winner: ").append(p.getName()).append("\n");
            }
        }
        return result.toString();
    }

    //setting up scenarios for selenium testing
    @PostMapping("/setUpScenario1")
    public ResponseEntity<String> setUpScenario1(@RequestParam String sessionId) {
        StartGame(sessionId);
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        Player[] players = game.getPlayers();

        //Setup for Player 1
        players[0].clearHand();
        game.playerAddCards(players[0],"F",5,2);
        game.playerAddCards(players[0],"F",10,2);
        game.playerAddCards(players[0],"F",15,2);
        game.playerAddCards(players[0],"F",30,1);
        game.playerAddCards(players[0],"H",10,1);
        game.playerAddCards(players[0],"A",15,2);
        game.playerAddCards(players[0],"L",20,2);

        //Setup for Player 2
        players[1].clearHand();
        game.playerAddCards(players[1],"F",5,3);
        game.playerAddCards(players[1],"F",10,1);
        game.playerAddCards(players[1],"F",15,1);
        game.playerAddCards(players[1],"F",20,1);
        game.playerAddCards(players[1],"F",30,1);
        game.playerAddCards(players[1],"F",60,1);
        game.playerAddCards(players[1],"S",10,2);
        game.playerAddCards(players[1],"L",20,2);

        //Setup for Player 3
        players[2].clearHand();
        game.playerAddCards(players[2], "F", 5, 3);
        game.playerAddCards(players[2], "F", 15, 2);
        game.playerAddCards(players[2], "F", 30, 1);
        game.playerAddCards(players[2], "F", 40, 1);
        game.playerAddCards(players[2], "L", 20, 1);
        game.playerAddCards(players[2], "S", 10, 2);
        game.playerAddCards(players[2], "H", 10, 1);
        game.playerAddCards(players[2], "F", 50, 1);

        //Setup for Player 4
        players[3].clearHand();
        game.playerAddCards(players[3],"F",10,2);
        game.playerAddCards(players[3],"F",15,3);
        game.playerAddCards(players[3],"F",40,1);
        game.playerAddCards(players[3],"F",50,1);
        game.playerAddCards(players[3],"F",70,1);
        game.playerAddCards(players[3],"L",20,1);
        game.playerAddCards(players[3],"E",30,1);
        game.playerAddCards(players[3],"A",15,2);

        //setting event cards
        game.clearEventDeck();
        game.addCardToEventDeck(new Card("Q", 4));

        //setting adventure deck
        game.clearAdventureDeck();
        List<Card> cards = new ArrayList<Card>();

        cards.add(new Card("F", 30));
        cards.add(new Card("S", 10));
        cards.add(new Card("A", 15));

        cards.add(new Card("F", 10));
        cards.add(new Card("L", 20));
        cards.add(new Card("L", 20));

        cards.add(new Card("F", 30));
        cards.add(new Card("L", 20));
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));


        game.addCardToAdventureDeck(cards);

        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/setUpScenario2")
    public ResponseEntity<String> setUpScenario2(@RequestParam String sessionId) {
        StartGame(sessionId);
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        Player[] players = game.getPlayers();

        //Setup for Player 1
        players[0].clearHand();
        game.playerAddCards(players[0],"F",5,2);
        game.playerAddCards(players[0],"F",10,2);
        game.playerAddCards(players[0],"F",15,2);
        game.playerAddCards(players[0],"D",5,1);
        game.playerAddCards(players[0],"H",10,2);
        game.playerAddCards(players[0],"A",15,2);
        game.playerAddCards(players[0],"L",20,1);

        //Setup for Player 2
        players[1].clearHand();
        game.playerAddCards(players[1],"F",40,1);
        game.playerAddCards(players[1],"F",50,1);
        game.playerAddCards(players[1],"H",10,2);
        game.playerAddCards(players[1],"A",15,2);
        game.playerAddCards(players[1],"L",20,2);
        game.playerAddCards(players[1],"S",10,3);
        game.playerAddCards(players[1],"E",30,1);

        //Setup for Player 3
        players[2].clearHand();
        game.playerAddCards(players[2],"F",5,4);
        game.playerAddCards(players[2],"D",5,3);
        game.playerAddCards(players[2],"H",10,5);

        //Setup for Player 4
        players[3].clearHand();
        game.playerAddCards(players[3], "F", 5, 1);
        game.playerAddCards(players[3], "F", 50,1);
        game.playerAddCards(players[3], "H", 10,2);
        game.playerAddCards(players[3], "L", 20,2);
        game.playerAddCards(players[3], "S", 10,3);
        game.playerAddCards(players[3], "A", 15,2);
        game.playerAddCards(players[3], "E", 30,1);

        game.clearEventDeck();
        game.addCardToEventDeck(new Card("Q", 4));
        game.addCardToEventDeck(new Card("Q", 3));

        game.clearAdventureDeck();
        List<Card> cards = new ArrayList<Card>();
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("F", 40));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 30));
        cards.add(new Card("F", 30));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 30));
        //2nd quest
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 30));
        cards.add(new Card("L", 20));
        
        game.addCardToAdventureDeck(cards);
        
        

        //must save the game state back to session
        gameCache.put(sessionId, game);

        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/setUpScenario3")
    public ResponseEntity<String> setUpScenario3(@RequestParam String sessionId) {
        StartGame(sessionId);
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        Player[] players = game.getPlayers();

        //Setup for Player 1
        players[0].clearHand();
        game.playerAddCards(players[0],"F",5,2);
        game.playerAddCards(players[0],"F",10,2);
        game.playerAddCards(players[0],"F",15,2);
        game.playerAddCards(players[0],"F",20,2);
        game.playerAddCards(players[0],"D",5,4);

        //Setup for Player 2
        players[1].clearHand();
        game.playerAddCards(players[1],"F",25,1);
        game.playerAddCards(players[1],"F",30,1);
        game.playerAddCards(players[1],"H",10,2);
        game.playerAddCards(players[1],"S",10,3);
        game.playerAddCards(players[1],"A",15,2);
        game.playerAddCards(players[1],"L",20,2);
        game.playerAddCards(players[1],"E",30,1);


        //Setup for Player 3
        players[2].clearHand();
        game.playerAddCards(players[2],"F",25,1);
        game.playerAddCards(players[2],"F",30,1);
        game.playerAddCards(players[2],"H",10,2);
        game.playerAddCards(players[2],"S",10,3);
        game.playerAddCards(players[2],"A",15,2);
        game.playerAddCards(players[2],"L",20,2);
        game.playerAddCards(players[2],"E",30,1);

        //Setup for Player 4
        players[3].clearHand();
        game.playerAddCards(players[3],"F",25,1);
        game.playerAddCards(players[3],"F",30,1);
        game.playerAddCards(players[3],"F",70,1);
        game.playerAddCards(players[3],"H",10,2);
        game.playerAddCards(players[3],"S",10,3);
        game.playerAddCards(players[3],"A",15,2);
        game.playerAddCards(players[3],"L",20,1);
        game.playerAddCards(players[3],"E",30,1);

        //setting event cards
        game.clearEventDeck();
        game.addCardToEventDeck(new Card("Q", 4));
        game.addCardToEventDeck(new Card("Plague", 0));
        game.addCardToEventDeck(new Card("Prosperity", 0));
        game.addCardToEventDeck(new Card("Queen's Favor", 0));
        game.addCardToEventDeck(new Card("Q", 3));

        //setting adventure deck
        game.clearAdventureDeck();
        List<Card> cards = new ArrayList<Card>();
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));

        cards.add(new Card("F", 25));
        cards.add(new Card("F", 25));

        cards.add(new Card("H", 10));
        cards.add(new Card("S", 10));

        cards.add(new Card("A", 15));
        cards.add(new Card("F", 40));

        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));

        cards.add(new Card("F", 30));
        cards.add(new Card("F", 25));

        cards.add(new Card("A", 15));
        cards.add(new Card("H", 10));
        cards.add(new Card("F", 50));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("F", 40));
        cards.add(new Card("F", 50));

        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));

        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));

        cards.add(new Card("F", 35));

        //draws prosperity
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 10));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));

        //queens favor
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 15));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 20));
        cards.add(new Card("F", 25));
        cards.add(new Card("F", 30));
        cards.add(new Card("L", 20));

        game.addCardToAdventureDeck(cards);

        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/setUpScenario4")
    public ResponseEntity<String> setUpScenario4(@RequestParam String sessionId) {
        StartGame(sessionId);
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        Player[] players = game.getPlayers();

        //Setup for Player 1
        players[0].clearHand();
        game.playerAddCards(players[0],"F",50,1);
        game.playerAddCards(players[0],"F",70,1);
        game.playerAddCards(players[0],"D",5,2);
        game.playerAddCards(players[0],"H",10,2);
        game.playerAddCards(players[0],"S",10,2);
        game.playerAddCards(players[0],"A",15,2);
        game.playerAddCards(players[0],"L",20,2);

        //Setup for Player 2
        players[1].clearHand();
        game.playerAddCards(players[1],"F",5,2);
        game.playerAddCards(players[1],"F",10,1);
        game.playerAddCards(players[1],"F",15,2);
        game.playerAddCards(players[1],"F",20,2);
        game.playerAddCards(players[1],"F",25,1);
        game.playerAddCards(players[1],"F",30,2);
        game.playerAddCards(players[1],"F",40,1);
        game.playerAddCards(players[1],"E",30,1);

        //Setup for Player 3
        players[2].clearHand();
        game.playerAddCards(players[2], "F", 5, 2);
        game.playerAddCards(players[2], "F", 10, 1);
        game.playerAddCards(players[2], "F", 15, 2);
        game.playerAddCards(players[2], "F", 20, 2);
        game.playerAddCards(players[2], "F", 25, 2);
        game.playerAddCards(players[2], "F", 30, 1);
        game.playerAddCards(players[2], "F", 40, 1);
        game.playerAddCards(players[2], "L", 20, 1);

        //Setup for Player 4
        players[3].clearHand();
        game.playerAddCards(players[3],"F",5,2);
        game.playerAddCards(players[3],"F",10,1);
        game.playerAddCards(players[3],"F",15,2);
        game.playerAddCards(players[3],"F",20,2);
        game.playerAddCards(players[3],"F",25,2);
        game.playerAddCards(players[3],"F",30,1);
        game.playerAddCards(players[3],"F",50,1);
        game.playerAddCards(players[3],"E",30,1);

        //setting event cards
        game.clearEventDeck();
        game.addCardToEventDeck(new Card("Q", 2));

        //setting adventure deck
        game.clearAdventureDeck();
        List<Card> cards = new ArrayList<Card>();

        cards.add(new Card("F", 5));
//        cards.add(new Card("F", 15));
//        cards.add(new Card("F", 10));


        cards.add(new Card("F", 15));
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("D", 5));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("H", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("S", 10));
        cards.add(new Card("F", 5));
        cards.add(new Card("F", 10));

        game.addCardToAdventureDeck(cards);

        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/displayAllHands")
    public ResponseEntity<String> displayAllHands(@RequestParam String sessionId) {
        Game game = gameCache.get(sessionId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game has not been started");
        }
        StringBuilder result = new StringBuilder("<h3>All Player's Hands</h3><ul>");
        for(Player player:game.getPlayers()){
            result.append("<li>").append(player.getName()).append(" Number of Cards (").append(player.getHandSize()).append("): ");
            for (Card c : player.getHand()) {
                result.append(c.toString()).append(",");
            }
            result.append("</li>");
        }
        result.append("</ul>");
        return ResponseEntity.ok(result.toString().replace(",</li>","</li>"));
    }


}