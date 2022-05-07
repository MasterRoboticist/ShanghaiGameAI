package Shanghai.Table;

import Deck.DeckWrapper;
import Shanghai.*;

public class SetWrapper extends DeckWrapper {
    Set set;

    public SetWrapper(Set set){
        super(set);
        this.set = set;
    }

    public int getDenomination(){
        return set.getDenomination();
    }

    public boolean isPlayable(ShanghaiCard card){
        return set.isPlayable(card);
    }
}
