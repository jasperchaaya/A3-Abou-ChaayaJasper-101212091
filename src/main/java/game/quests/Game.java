package game.quests;

import java.util.*;
import java.util.Scanner;


public class Game {
    private Deck adventureDeck;
    private Deck eventDeck;
    private Player[] players;
    private int currentPlayer;
    private final int maxHandSize = 12;
    //private List<Card> gameStages = new ArrayList<>();
    private List<List<Card>> gameStages = new ArrayList<>();

    //floor used cards
    private List<Card> usedAdventureCards = new ArrayList<>();
    private List<Card> usedEventCards = new ArrayList<>();

    private Player stageOwner;
    Scanner scanner;

    public Game(int numberOfPlayers, boolean shuffle){
        currentPlayer = 0;
        initializeDecks();
        if(shuffle) {
            adventureDeck.shuffle();
            eventDeck.shuffle();
        }
        initializePlayers(numberOfPlayers);
    }

    public void initializeDecks() {
        adventureDeck = new Deck();
        eventDeck = new Deck();
        adventureDeck.addCards(initializeAdventureCards());
        eventDeck.addCards(initializeEventCards());
    }

    //Getters
    public Deck getAdventureDeck() {
        return adventureDeck;
    }

    public Deck getEventDeck() {
        return eventDeck;
    }

    public int getEventDeckSize(){
        return eventDeck.size();
    }

    public int getAdventureDeckSize(){
        return adventureDeck.size();
    }

    public int getMaxHandSize(){
        return maxHandSize;
    }

    private void addAdventureCards(List<Card> deck, String type, int value, int count) {
        for (int i = 0; i < count; i++) {
            deck.add(new Card(type, value));
        }
    }

    private void addEventCards(List<Card> deck,String type, int stage, int count) {
        for (int i = 0; i < count; i++) {
            if(stage>0){
                deck.add(new Card(type, stage));
            }else{
                deck.add(new Card(type,i+1));
            }
        }
    }

    //Initializes Adventure deck with Foe cards and Adventure cards
    private List<Card> initializeAdventureCards() {
        List<Card> adventureCards = new ArrayList<>();

        //Add F (Foe) cards
        addAdventureCards(adventureCards,"F",5, 8);
        addAdventureCards(adventureCards,"F",10,7);
        addAdventureCards(adventureCards,"F",15,8);
        addAdventureCards(adventureCards,"F",20,7);
        addAdventureCards(adventureCards,"F",25,7);
        addAdventureCards(adventureCards,"F",30,4);
        addAdventureCards(adventureCards,"F",35,4);
        addAdventureCards(adventureCards,"F",40,2);
        addAdventureCards(adventureCards,"F",50,2);
        addAdventureCards(adventureCards,"F",70,1);

        //Add Weapon cards
        addAdventureCards(adventureCards, "D",5,6);   //6 daggers with value 5
        addAdventureCards(adventureCards, "H",10,12); //12 horses with value 10
        addAdventureCards(adventureCards, "S",10,16); //16 swords
        addAdventureCards(adventureCards, "A",15,8);  //8 battle-axes
        addAdventureCards(adventureCards, "L",20,6);  //6 lances
        addAdventureCards(adventureCards, "E",30,2);  //2 excaliburs

        return adventureCards;
    }

    private List<Card> initializeEventCards() {
        //Add 17 event cards (12 Q cards, 5 E cards)
        List<Card> eventCards = new ArrayList<>();

        //Add Q cards (quests)
        addEventCards(eventCards,"Q",2,3); //3 Q2 cards with 2 stages
        addEventCards(eventCards,"Q",3,4); //4 Q3 cards with 3 stages
        addEventCards(eventCards,"Q",4,3); //3 Q4 cards with 4 stages
        addEventCards(eventCards,"Q",5,2); //2 Q5 cards with 5 stages

        //Add E cards (events)
        addEventCards(eventCards, "Plague",0,1);        //Lose 2 shields
        addEventCards(eventCards, "Queen's Favor",0,2); //Player draws 2 A cards
        addEventCards(eventCards, "Prosperity",0,2);    //All players draw 2 A cards

        return eventCards;
    }

