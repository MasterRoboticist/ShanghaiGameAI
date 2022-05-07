package Deck;

import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;

public class StandardCardTest {

    @Test
    public void createStandardCard(){
        var suit = StandardCard.CLUBS;
        var denomination = StandardCard.ACE;
        StandardCard c = new StandardCard(suit, denomination);

        suit = StandardCard.CLUBS;
        denomination = StandardCard.KING;
        c = new StandardCard(suit, denomination);

        suit = StandardCard.DIAMONDS;
        denomination = StandardCard.THREE;
        c = new StandardCard(suit, denomination);

        suit = StandardCard.SPADES;
        denomination = StandardCard.FIVE;
        c = new StandardCard(suit, denomination);

        suit = StandardCard.HEARTS;
        denomination = StandardCard.SEVEN;
        c = new StandardCard(suit, denomination);

        suit = StandardCard.JOKERSUIT;
        denomination = StandardCard.JOKERRED;
        c = new StandardCard(suit, denomination);

        suit = StandardCard.JOKERSUIT;
        denomination = StandardCard.JOKERBLACK;
        c = new StandardCard(suit, denomination);
    }

    @Test(expected = InvalidParameterException.class)
    public void createBadSuitStandardCard(){
        var suit = "None";
        var denomination = StandardCard.ACE;
        StandardCard c = new StandardCard(suit, denomination);
    }

    @Test(expected = InvalidParameterException.class)
    public void createBadDenomStandardCard(){
        var suit = StandardCard.CLUBS;
        var denomination = StandardCard.KING + 10;
        StandardCard c = new StandardCard(suit, denomination);
    }

    @Test(expected = InvalidParameterException.class)
    public void createBadJokerDenomStandardCard(){
        var suit = StandardCard.JOKERSUIT;
        var denomination = StandardCard.KING;
        StandardCard c = new StandardCard(suit, denomination);
    }

    @Test(expected = InvalidParameterException.class)
    public void createBadJokerSuitStandardCard(){
        var suit = StandardCard.CLUBS;
        var denomination = StandardCard.JOKERRED;
        StandardCard c = new StandardCard(suit, denomination);
    }

    @Test
    public void getStandardCard(){
        var suit = StandardCard.CLUBS;
        var denomination = StandardCard.ACE;
        StandardCard c = new StandardCard(suit, denomination);

        Assert.assertEquals(c.getSuit(), suit);
        Assert.assertEquals(c.getDenomination(), denomination);
    }

    @Test
    public void equalsStandardCard(){
        var suit = StandardCard.CLUBS;
        var denomination = StandardCard.ACE;
        StandardCard c1 = new StandardCard(suit, denomination);
        StandardCard c2 = new StandardCard(suit, denomination);
        StandardCard c3 = new StandardCard(suit, StandardCard.KING);

        Assert.assertTrue(c1.equals(c2));
        Assert.assertFalse(c3.equals(c2));

    }

    @Test
    public void standardDenomToString(){
        Assert.assertEquals(StandardCard.denomToString(StandardCard.ACE), "A");
        Assert.assertEquals(StandardCard.denomToString(StandardCard.KING), "K");
        Assert.assertEquals(StandardCard.denomToString(StandardCard.QUEEN), "Q");
        Assert.assertEquals(StandardCard.denomToString(StandardCard.JACK), "J");
        Assert.assertEquals(StandardCard.denomToString(StandardCard.FIVE), "5");
    }

    @Test(expected = InvalidParameterException.class)
    public void invalidDenomToString(){
        StandardCard.denomToString(StandardCard.KING + 10);
    }

    @Test
    public void validStandardDenom(){
        Assert.assertTrue(StandardCard.isValidDenom(StandardCard.ACE));
        Assert.assertTrue(StandardCard.isValidDenom(StandardCard.KING));
    }

    @Test
    public void notValidStandardDenom(){
        Assert.assertFalse(StandardCard.isValidDenom(StandardCard.KING + 10));
        Assert.assertFalse(StandardCard.isValidDenom(StandardCard.TWO - 10));
    }

    @Test
    public void getNumSuit(){
        Assert.assertTrue(StandardCard.getNumericalSuit(StandardCard.SUITS[0]) < StandardCard.SUITS.length);
        Assert.assertTrue(StandardCard.getNumericalSuit(StandardCard.SUITS[0]) >= 0);
        Assert.assertTrue(StandardCard.getNumericalSuit(StandardCard.SUITS[StandardCard.SUITS.length-1]) < StandardCard.SUITS.length);
        Assert.assertTrue(StandardCard.getNumericalSuit(StandardCard.SUITS[StandardCard.SUITS.length-1]) >= 0);
    }

    @Test(expected = InvalidParameterException.class)
    public void invalidStringSuit(){
        StandardCard.getNumericalSuit("NotASuit");
    }

    @Test
    public void validSuit(){
        Assert.assertTrue(StandardCard.isValidSuit(StandardCard.CLUBS));
        Assert.assertFalse(StandardCard.isValidSuit("NotASuit"));
    }

    @Test(expected = InvalidParameterException.class)
    public void invalidNumSuit(){
        StandardCard.getStringSuit(StandardCard.SUITS.length);
    }

    @Test
    public void isJoker(){
        var suit = StandardCard.CLUBS;
        var denomination = StandardCard.ACE;
        StandardCard joker = new StandardCard(StandardCard.JOKERSUIT, StandardCard.JOKERRED);
        StandardCard notJoker = new StandardCard(StandardCard.CLUBS, StandardCard.ACE);

        Assert.assertTrue(joker.isJoker());
        Assert.assertFalse(notJoker.isJoker());
    }


}
