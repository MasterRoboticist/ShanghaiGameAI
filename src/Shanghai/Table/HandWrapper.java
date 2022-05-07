package Shanghai.Table;

import Deck.DeckWrapper;
import Shanghai.ShanghaiCard;

public class HandWrapper extends DeckWrapper<ShanghaiCard> {
    private Hand hand;

    public HandWrapper(Hand h){
        super(h);
        hand = h;
    }

    public int getHandCost(){
        return hand.getHandCost();
    }
}