    private void initializePlayers(int numberOfPlayers) {
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("Player " + (i + 1),adventureDeck.draw(12),maxHandSize);
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getCurrentPlayer(){
        return players[currentPlayer];
    }

    public void handleEvent(Card card) {
        String cardType = card.getType();
        Player currentPlayer = getCurrentPlayer();

        switch (cardType) {
            case "Plague":
                //Player will lose 2 shields
                System.out.println(currentPlayer.getName() + " has drawn a Plague card!");
                currentPlayer.removeShields(2);
                System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getShields() + " shields.");
                break;

            case "Queen's Favor":
                //Player draws 2 A cards
                System.out.println(currentPlayer.getName() + " has drawn a Queen's Favor!");
                List<Card> drawnCards = adventureDeck.draw(2);
                currentPlayer.addCard(drawnCards.get(0));
                currentPlayer.addCard(drawnCards.get(1));
                System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getHandSize() + " cards.");
                trimPlayerHand();
                break;

            case "Prosperity":
                //All players draw 2 A cards
                System.out.println(currentPlayer.getName() + " has drawn a Prosperity card!");
                System.out.println("All players draw 2 Adventure cards!");
                for (Player player : players) {
                    List<Card> advCards = adventureDeck.draw(2);
                    player.addCard(advCards.get(0));
                    player.addCard(advCards.get(1));
                    System.out.println(player.getName() + " now has " + player.getHandSize() + " cards.");
                    trimPlayerHand();
                }
                break;

            case "Q":
            case "Quest":
                // Offer sponsorship for a quest
                System.out.println("A Quest card has been drawn! Offering sponsorship...");
                if (!isSponsorshipOffered()) {
                    System.out.println("player did not accept the sponsorship for the quest.");
                } else {
                    System.out.println("Quest sponsorship has been accepted.");
                    setStage(card);
                    if(!getCurrentStage().isEmpty()){
                        stageOwner = getCurrentPlayer();
                    }
                }
                break;

            default:
                System.out.println("Unknown event card: " + cardType);
                break;
        }
    }

    public boolean isSponsorshipOffered(){
        scanner = new Scanner(System.in);
        for(int n=0;n<players.length;n++){
            Player p = getCurrentPlayer();
            //Prompt player if they wish to sponsor
            System.out.println(p.getName() + ", Do you want to sponsor the event? Y/N");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Y")) {
                System.out.println("User input Y");
                //player wants to sponsor, return true
                return true;
            }else{
                System.out.println("User input N");
                setCurrentPlayer();//set current player to next
            }
        }
        return false; //no one sponsor
    }

    private void setCurrentPlayer(){
        currentPlayer++;
        if(currentPlayer > 3){
            currentPlayer = 0;
        }
    }

    public List<List<Card>> getGameStages(){
        return gameStages;
    }
    public List<Card> getCurrentStage(){
        if(!gameStages.isEmpty()){
            return gameStages.getLast();
        }else{
            return new ArrayList<>();
        }
    }

    public void setStage(Card card){
        scanner = new Scanner(System.in);
        List<Card> stage = new ArrayList<>();
        int choice = -1;
        players[currentPlayer].printHand();
        for(int n = 0;n<card.getValue();n++) {
            System.out.println("Enter the position of the next card to include in that stage or type 'quit' to end.");
            if(scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    if (stage.isEmpty()) {
                        System.out.println("Stage can not be empty");
                    } else {
                        return;
                    }
                } else {
                    choice = Integer.parseInt(input);
                    //check if choice is valid index
                    if (choice >= 0 && choice < players[currentPlayer].getHandSize()) {
                        System.out.println("Card at index " + choice + " selected");
                        Card c = players[currentPlayer].getCardAtIndex(choice);

                        if (stage.isEmpty() || c.getValue() > stage.getLast().getValue()) {
                            stage.add(c);
                            players[currentPlayer].removeCardAtIndex(choice);
                            System.out.println("Card " + c + " removed from player's hand");
                            players[currentPlayer].printHand();
                        } else {
                            System.out.println("Insufficient value for this stage");
                            n--;
                        }
                    }
                }
            }else{
                System.out.println("Stage can not be empty");
            }
        }
        if (stageOwner == null) {
            stageOwner = getCurrentPlayer();
        }
        gameStages.add(stage);
    }

    public void endTurn(){
        //clear screen so next player can't see previous players cards
        clearScreen();

        //set next player
        setCurrentPlayer();
    }

    public void endQuest(){
        //stage setter, must pick same number of cards
        for(int n=0;n<gameStages.getLast().size();n++){
            stageOwner.addCard(adventureDeck.draw());
        }
        stageOwner = null;
        setCurrentPlayer();

        System.out.println("\n End of quest summary:");
        for (Player player : players){
            System.out.println(player.getName() + " has " + player.getShields() + " shields.");
        }
        gameStages.add(new ArrayList<>());
    }

    private void trimPlayerHand(){
        if(players[currentPlayer].getHandSize() > maxHandSize){
            trimPlayerHand(players[currentPlayer].getHandSize() - maxHandSize);
            System.out.println(players[currentPlayer].getName() + " now has " + players[currentPlayer].getHandSize() + " cards after trim.");
        }
    }
    public void trimPlayerHand(int numberOfCards){
        List<Integer> toTrim = players[currentPlayer].trimHand(numberOfCards);
        for(int n : toTrim){
            System.out.println("Trim: "+players[currentPlayer].removeCardAtIndex(n));
        }
    }

    public void addCardsToCurrentPlayerHand(List<Card> drawnCards){
        for(int i = 0;i<drawnCards.size();i++) {
            players[currentPlayer].addCard(drawnCards.get(i));
        }
    }

    public void playAttack() {
        // play current player attack
        scanner = new Scanner(System.in);
        List<Card> attackCards = new ArrayList<>();
        while (true) {
            players[currentPlayer].printHand();  // Display the player's hand
            System.out.println("Enter the index of the card to include in the attack or type 'quit' to end:");
            String input = scanner.nextLine();

            if(input.equalsIgnoreCase("quit")){
                break;  // End the attack setup
            }

            int choice = Integer.parseInt(input);
            if(choice >= 0 && choice < players[currentPlayer].getHandSize()){
                Card selectedCard = players[currentPlayer].getCardAtIndex(choice);
                // Ensure the selected card is valid (non-repeated weapon card)
                if(attackCards.contains(selectedCard)){
                    System.out.println("This card has already been used. Please choose another card.");
                }else{
                    attackCards.add(selectedCard); // Add the card to the attack
                    addUsedAdventureCard(selectedCard);//add it ity to the used / floor cards
                    players[currentPlayer].removeCardAtIndex(choice);  // Remove the card from the player's hand
                    System.out.println("Added card to attack: " + selectedCard);
                }
            }else{
                System.out.println("Invalid index. Please try again.");
            }

            List<Card> stage = getCurrentStage();
            if(attackCards.size() >= stage.size()){
                players[currentPlayer].addShields(stage.size());
                break;
            }
        }
        //After quitting, print the final attack
        System.out.println("Final attack setup: " + attackCards);
    }

    public List<Card> getUsedAdventureCards(){
        return usedAdventureCards;
    }
    public List<Card> getUsedEventCards(){
        return usedEventCards;
    }
    public void addUsedAdventureCard(Card card){
        usedAdventureCards.add(card);
    }
    public void clearUsedAdventureCards(){
        usedAdventureCards.clear();
    }
    public void clearUsedEventCards(){
        usedEventCards.clear();
    }

    //draw card based on type and value
    public Card drawEventCard(String type, int value){
        Card card = eventDeck.draw(type,value);
        if(card != null){
            //add the removed card to the used list
            usedEventCards.add(card);
        }
        return card;
    }

    public boolean checkForWinner() {
        for (Player player : players) {
            if (player.getShields() >= 7) {
                return true;
            }
        }
        return false;
    }

    public void playGame() {
        scanner = new Scanner(System.in);
        while (!checkForWinner()) {
            for (int n=0;n<players.length;n++) {
                Player player = getCurrentPlayer();
                String name = player.getName();



                System.out.println("\nIt's " + name + "'s turn.");

                List<Card> currentStage = getCurrentStage();
                if(currentStage.isEmpty()) {
                    player.printHand();
                    System.out.println(name + " Press Enter to draw an event card...");
                    String input = scanner.nextLine();

                    Card eventCard = getEventDeck().draw();

                    System.out.println(name + " drew: " + eventCard);

                    handleEvent(eventCard);
                }else{
                    if(stageOwner != null && !stageOwner.equals(player)){
                        player.addCard(adventureDeck.draw());
                        trimPlayerHand();
                        playAttack();
                    }

                    if (checkForWinner()) {
                        System.out.println("\nCongratulations! " + name + " has won the game!");
                        endQuest();
                        cleanup();
                        return;
                    }else{
                        clearScreen();
                    }
                }
                endTurn();
            }
            endQuest();
        }

    }

    private static void clearScreen() {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.flush();
    }
    private void cleanup(){
        try{
            clearUsedAdventureCards();
            clearUsedEventCards();
            adventureDeck.clear();
            eventDeck.clear();
            for(Player p : players){
                p.clearHand();
            }
        }catch(Exception _){
        }

    }
}


