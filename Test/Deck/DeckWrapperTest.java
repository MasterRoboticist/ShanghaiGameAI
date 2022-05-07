package Deck;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

public class DeckWrapperTest {
    Deck<StandardCard> deck = new Deck<StandardCard>(DeckUtil.getStandardDeck(2, DeckUtilTest.scf));

    @Test
    public void getNumCards(){
        var deckWrap = new DeckWrapper<StandardCard>(deck);

        Assert.assertEquals(deck.getNumCards(), deckWrap.getNumCards());
    }

    @Test
    public void getCards(){
        var deckWrap = new DeckWrapper<StandardCard>(deck);
        var r = new Random();
        int randplace = (int)(r.nextDouble()*deck.getNumCards());

        Assert.assertTrue(deck.peek(randplace).equals(deckWrap.getCard(randplace)));
    }
}
