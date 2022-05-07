package Deck;

import org.junit.Assert;
import org.junit.Test;

public class CardTest {
    @Test
    public void makeCard(){
        String cardstr = "test";
        int cardint = 0;
        Card c = new Card(cardstr, cardint);
        Assert.assertEquals(cardstr, c.getStringValue());
        Assert.assertEquals(cardint, c.getNumericalValue());
    }

    @Test
    public void equalCard(){
        String cardstr = "Test";
        int cardint = 0;
        Card c1 = new Card(cardstr, cardint);
        Card c2 = new Card(cardstr, cardint);
        Card c3 = new Card(cardstr, cardint - 1);

        Assert.assertTrue(c1.equals(c2));
        Assert.assertFalse(c3.equals(c2));
    }
}
