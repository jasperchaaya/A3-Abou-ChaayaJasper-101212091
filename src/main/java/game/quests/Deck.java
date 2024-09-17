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

    // Returns the current list of cards in the deck
    public List<Card> getCards() {
        return cards;
    }
}

