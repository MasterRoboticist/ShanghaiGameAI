package Shanghai.Table;

import Deck.StandardCard;
import org.junit.Assert;
import org.junit.Test;
import Shanghai.CheaterCheaterPumpkinEaterException;

public class RunTest {
    @Test
    public void makeEmptyRun(){
        var first = StandardCard.ACE;
        var suit = StandardCard.CLUBS;
        Run r = new Run(suit, first);

        Assert.assertEquals(suit, r.getSuit());

        try{
            r = new Run("No such suit", first);
            Assert.fail();
        } catch(CheaterCheaterPumpkinEaterException e) {}

        try{
            r = new Run(suit, StandardCard.LOWESTDENOM - 10);
            Assert.fail();
        } catch(CheaterCheaterPumpkinEaterException e) {}

        try{
            r = new Run(StandardCard.JOKERSUIT, first);
            Assert.fail();
        } catch(CheaterCheaterPumpkinEaterException e) {}

        try{
            r = new Run(suit, StandardCard.JOKERRED);
            Assert.fail();
        } catch(CheaterCheaterPumpkinEaterException e) {}

        try{
            r = new Run(suit, StandardCard.JOKERBLACK);
            Assert.fail();
        } catch(CheaterCheaterPumpkinEaterException e) {}
    }
}
