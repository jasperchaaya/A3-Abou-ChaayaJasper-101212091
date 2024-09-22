package game.quests;
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
        if (!cards.isEmpty()) {
            return cards.removeFirst();
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


}

