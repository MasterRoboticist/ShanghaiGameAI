package Shanghai.Table;

import Deck.*;
import Shanghai.*;

import java.util.ArrayList;

public class Hand extends Deck<ShanghaiCard> {
    HandWrapper handwrapper;

    public Hand(){
        handwrapper = new HandWrapper(this);
    }

    public Hand(Hand hand){
        super(hand);
        handwrapper = new HandWrapper(this);
    }

    public Hand(HandWrapper hand){
        super(hand);
        handwrapper = new HandWrapper(this);
    }

    /**
     * Gets the size of the player's hand
     */
    public void size(){
        getNumCards();
    }

    /**
     * @return the player's current hand
     */
    public HandWrapper getHand() {
        return handwrapper;
    }

    /**
     * Removes all cards from the player's hand
     */
    public void clear(){
        empty();
    }

//    /**
//     * Called when the player is dealt a card
//     * @param card The card the player was dealt
//     */
//    void addCard(ShanghaiCard card){
//        addCard(card);
//    }
//
//    /**
//     * Called when the player is dealt multiple cards
//     * @param cards the cards the player was dealt
//     */
//    void addCards(ArrayList<ShanghaiCard> cards){
//        for(ShanghaiCard c: cards){
//            addCard(c);
//        }
//    }

    /**
     * Called to play a card from a hand. Removes the card from the hand if it exists
     * @param card the card to be played from the hand
     * @return true if the card was in the hand, false otherwise
     */
    public boolean playCard(ShanghaiCard card){
        for(ShanghaiCard c: deck){
            if(c.equals(card)) {
                deck.remove(c);
                return true;
            }
        }
        return false;
    }

    /**
     * @return the number of points the current hand is worth
     */
    public int getHandCost(){
        int cost = 0;
        for(var c: deck){
            cost += c.getPoints();
        }
        return cost;
    }
}
