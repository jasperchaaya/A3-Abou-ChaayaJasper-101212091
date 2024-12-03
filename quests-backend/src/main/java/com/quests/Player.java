package com.quests;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final String name;
    private List<Card> hand = new ArrayList<>();
    private int numShields;
    private Boolean questWinner;

    public Player(String playerName,List<Card> cards, int maxHandSize) {
        this.name = playerName;
        this.numShields = 0;
        //if the list size is less or equal max add all, otherwise add only first 12 cards
        if(cards.size() <= maxHandSize){
            hand.addAll(cards);
        }else{
            for(int i = 0;i<maxHandSize;i++) {
                hand.add(cards.get(i));
            }
        }
        sortHand();
        this.questWinner = false;
    }

    public String getName(){
        return this.name;
    }

    public void printHand() {
        System.out.println(name );
        System.out.println("-----------");
        for (Card card : hand) {
            System.out.println(card);
        }
    }

    public List<Card> getHand(){
        return hand;
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
        //if(hand.size() < maxHandSize){
        hand.add(card);
        sortHand();
    }

    public Card getCardAtIndex(int index){
        if(!hand.isEmpty() && index < hand.size()){
            return hand.get(index);
        }
        return null;
    }

    public Card removeCardAtIndex(int index){
        if(!hand.isEmpty() && index < hand.size()){
            return hand.remove(index);
        }
        return null;
    }

    public void sortHand(){
        hand = hand.stream().sorted((p1, p2) -> Integer.compare(p1.getValue(), p2.getValue())).collect(Collectors.toList());
    }

    public Card playCard(Card expected){
        List<Card> filtered = hand.stream()
                .filter(p -> p.getType().equals(expected.getType()))
                .sorted((p1, p2) -> Integer.compare(p1.getValue(), p2.getValue()))
                .toList();

        //check the list
        if (!filtered.isEmpty()) {
            for(Card c : filtered){
                if(c.getValue() > expected.getValue() ){
                    return c; //return first higher card
                }
            }
            return filtered.get(0);//getFirst();
        }
        return null;
    }

    //this function return the indexes of the cards player wish to give up
    public List<Integer> trimHand(int numberOfCards){
        List<Integer> trimmed = new ArrayList<>();
        //sort the list so smaller cards first
        hand = hand.stream().sorted((p1, p2) -> Integer.compare(p1.getValue(), p2.getValue())).collect(Collectors.toList());
        //pick element from top
        for(int n=0;n<numberOfCards;n++){
            trimmed.add(n);
        }
        return trimmed;
    }

    public void clearHand(){
        hand.clear();
    }

    public int calculateTotalAttackValue() {
        return hand.stream()
                .filter(card -> card.getType().equals("F") || card.getType().equals("W"))
                .mapToInt(Card::getValue)
                .sum();
    }
    public boolean getQuestWinner(){
        return this.questWinner;
    }
    public void setQuestWinner(boolean questWinner){
        this.questWinner = questWinner;
    }




//    public boolean hasCard(Card card) {
//        return hand.stream().anyMatch(c -> c.getType().equals(card.getType()) && c.getValue() == card.getValue());
//    }
//
//    public Card playACard(Card card){
//        for(Card c : hand){
//            if(c.getType().equals(card.getType()) && c.getValue() == card.getValue()){
//                return c;
//            }
//        }
//        return null;
//    }

}
