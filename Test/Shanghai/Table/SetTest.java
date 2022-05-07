package Shanghai.Table;

import Deck.StandardCard;
import Shanghai.*;
import org.junit.Assert;
import org.junit.Test;

public class SetTest {
    @Test
    public void makeSet(){
        var denomination = StandardCard.ACE;
        Set set = new Set(denomination);
        Assert.assertEquals(set.getDenomination(), denomination);
    }

    @Test
    public void addToSetLegal(){
        var denomination = StandardCard.ACE;
        Set set = new Set(denomination);
        var goodCard = new ShanghaiCard(StandardCard.CLUBS, denomination);
        var badCard = new ShanghaiCard(StandardCard.CLUBS, StandardCard.KING);

        Assert.assertTrue(set.isPlayable(goodCard));
        set.addCard(goodCard);
        Assert.assertEquals(1, set.getNumCards());

        Assert.assertFalse(set.isPlayable(badCard));
    }

    @Test(expected = CheaterCheaterPumpkinEaterException.class)
    public void addToSetIllegal(){
        var denomination = StandardCard.ACE;
        Set set = new Set(denomination);
        var badCard = new ShanghaiCard(StandardCard.CLUBS, StandardCard.KING);

        Assert.assertFalse(set.isPlayable(badCard));
        set.addCard(badCard);
    }

    @Test
    public void setValidity(){
        var denomination = StandardCard.ACE;
        Set set = new Set(denomination);
        var goodCard = new ShanghaiCard(StandardCard.CLUBS, denomination);

        Assert.assertFalse(set.isValidSet());
        set.addCard(goodCard);
        Assert.assertFalse(set.isValidSet());
        set.addCard(goodCard);
        Assert.assertFalse(set.isValidSet());
        set.addCard(goodCard);
        Assert.assertTrue(set.isValidSet());
        set.addCard(goodCard);
        Assert.assertTrue(set.isValidSet());
    }
}
