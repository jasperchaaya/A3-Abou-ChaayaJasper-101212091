package com.quests;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    //Adds list of cards to the deck
    public void addCards(List<Card> listOfCards) {
        cards.addAll(listOfCards);
    }

    //add single card
    public void addCard(Card card) {
        cards.add(card);
    }


    //Returns the number of cards in the deck
    public int size() {
        return cards.size();
    }

    //Shuffles the deck
    public void shuffle() {
        Collections.shuffle(cards);
    }

    //Returns the current list of cards in the deck
    public List<Card> getCards() {
        return cards;
    }

    //Draw 1 card
    public Card draw() {
        if (!this.cards.isEmpty()) {
            return this.cards.remove(0);//.removeFirst();
        }
        //Deck is empty
        return null;
    }

    //Draw multiple cards
    public List<Card> draw(int numberOfCards) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards && !cards.isEmpty(); i++) {
            drawnCards.add(draw());
        }
        return drawnCards;
    }

    // draw card based on type and value
    public Card draw(String type, int value){
        if(!this.cards.isEmpty()){
            for(int n=0;n<this.cards.size();n++){
                Card c = this.cards.get(n);
                if(value > 0) {
                    if (c.getType().equals(type) && c.getValue() == value) {
                        return this.cards.remove(n);
                    }
                }else if (c.getType().equals(type)){
                    return this.cards.remove(n);
                }
            }
        }
        return null;
    }

    public void print(){
        for(Card card : cards){
            System.out.println(card.toString());
        }
    }

    public void clear(){
        cards.clear();
    }

}

