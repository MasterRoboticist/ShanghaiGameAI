package Shanghai.Table;

import Deck.*;
import Shanghai.*;

import java.util.ArrayList;

public class Run extends Deck<ShanghaiCard> implements Iterable<ShanghaiCard>{
    private int firstCard;
    private String suit;
    private int NOTPLAYABLE = -2;

    /**
     * Creates a run with no cards in it with a defined first card
     * @param suit The suit of the run, from the options in in StandardCard
     * @param firstCard The denomination of the first card, from the options in StandardCard
     */
    public Run(String suit, int firstCard){
        if (!StandardCard.isValidDenom(firstCard))
            throw new CheaterCheaterPumpkinEaterException("Invalid first card");
        if (firstCard == StandardCard.JOKERBLACK || firstCard == StandardCard.JOKERRED)
            throw new CheaterCheaterPumpkinEaterException("First card denomination may not be a joker denomination");
        this.firstCard = firstCard;

        if (!StandardCard.isValidSuit(suit)) {
            throw new CheaterCheaterPumpkinEaterException("Invalid suit");
        }
        if (suit.equals(StandardCard.JOKERSUIT)) {
            throw new CheaterCheaterPumpkinEaterException("Suit may not be Jokers");
        }
        this.suit = suit;
    }

    /**
     * Creates a run with the given cards. The cards must be in order
     * @param suit The suit of the run, from the options in in StandardCard
     * @param firstCard The denomination of the first card, from the options in StandardCard
     * @param cards The cards to be added to the run
     */
    public Run(String suit, int firstCard, ArrayList<ShanghaiCard> cards){
        this(suit, firstCard);

        var currDenom = firstCard;
        for(var card: cards){
            add(card, currDenom);
            currDenom ++;
        }
    }

    public Run(Run run){
        this(run.getSuit(), run.getFirstCard().getDenomination());
        var denom = run.getFirstCard().getDenomination();
        for(var c: deck){
            add(c, denom);
            denom++;
        }
    }

    public Run(RunWrapper run){
        this(run.getSuit(), run.getFirstCard().getDenomination());
        var denom = run.getFirstCard().getDenomination();
        for(var c: deck){
            add(c, denom);
            denom++;
        }
    }

    /**
     * Adds a card to the run. Throws an error if the card cannot be added
     * @param card the card to be added
     * @param denomination the denomination where this card will be played (ignored unless card is a Joker)
     * @return A joker if the card replaced a joker, null otherwise
     */
    public ShanghaiCard add(ShanghaiCard card, int denomination) {
        int place = card.isJoker() ? wherePlayable(denomination) : wherePlayable(card);
        if(place == NOTPLAYABLE) {
            if (card.isJoker()) throw new CheaterCheaterPumpkinEaterException(
                    "Joker can't be played as a " + denomination + " on run " + this);
            else throw new CheaterCheaterPumpkinEaterException(card + " can't be played on run " + this);
        }

        if(place == -1) {
            addCard(card, 0);
            firstCard--;
        }
        else if(place == getNumCards()) addCard(card);
        else return replace(place, card); // This always returns a joker

        return null;
    }

    /**
     * Throws an error when used. Overridden method to prevent people from using it
     * @param card  The card object to be added
     * @param place The place in the deck to add the card
     * @return Throws an error when called
     */
    public boolean addCard(ShanghaiCard card, int place){
        throw new CheaterCheaterPumpkinEaterException("You must use add(card, denomination) because reasons");
    }

    /**
     * Checks if a card is playable on a given downable thing
     * @param card the card to be checked
     * @param denomination the denomination where this card will be played (ignored unless card is a Joker)
     * @return true if the card could be played, false otherwise
     */
    public boolean isPlayable(ShanghaiCard card) {
        return wherePlayable(card) != NOTPLAYABLE;
    }

    /**
     * Finds the index at which the card can be played in a run. If an Ace can be played at both ends,
     * returns the first index
     * @param card The card to be played on the run
     * @return The index where it can be played, -1 if added to start, NOTPLAYABLE if can't be played
     */
    private int wherePlayable(ShanghaiCard card) {
        if(!card.getSuit().equals(suit)) return NOTPLAYABLE;

        // Joker logic: Joker will try to be first cards, then will try to be last card. If the run is complete
        if (card.isJoker()){
            if(isCompleteRun())
                return NOTPLAYABLE;

            else if (firstCard != StandardCard.ACE) return -1;
            else return getNumCards();
        }

        return wherePlayable(card.getDenomination());
    }

    /**
     * Finds the index at which the card can be played in a run. If an Ace can be played at both ends,
     * returns the first index
     * @param cardDenom The denomination of the card to be played
     * @return The index where it can be played, -1 if added to start, null if can't be played
     */
    private int wherePlayable(int cardDenom){
        var lastCard = peek(getNumCards()).getDenomination();

        if (firstCard - 1 == cardDenom) return -1;
        if (lastCard + 1 == cardDenom) return getNumCards();

        for(int i = 0; i < getNumCards(); i++){
            var currCard = peek(i);
            if(!currCard.isJoker() && currCard.getDenomination() == cardDenom)
                return NOTPLAYABLE;
            else if(cardDenom == firstCard + i) return i;
        }

        return NOTPLAYABLE;
    }

    /**
     * @return true if cards can no longer be added to the beginning and end of this run, false otherwise
     */
    public boolean isCompleteRun(){
        return getNumCards() >= DeckUtil.getNumCardsInSuit();
    }

    /**
     * @return True if the run is long enough to be a valid run
     */
    public boolean isValidRun() {
        // Check that run contains the minimum cards
        return getNumCards() >= 4;
    }

    /**
     * @return The suit of the run
     */
    public String getSuit() {
        return suit;
    }

    /**
     * @return The first (lowest) card in the run. If Joker, returns the card the joker replaces
     */
    public ShanghaiCard getFirstCard(){
        if(peek(0).isJoker()){
            return new ShanghaiCard(suit, firstCard);
        }
        else return peek(0);
    }

    /**
     * @return The last (highest) card in the run. If joker, returns the card the joker replaces
     */
    public ShanghaiCard getLastCard(){
        if(peek(getNumCards()).isJoker()){
            var card = new ShanghaiCard(suit, firstCard).cardBefore();
            for(ShanghaiCard c: deck){
                card = card.cardAfter();
            }
            return card;
        }
        else return peek(getNumCards());
    }

    /**
     * @return the cards in the run that are jokers
     */
    public ArrayList<ShanghaiCard> getJokerCards(){
        var jokerCards = new ArrayList<ShanghaiCard>();
        var previousCard = getFirstCard().cardBefore();

        for(ShanghaiCard c: deck){
            if(c.isJoker()) {
                jokerCards.add(previousCard.cardAfter());
                previousCard = previousCard.cardAfter();
            }
            else previousCard = c.cardAfter();
        }
        return jokerCards;
    }
}
