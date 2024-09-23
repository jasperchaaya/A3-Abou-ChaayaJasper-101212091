package game.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private final int maxHandSize;
    private int numShields;

    public Player(String playerName,List<Card> cards, int maxHandSize) {
        this.name = playerName;
        this.maxHandSize = maxHandSize;
        this.numShields = 0;
        //if the list size is less or equal max add all, otherwise add only first 12 cards
        if(cards.size() <= maxHandSize){
            hand.addAll(cards);
        }else{
            for(int i = 0;i<maxHandSize;i++) {
                hand.add(cards.get(i));
            }
        }

    }

    public void printHand() {
        System.out.println("Player: " + name );
        System.out.println("-----------");
        for (Card card : hand) {
            System.out.println(card);
        }
    }

    public int getHandSize() {
        return hand.size();
    }

    public int getShields(){
        return this.numShields;
    }
    public void addShields(int count){
        this.numShields += count;
    }
    public void removeShields(int count){
        this.numShields -= count;
        if(this.numShields < 0){
            this.numShields = 0;
        }
    }

    public void addCard(Card card){
        if(hand.size() < maxHandSize){
            hand.add(card);
        }
    }

    public Card drawCard(Card expected){
        List<Card> filtered = hand.stream()
                .filter(p -> p.getType().equals(expected.getType()))
                .sorted((p1, p2) -> Integer.compare(p1.getValue(), p2.getValue()))
                .collect(Collectors.toList());

        //check the list
        if (!filtered.isEmpty()) {
            for(Card c : filtered){
                if(c.getValue() > expected.getValue() ){
                    return c; //return first higher card
                }
            }
            return filtered.getFirst();
        }
        return null;
    }
    public String getName(){
        return this.name;
    }
}
