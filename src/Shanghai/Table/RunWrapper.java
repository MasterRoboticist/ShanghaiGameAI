package Shanghai.Table;

import Deck.DeckWrapper;
import Shanghai.*;

import java.util.ArrayList;

public class RunWrapper extends DeckWrapper {
    Run run;

    public RunWrapper(Run run){
        super(run);
        this.run = run;
    }

    public boolean isPlayable(ShanghaiCard card){
        return run.isPlayable(card);
    }
    public String getSuit() {return run.getSuit();}
    public ShanghaiCard getFirstCard() {return run.getFirstCard();}
    public ShanghaiCard getLastCard() {return run.getLastCard();}
    public ArrayList<ShanghaiCard> getJokerCards() {return run.getJokerCards();}
    public boolean isCompleteRun(){return run.isCompleteRun(); }
}
