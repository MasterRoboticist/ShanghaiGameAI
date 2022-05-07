package Deck;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class DeckTest {

    @Test
    public void emptyDeck(){
        var d = new Deck<Card>();
        Assert.assertTrue(d.deck.size() == 0);
    }

    @Test
    public void cardDeck(){
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card("test", 0));
        var d = new Deck<Card>(cards);
        Assert.assertSame(d.getDeck(), cards);
    }

    @Test
    public void shuffleDeck(){
        var shufflePercentage = 0.9;
        var deck1 = new Deck<StandardCard>(DeckUtil.getStandardDeck(2, DeckUtilTest.scf));
        var deck2 = new Deck<StandardCard>(DeckUtil.getStandardDeck(2, DeckUtilTest.scf));

        deck1.shuffle();
        double differentCards = 0;
        for (int i = 0; i < deck1.getNumCards(); i++) {
            if (!(deck1.peek(i).equals(deck2.peek(i)))) { differentCards++; }
        }
        Assert.assertTrue(differentCards/deck1.getNumCards() > shufflePercentage);
    }

    @Test
    public void dealCardDeck(){
        var first = new Card("first", 0);
        var second = new Card("second", 1);
        var third = new Card("third", 2);
        var fourth = new Card("fourth", 3);
        var fifth = new Card("fifth", 3);
        var cards = new ArrayList<Card>();
        cards.add(first);
        cards.add(second);
        cards.add(third);
        cards.add(fourth);
        cards.add(fifth);

        var deck = new Deck<Card>(cards);
        var decklen = deck.getNumCards();

        var ct = deck.deal();
        Assert.assertTrue(ct.equals(first));
        Assert.assertEquals(decklen - 1, deck.getNumCards());

        decklen = deck.getNumCards();
        var cb = deck.dealBottom();
        Assert.assertTrue(cb.equals(fifth));
        Assert.assertEquals(decklen - 1, deck.getNumCards());

        decklen = deck.getNumCards();
        var c2 = deck.deal(2);
        Assert.assertTrue(c2.equals(fourth));
        Assert.assertEquals(decklen - 1, deck.getNumCards());
    }

    @Test
    public void findInDeck() {
        var findme = new Card("findme", 0);
        var dontfindme = new Card("dontfindme", 1);
        var imnotinthedeck = new Card("notindeck", 2);
        var cards = new ArrayList<Card>();
        cards.add(dontfindme);
        cards.add(dontfindme);
        cards.add(findme);
        cards.add(dontfindme);
        cards.add(dontfindme);

        var deck = new Deck<Card>(cards);

        Assert.assertTrue(deck.find(findme) == 2);
        Assert.assertTrue(deck.find(imnotinthedeck) == -1);
        Assert.assertTrue(deck.find(findme.getStringValue()) == 2);
        Assert.assertTrue(deck.find(imnotinthedeck.getStringValue()) == -1);
    }

    @Test
    public void peekInDeck() {
        var findme = new Card("findme", 0);
        var dontfindme = new Card("dontfindme", 1);
        var imnotinthedeck = new Card("notindeck", 2);
        var cards = new ArrayList<Card>();
        cards.add(dontfindme);
        cards.add(dontfindme);
        cards.add(findme);

        var deck = new Deck<Card>(cards);

        Assert.assertTrue(deck.peek(2).equals(findme));
    }

    @Test
    public void replaceInDeck() {
        var replaceme = new Card("replaceme", 0);
        var replacement = new Card("replacement", 1);
        var cards = new ArrayList<Card>();
        cards.add(replaceme);

        var deck = new Deck<Card>(cards);

        var replacedcard = deck.replace(0, replacement);

        Assert.assertTrue(replacedcard.equals(replaceme));
        Assert.assertTrue(replacement.equals(deck.peek(0)));
    }

    @Test
    public void addCardsDeck() {
        var first = new Card("first", 0);
        var second = new Card("second", 1);
        var third = new Card("third", 2);
        var fourth = new Card("fourth", 3);
        var fifth = new Card("fifth", 4);
        var sixth = new Card("sixth", 5);
        var bottom = new Card("bottom", 6);
        var cards1 = new ArrayList<Card>();
        var cards2 = new ArrayList<Card>();
        var cards3 = new ArrayList<Card>();
        cards1.add(first);
        cards2.add(third);
        cards2.add(fourth);
        cards3.add(fifth);
        cards3.add(sixth);


        var deck1 = new Deck<Card>(cards1);
        var decklen = deck1.getNumCards();
        deck1.addCard(second);
        Assert.assertEquals(decklen + 1, deck1.getNumCards());
        Assert.assertTrue(deck1.find(second) == 1);

        decklen = deck1.getNumCards();
        deck1.addCards(cards2, 0);
        Assert.assertEquals(decklen + cards2.size(), deck1.getNumCards());
        Assert.assertTrue(deck1.peek(0).equals(third));
        Assert.assertTrue(deck1.find(fourth) == 1);

        decklen = deck1.getNumCards();
        var deck2 = new Deck(cards3);
        deck1.addDeck(deck2, 0);
        Assert.assertEquals(decklen + cards3.size(), deck1.getNumCards());
        Assert.assertTrue(deck1.peek(0).equals(fifth));
        Assert.assertTrue(deck1.find(sixth) == 1);

        decklen = deck1.getNumCards();
        deck1.addCard(bottom);
        Assert.assertEquals(decklen + 1, deck1.getNumCards());
        Assert.assertTrue(deck1.peek(deck1.getNumCards() - 1).equals(bottom));
    }

    @Test
    public void clearDeck(){
        var deck = new Deck<StandardCard>(DeckUtil.getStandardDeck(2, DeckUtilTest.scf));

        deck.empty();
        Assert.assertEquals(0, deck.getNumCards());
        Assert.assertEquals(0, deck.getDeck().size());
        Assert.assertNull(deck.deal());
    }

    @Test
    public void getDeckValues() {
        var card1 = new Card("card1", 0);
        var card2 = new Card("card2", 1);
        var cards = new ArrayList<Card>();
        cards.add(card1);
        cards.add(card2);

        var deck = new Deck<Card>(cards);

        var vals = deck.getDeckValues();

        Assert.assertEquals("card1", vals[0]);
        Assert.assertEquals("card2", vals[1]);
    }


}
