package Deck;

import java.util.ArrayList;
import java.util.Arrays;

public class DeckUtil {
    //////// Constants ////////////
    static final public int CARDSINSTANDARDDECK = 54;
    static final public int NONJOKERCARDSINSTANDARDDECK = 52;
    static final public int CARDSINSTANDARDSUIT = 13;

    /**
     * Creates a standard 52 card playing deck with or without the addition of two Jokers
     * @param jokers The number of jokers in the deck (half black, half red)
     */
    public static <T extends StandardCard> ArrayList<T> getStandardDeck(int jokers, CardFactory<T> cardMaker){
        return getStandardDeck(1, jokers, cardMaker);
    }

    /**
     * Creates a deck containing numdecks number of standard playing decks with or without two Jokers
     * per deck
     * @param numdecks The number of standard decks in the deck; The number of copies of any card in a standard deck
     * @param jokers The number of jokers in the deck (half black, half red)
     */
    public static <T extends StandardCard> ArrayList<T> getStandardDeck(int numdecks, int jokers, CardFactory<T> cardMaker){
        return getStandardDeck(customFromStandardDeck(numdecks, jokers), cardMaker);
    }

    /**
     * Creates a custom playing deck from a standard deck of cards, where the number of each of the standard cards
     * is customizable. customDeck is an array of 54 cards corresponding to A-K of clubs, A-K of diamonds, A-K of Spades,
     * A-K of Hearts, Red Joker, and Black Joker in that order.
     * @param customStandardDeck A 54 card array of ints corresponding to the number of each standard playing card in the deck
     */
    public static <T extends StandardCard> ArrayList<T> getStandardDeck(int[] customStandardDeck, CardFactory<T> cardMaker){
        assert customStandardDeck.length == CARDSINSTANDARDDECK;

        var deck = new ArrayList<T>();
        for (int i = 0; i < CARDSINSTANDARDDECK; i++){
            for (int j = 0; j < customStandardDeck[i]; j++){
                var suit = StandardCard.getStringSuit(i/ CARDSINSTANDARDSUIT + StandardCard.LOWESTSUIT);
                var denom = suit.equals(StandardCard.JOKERSUIT) ?
                        i%2 == 1 ? StandardCard.JOKERRED : StandardCard.JOKERBLACK :
                        i % CARDSINSTANDARDSUIT + StandardCard.LOWESTDENOM;
                deck.add(0, cardMaker.makeCard(suit, denom));
            }
        }

        return deck;
    }

    /**
     * Translates a number of standard decks into a custom deck formatted array
     * @param numdecks The number of standard decks in the deck; The number of copies of any card in a standard deck
     * @param jokers A boolean determining whether Jokers will be part of the standard deck or not
     * @return An array of numdecks with 0 for the last two values if there are no jokers
     */
    static private int[] customFromStandardDeck(int numdecks, int jokers) {
        // Create a custom deck array with numdecks of each type of card in a standard deck
        int[] customDeck = new int[CARDSINSTANDARDDECK];
        Arrays.fill(customDeck, numdecks);

        // set # jokers to given amount
        customDeck[CARDSINSTANDARDDECK - 2] = jokers/2 + jokers%2;
        customDeck[CARDSINSTANDARDDECK - 1] = jokers/2;

        return customDeck;
    }

    /**
     * @return the number of unique cards in each suit
     */
    static public int getNumCardsInSuit(){
        return CARDSINSTANDARDSUIT;
    }

    /**
     *
     * @param <T> The type of the card in the deck
     */
    @FunctionalInterface
    public static interface CardFactory<T extends StandardCard>{
        public T makeCard(String suit, int denomination);
    }
}
