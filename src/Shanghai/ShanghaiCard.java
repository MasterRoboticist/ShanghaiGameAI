package Shanghai;

import Deck.DeckUtil;
import Deck.StandardCard;

public class ShanghaiCard extends StandardCard {
    protected int points;

    public ShanghaiCard(String suit, int denomination){
        super(suit, denomination);

        if (denomination == ACE || suit.equals(JOKERSUIT)) points = 20;
        else if (denomination >= EIGHT) points = 10;
        else points = 5;
    }

    /**
     * @return The card that would be before this one in a run. Ace returns King
     */
    public ShanghaiCard cardBefore(){
        int newDenom = getDenomination() == ACE ? KING : getDenomination() - 1;
        return new ShanghaiCard(getSuit(), newDenom);
    }

    /**
     * @return The card that would be after this one in a run. King returns Ace
     */
    public ShanghaiCard cardAfter(){
        int newDenom = getDenomination() == KING ? ACE : getDenomination() + 1;
        return new ShanghaiCard(getSuit(), newDenom);
    }

    /**
     * Returns the minimum distance between the given card and this card. Checks Ace high and Ace low
     * @param otherCard the card to compare to this card
     * @return the minimum distance between the cards. -1 if the cards are different suits or jokers
     */
    public int minDistance(ShanghaiCard otherCard){
        var distDown = distanceDown(otherCard);
        var distUp = distanceUp(otherCard);

        if(distDown == -1) return distUp;
        else if(distUp == -1) return distDown;
        else return Math.min(distUp, distDown);
    }

    /**
     * Distance from otherCard to this, where otherCard is lower than this. -1 otherwise
     * @param otherCard
     * @return
     */
    public int distanceDown(ShanghaiCard otherCard){
        return otherCard.distanceUp(this);

    }

    /**
     * Distance from this to otherCard, where otherCard is greater than this. -1 otherwise
     * @param otherCard
     * @return
     */
    public int distanceUp(ShanghaiCard otherCard){
        int otherDenom = otherCard.getDenomination();
        int thisDenom = getDenomination();

        if(otherDenom == thisDenom) return 0;

        otherDenom = otherDenom == ACE ? KING + 1: otherDenom;

        if (!otherCard.getSuit().equals(getSuit())) return -1;
        if (otherCard.isJoker() || this.isJoker()) return -1;
        if (otherDenom < thisDenom) return -1;

        return otherDenom - thisDenom;
    }

    /**
     * @return The number of points this card is worth
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return The card factory for DeckUtil
     */
    public static DeckUtil.CardFactory<ShanghaiCard> getCardFactory(){
        return ShanghaiCard::new;
    }
}
