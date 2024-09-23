package game.quests;

import java.util.*;
import java.util.Scanner;


public class Game {
    private Deck adventureDeck;
    private Deck eventDeck;
    private Player[] players;
    private int currentPlayer;
    private final int maxHandSize = 12;

    public Game(int numberOfPlayers){
        initializeDecks();
        initializePlayers(numberOfPlayers);
        currentPlayer = 0;
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
        addAdventureCards(adventureCards, "B",15,8);  //8 battle-axes
        addAdventureCards(adventureCards, "L",20,6);  //6 lances
        addAdventureCards(adventureCards, "E",30,2);  //2 excaliburs

        return adventureCards;
    }

    private List<Card> initializeEventCards() {
        //Add 17 event cards (12 Q cards, 5 E cards)
        List<Card> eventCards = new ArrayList<>();

        //Add Q cards (quests)
        addEventCards(eventCards,"Q", 2, 3);  //3 Q2 cards with 2 stages
        addEventCards(eventCards,"Q",3, 4);  //4 Q3 cards with 3 stages
        addEventCards(eventCards,"Q",4, 3);  //3 Q4 cards with 4 stages
        addEventCards(eventCards,"Q",5, 2);  //2 Q5 cards with 5 stages

        //Add E cards (events)
        addEventCards(eventCards, "Plague",0,1);        //Lose 2 shields
        addEventCards(eventCards, "Queen's Favor",0,2); //Player draws 2 A cards
        addEventCards(eventCards, "Prosperity",0,2);    //All players draw 2 A cards

        return eventCards;
    }

    public void initializePlayers(int numberOfPlayers) {
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

    public void handleEvent(Card card){

    }

    public boolean isSponsorshipOffered(){
        Player current = getCurrentPlayer();
        //Scanner scanner = new Scanner(System.in);  // Create a scanner object to read input
        for(Player p : players){
            if(!p.equals(current)) {
                //Prompt player if they wish to sponsor
                System.out.println(p.getName() + ", Do you want to sponsor the event? Y/N");
                String input = System.console().readLine();
                //System.out.println(opt);
                //String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Y")) {
                    //player wishes to sponsor, return true
                    return true;
                }
            }
        }
        return false; //no one sponsor
    }

}


