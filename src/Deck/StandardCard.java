package Deck;

import java.security.InvalidParameterException;

public class StandardCard extends Card {
    //Constants
    public static final String  CLUBS = "C",
                                DIAMONDS = "D",
                                SPADES = "S",
                                HEARTS = "H",
                                JOKERSUIT = "J";
    public static final String[] SUITS = {CLUBS, DIAMONDS, SPADES, HEARTS, JOKERSUIT};
    public static final int LOWESTSUIT = 0;
    public static final int JOKERRED = -2, JOKERBLACK = -1, ACE = 1, TWO = 2, THREE = 3, FOUR = 4, FIVE = 5,
                            SIX = 6, SEVEN = 7, EIGHT = 8, NINE = 9, TEN = 10,
                            JACK = 11, QUEEN = 12, KING = 13;
    public static final int LOWESTDENOM = ACE;


    private final String suit; // A number 1-5 representing the suit of the card
    private final int denomination; // A number 1-13 representing the denomination of the card

    /**
     * Creates a standard playing card
     * @param suit The suit of the card, chosen from the suit constants
     * @param denomination The denomination of the card, chosen from the denomination constants. If suit is Joker, joker denom used
     */
    public StandardCard(String suit, int denomination){
        super(denomToString(denomination) + suit, getNumericalSuit(suit)*DeckUtil.getNumCardsInSuit() + denomination);
        this.suit = suit;
        this.denomination = denomination;

        if (!isValidSuit(suit))
            throw new InvalidParameterException("Invalid suit  for standard card: " + suit);
        if (!isValidDenom(denomination))
            throw new InvalidParameterException("Invalid denomination for standard card: " + denomination);
        if (!isValidSuitDenomCombo(suit, denomination))
            throw new InvalidParameterException("Invalid denomination and suit combination for standard card: " + suit + ", " + denomination);
    }

    /**
     * @return the string representation of a card's suit
     */
    public String getSuit() {
        return suit;
    }

    /**
     * @return the numerical represeentation of a card's denomination
     */
    public int getDenomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof StandardCard){
            StandardCard card = (StandardCard) obj;
            return card.getSuit().equals(suit) &&
                    card.getDenomination() == denomination;
        }
        else return false;
    }

    /**
     * Converts a denomination of a card to a string
     * @param denom The denomination to be converted
     * @return the string equivalent
     */
    public static String denomToString(int denom) {
        switch (denom) {
            case ACE: return "A";
            case JACK: return "J";
            case QUEEN: return "Q";
            case KING: return "K";
            case JOKERRED: return "0";
            case JOKERBLACK: return "0";
            case TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN: return "" + denom;
        }
        throw new InvalidParameterException("Invalid denomination: " + denom);
    }

    /**
     * @param denom The denomination to be tested
     * @return true if the denomination is valid for a standard deck, false otherwise
     */
    public static boolean isValidDenom(int denom) {
        try {
            StandardCard.denomToString(denom);
            return true;
        } catch (InvalidParameterException e){
            return false;
        }
    }

    /**
     * Gets the numerical representation of a card's suit
     * @param suit the string representation of the card's suit
     * @return the numerical representation of the card's suit
     */
    public static int getNumericalSuit(String suit) {
        for(int i = 0; i < SUITS.length; i++){
            if(suit.equals(SUITS[i])){
                return i;
            }
        }
        throw new InvalidParameterException("Invalid suit: " + suit);
    }

    /**
     * @param suit The suit to be tested
     * @return true if the suit is valid for a standard deck, false otherwise
     */
    public static boolean isValidSuit(String suit) {
        try {
            StandardCard.getNumericalSuit(suit);
            return true;
        } catch (InvalidParameterException e){
            return false;
        }
    }

    /**
     * Gets the string representation of a card's suit
     * @param suit the numerical representation of a card's suit
     * @return the string representation of a card's suit
     */
    public static String getStringSuit(int suit) {
        if (suit >= SUITS.length || suit < 0)
            throw new InvalidParameterException("Invalid suit number: " + suit);
        return SUITS[suit];
    }

    /**
     * This function checks that joker suits and denominations are paired properly
     * @param suit The valid standard suit for a card
     * @param denomination The valid standard denomination for a card
     * @return Whether the suit and denomination makea valid standard card
     */
    public static boolean isValidSuitDenomCombo(String suit, int denomination){
        boolean isJokerDenom = denomination == JOKERBLACK || denomination == JOKERRED;
        if(suit.equals(JOKERSUIT)) return isJokerDenom;
        else return !isJokerDenom;
    }

    /**
     * @return true if the card is a joker, false otherwise
     */
    public boolean isJoker(){
        return getSuit().equals(JOKERSUIT);
    }

    @Override
    public String toString(){
        return denomToString(denomination) + suit;
    }
}