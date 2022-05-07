package Shanghai.Player;

import Deck.StandardCard;
import Shanghai.Table.*;
import Shanghai.ShanghaiCard;

import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * A class of static functions that can be useful to players
 */
public class PlayerUtil {
    /**
     * Returns the minimum number of playable cards in a hand. May miss some playable cards in complex circumstances
     * This is not a lightweight function. Use sparingly
     * @param hand The hand to be played
     * @param table The table to play the hand on
     * @param numSets The number of sets for the round
     * @param numRuns The number of runs for the round
     * @param isDown whether the player is down or not
     * @return The minimum number of playable cards in the hand
     */
    public static int maxPlayableCards(HandWrapper hand, Table table, int numSets, int numRuns, boolean isDown){
        return playableCards(hand, table, numSets, numRuns, isDown).size();
    }

    /**
     * Returns the cards in a hand that are playable at a minimum. May miss some playable cards in complex circumstances
     * This is not a lightweight function. Use sparingly
     * @param handW The hand (HandWrapper object) to be played
     * @param table The table to play the hand on
     * @param numSets The number of sets for the round
     * @param numRuns The number of runs for the round
     * @param isDown whether the player is down or not
     * @return The playable cards in the hand
     */
    public static ArrayList<ShanghaiCard> playableCards(HandWrapper handW, Table table, int numSets, int numRuns, boolean isDown){

        if(!isDown) {
            // find runs and sets

            // remove all cards playable on the table

            // find best runs/sets

            // play them
        }

        // play the rest of the cards on the table

        return new ArrayList<ShanghaiCard>();
    }

    public static ArrayList<ShanghaiCard> cardsPlayableOnTable(HandWrapper handW, Table table){
        var playableCards = new ArrayList<ShanghaiCard>();

        var hand = new Hand(handW);
        var handlist = new ArrayList<Hand>();
        handlist.add(hand);
        Table theorTable = new Table(table, handlist);

        // remove jokers
        var jokers = getJokers(hand);
        hand.dealMultiple(jokers);

        int jokersToUse = 0;
        do {
            // play cards on runs
            var playedCard = true;
            while (playedCard) {
                playedCard = false;
                for (var c : hand) {
                    // prioritize runs where the card replaces a joker since these are always the best play
                    var runToPlayOn = replacesRunJoker(c, theorTable);
                    // find other places to play
                    if (runToPlayOn == null) {
                        var wherePlayable = playableOnRuns(c, theorTable, jokersToUse);
                        runToPlayOn = getBestRunPlay(c, wherePlayable, hand);

                    }
                    // play
                    if (runToPlayOn != null) {
                        var replacedJoker = table.addToRun(runToPlayOn, c, c.getDenomination(), hand.getHand());
                        if(replacedJoker != null) jokers.add(replacedJoker);
                        playableCards.add(c);
                        playedCard = true;
                        if(jokersToUse > 0){ //TODO this could be optimized lots
                            jokersToUse = 0;
                            break;
                        }

                    }
                }
            }
            jokersToUse++;
        }while (jokers.size() > 0);

        // play cards on sets
        for(var c: hand){
            var set = playableOnSet(c, table);
            if(set != null) {
                table.addToSet(set, c, hand.getHand());
                playableCards.add(c);
            }
        }

        // There are no edge cases where this doesn't work because, if no sets, someone will win
        // long before all available runs are complete
        playableCards.addAll(jokers);

        return playableCards;
    }

    /**
     * This function finds the best run to play a card on. It assumes the card can be played on all the given
     * runs. It also assumes all cards that could be played before the given card have been played. It checks
     * for cards that can be played after this card to determine the best play. (i.e. in a run 2-3-4-5, with
     * one joker and 7 as the given card, it does not check if the hand contains a 6, but will check if the
     * hand contains an 8).
     * @param c The given card to
     * @param wherePlayable The runs where the card can be played
     * @param hand The hand the cards will be played from
     * @return The best run to play on, or the last best run
     */
    private static RunWrapper getBestRunPlay(ShanghaiCard c, ArrayList<RunWrapper> wherePlayable, Hand hand){
        if (wherePlayable.size() == 0) return null;
        if (wherePlayable.size() == 1) return wherePlayable.get(0);

        boolean isAce = c.getDenomination() == StandardCard.ACE;
        // if the card is an ace, you can't play cards before/after it in an existing run
        boolean haveLowerCard = !isAce && hand.find(c.cardBefore()) != -1;
        boolean haveHigherCard = !isAce && hand.find(c.cardAfter()) != -1;
        RunWrapper playLower = null;
        RunWrapper playHigher = null;

        for (var r : wherePlayable) {
            if (haveLowerCard && r.getFirstCard().distanceDown(c) != -1)
                playLower = r;
            else if (haveHigherCard && r.getLastCard().distanceUp(c) != -1)
                playHigher = r;
            else // replaces a joker, so it's always the best choice
                return r;
        }
        //TODO: if both, this could figure out if there are more cards that could be played
        if(playLower != null && haveLowerCard) return playLower;
        else return playHigher;
    }

    public static ArrayList<Run> getRuns(Hand hand){
        return null;
    }

    public static ArrayList<Set> getSets(Hand hand){
        return null;
    }

    public static ArrayList<RunWrapper> playableOnRuns(ShanghaiCard card, Table table, int numJokersToUse){
        var runs = table.getRuns();
        for(var r: runs){
            // if joker
            if(card.isJoker()) {
                if (r.isCompleteRun()) break;
                else runs.add(r);
            }
            // if not joker
            if(r.getSuit().equals(card.getSuit())) {
                var distBelowFirst = r.getFirstCard().distanceDown(card);
                var distAboveLast = r.getLastCard().distanceUp(card);

                if(distAboveLast == numJokersToUse+1 || distBelowFirst == numJokersToUse+1){
                    runs.add(r);
                }
            }
        }
        return runs;
    }

    public static RunWrapper replacesRunJoker(ShanghaiCard card, Table table){
        if(card.isJoker()) return null;
        for(var r: table.getRuns()){
            for(var c: r.getJokerCards()){
                if(card.equals(c)) return r;
            }
        }
        return null;
    }

    public static SetWrapper playableOnSet(ShanghaiCard card, Table table){
        var sets = table.getSets();
        for(var s: sets){
            if(s.isPlayable(card)){
                return s;
            }
        }
        return null;
    }

    public static ArrayList<ShanghaiCard> getJokers(Hand hand){
        var jokers = new ArrayList<ShanghaiCard>();
        for(var c: hand){
            if(c.isJoker()){
                jokers.add(c);
            }
        }
        return jokers;
    }
}
