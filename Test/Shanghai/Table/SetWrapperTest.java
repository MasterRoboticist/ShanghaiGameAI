package Shanghai.Table;

import Deck.StandardCard;
import Shanghai.ShanghaiCard;
import org.junit.Assert;
import org.junit.Test;

public class SetWrapperTest {
    @Test
    public void setWrapperTest(){
        var denom = StandardCard.ACE;
        Set set = new Set(denom);
        SetWrapper setW = new SetWrapper(set);

        Assert.assertEquals(set.getDenomination(), setW.getDenomination());
        Assert.assertTrue(setW.isPlayable(new ShanghaiCard(StandardCard.CLUBS, denom)));
        Assert.assertFalse(setW.isPlayable(new ShanghaiCard(StandardCard.DIAMONDS, StandardCard.FIVE)));
    }
}
