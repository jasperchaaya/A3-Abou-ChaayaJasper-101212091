package game.quests;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public Player(String playerName,List<Card> cards) {
        this.name = playerName;
        hand.addAll(cards);
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


}
