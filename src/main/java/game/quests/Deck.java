package game.quests;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
            return this.cards.removeFirst();
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
                if(c.getType().equals(type) && c.getValue() == value){
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

}

