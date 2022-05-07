package Deck;

import org.junit.Assert;
import org.junit.Test;
import Deck.DeckUtil;

import java.util.ArrayList;

public class DeckUtilTest {
    int cardsInDeck = 52;

    static DeckUtil.CardFactory<StandardCard> scf = (String suit, int denom) -> new StandardCard(suit, denom);

    @Test
    public void standardDeckNoJokers(){
        int jokers = 0;
        var cards = DeckUtil.getStandardDeck(jokers, scf);
        standardDeckHelper(cards, 1, jokers);
    }

    @Test
    public void standardDeck(){
        int jokers = 2;
        var cards = DeckUtil.getStandardDeck(jokers, scf);
        standardDeckHelper(cards, 1, jokers);
    }

    @Test
    public void multipleStandardDecks(){
        int jokers = 3;
        int decks = 2;
        var cards = DeckUtil.getStandardDeck(decks, jokers, scf);
        standardDeckHelper(cards, decks, jokers);
    }

    private void standardDeckHelper(ArrayList<StandardCard> cards, int numDecks, int jokers){
        int[] counts = new int[DeckUtil.NONJOKERCARDSINSTANDARDDECK];
        int actualJokers = 0;
        for (var c: cards) {
            if(c.isJoker()){
                actualJokers ++;
            }
            else
                counts[((c.getDenomination()- StandardCard.LOWESTDENOM) * (StandardCard.SUITS.length - 1)) +
                        (StandardCard.getNumericalSuit(c.getSuit()) - StandardCard.LOWESTSUIT)]++;
        }
        for (int count: counts)
            Assert.assertEquals(count, numDecks);
        Assert.assertEquals(actualJokers, jokers);
    }
}
