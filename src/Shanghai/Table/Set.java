package Shanghai.Table;

import Deck.*;
import Shanghai.*;

public class Set extends Deck<ShanghaiCard> implements Iterable<ShanghaiCard>{
    //GLOBALS
    private int denomination;

    public Set(int denomination){
        this.denomination = denomination;
    }

    public Set(Set set){
        denomination = set.getDenomination();
        for(var c: deck){
            addCard(c);
        }
    }

    public Set(SetWrapper set){
        denomination = set.getDenomination();
        for(var c: deck){
            addCard(c);
        }
    }

    /**
     * @return the denomination of the set
     */
    public int getDenomination() {
        return denomination;
    }

    /**
     * Adds a card to the downable thing. Throws an error if the card cannot be added
     * @param card the card to be added
     * @param place this input is not used
     * @return true if the operation succeeded. Errors instead of returning false
     */
    public boolean addCard(ShanghaiCard card, int place) {
        if(isPlayable(card)) {
            deck.add(card);
            return true;
        }
        else throw new CheaterCheaterPumpkinEaterException(
                "Card " + card.getStringValue() + " cannot be added to set of " + denomination);
    }

    /**
     * Checks if a card is playable on a given downable thing
     * @param card the card to be checked
     * @return true if the card could be played, false otherwise
     */
    public boolean isPlayable(ShanghaiCard card) {
        return card.getDenomination() == denomination;
    }

    public boolean isValidSet(){
        if (deck.size() < 3) return false;

        for(StandardCard card: deck){
            if(card.getDenomination() != denomination && !card.isJoker())
                return false;
        }
        return true;
    }
}
