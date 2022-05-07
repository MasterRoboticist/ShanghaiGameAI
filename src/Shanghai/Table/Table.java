package Shanghai.Table;

import Deck.Deck;
import Shanghai.CheaterCheaterPumpkinEaterException;
import Shanghai.ShanghaiCard;

import java.util.ArrayList;

public class Table {
    //GLOBALS
    private ArrayList<Set> sets = new ArrayList<Set>();
    private ArrayList<Run> runs = new ArrayList<Run>();
    private ArrayList<Hand> hands;
    private int tableJoker = 0;

    /**
     * Creates a table with the given hands and no sets or runs
     * @param hands The hands the table can play from
     */
    Table(ArrayList<Hand> hands){
        this.hands = hands;
    }

    /**
     * Creates a table with the given inputs. Runs and Sets are added blindly without checking
     * that they are valid. This is purposeful
     * @param hands The hands the table can play from
     * @param runs The runs already on the table (can be incomplete or invalid)
     * @param sets The sets already on the table (can be incomplete or invalid)
     */
    Table(ArrayList<Hand> hands, ArrayList<Run> runs, ArrayList<Set> sets){
        this.hands = hands;

        // This purposefully does not check if these are valid sets and runs
        this.runs = runs;
        this.sets = sets;
    }

    /**
     * Does a mid depth copy of the table. The cards are the same objects, but the runs and sets are copies
     * @return
     */
    public Table(Table table, ArrayList<Hand> hands){
        this(hands);
        sets.addAll(table.sets);
        runs.addAll(table.runs);
    }

    //FUNCTIONS

    /**
     * @return the list of runs on the table
     */
    public ArrayList<RunWrapper> getRuns() {
        var runsWrapper = new ArrayList<RunWrapper>();
        for(Run r: runs){
            runsWrapper.add(new RunWrapper(r));
        }
        return runsWrapper;
    }

    /**
     * @return the list of sets on the table
     */
    public ArrayList<SetWrapper> getSets() {
        var SetsWrapper = new ArrayList<SetWrapper>();
        for(Set s: sets){
            SetsWrapper.add(new SetWrapper(s));
        }
        return SetsWrapper;
    }

    /**
     * @return The number of runs on the table
     */
    public int getNumRuns(){
        return runs.size();
    }

    /**
     * @return The number of sets on the table
     */
    public int getNumSets(){
        return sets.size();
    }

    /**
     * Adds a copy of a set to the table
     * @param set the set to be added
     * @param hand the hand to take the cards from. Must be a hand on the table
     */
    public void addSet(Set set, HandWrapper hand){
        if(!isHandOnTable(hand)) throw new CheaterCheaterPumpkinEaterException("That hand isn't in play");

        if (!set.isValidSet()) throw new CheaterCheaterPumpkinEaterException("Invalid " + set.getNumCards() + " card set");

        playCards(set, getHand(hand));

        // Add the set to the table
        sets.add(new Set(set));
    }

    /**
     * Adds a copy of the given run to the table
     * @param run the run to be added
     * @param hand the hand to take the cards from. Must be a hand on the table
     */
    public void addRun(Run run, HandWrapper hand){
        if(!isHandOnTable(hand)) throw new CheaterCheaterPumpkinEaterException("That hand object isn't in play");

        if(!run.isValidRun()) throw new CheaterCheaterPumpkinEaterException("Run is not valid: " + run);

        playCards(run, getHand(hand));

        runs.add(new Run(run));
    }

    /**
     * Adds the given card to the given run. Throws an error if it cannot be added
     * @param runW The run to be modified
     * @param card the card to be added
     * @param handW the hand to take the cards from. Must be a hand on the table
     * @return a joker if a joker was replaced, otherwise null
     */
    public ShanghaiCard addToRun(RunWrapper runW, ShanghaiCard card, int denomination, HandWrapper handW){
        var run = runW.run;
        boolean useTableJoker = card.isJoker() && tableJoker > 0;

        if(!isHandOnTable(handW)) throw new CheaterCheaterPumpkinEaterException("That hand isn't in play");
        if(!isRunOnTable(run)) throw new CheaterCheaterPumpkinEaterException("That run object isn't on the table");

        var joker = run.add(card, denomination);
        if(!useTableJoker) {
            getHand(handW).playCard(card);
            if (joker != null)
                tableJoker++;
        }
        else tableJoker--;
        return joker;
    }

    /**
     * Adds the given card to the given set. Throws an error if it cannot be added
     * @param setW The set to be modified
     * @param card the card to be added
     * @param handW the hand to take the cards from. Must be a hand on the table
     */
    public void addToSet(SetWrapper setW, ShanghaiCard card, HandWrapper handW){
        var set = setW.set;
        var hand = getHand(handW);
        if(!isHandOnTable(handW)) throw new CheaterCheaterPumpkinEaterException("That hand isn't in play");
        if(!isSetOnTable(set)) throw new CheaterCheaterPumpkinEaterException("That set object isn't on the table");

        set.addCard(card);
        hand.playCard(card);
    }

    public int getTableJokers(){
        return tableJoker;
    }

    /**
     * @return The number of cards down on the table (sets and runs)
     */
    public int numCardsOnTable(){
        int sum = 0;
        for(Set s: sets){
            sum += s.getNumCards();
        }
        for(Run r: runs){
            sum += r.getNumCards();
        }
        return sum;
    }

    /**
     * Checks if a given run object is one of the run objects on the table
     * @param run the run the check
     * @return a boolean indicating if the run is on the table
     */
    public boolean isRunOnTable(Run run){
        for(Run r: runs){
            if(r == run) return true;
        }
        return false;
    }
    public boolean isRunOnTable(RunWrapper run){
        return isRunOnTable(run.run);
    }

    /**
     * Checks if a given set object is one of the set objects on the table
     * @param set the set to check
     * @return a boolean indicating if the set is on the table
     */
    public boolean isSetOnTable(Set set){
        for(Set s: sets){
            if(s == set) return true;
        }
        return false;
    }
    public boolean isSetOnTable(SetWrapper set){
        return isSetOnTable(set.set);
    }

    /**
     * Checks if a given hand object is one of the hand objects on the table
     * @param hand the hand to check
     * @return a boolean indicating if the hand is on the table
     */
    public boolean isHandOnTable(HandWrapper hand){
        return getHand(hand) != null;
    }

    public Hand getHand(HandWrapper hand){
        for(Hand h: hands){
            if(h.getHand() == hand) return h;
        }
        return null;
    }

    /**
     * Plays all the given cards from the given hand
     * @param deck The deck/set/run to be removed from the hand
     * @param hand The hand to remove cards from
     */
    private void playCards(Deck<ShanghaiCard> deck, Hand hand){
        for(ShanghaiCard c: deck){
            if(!hand.playCard(c))
                throw new CheaterCheaterPumpkinEaterException(
                        "Player played " + c + ", but that card wasn't in their hand");
        }
    }

    /**
     * Removes all cards from the table
     */
    public void reset(){
        sets.clear();
        runs.clear();
    }

    public String toString(){
        String s = "Table:";
        s += "\n\tSets: ";
        for(var set: sets) {
            s += set;
            s += ", ";
        }
        s += "\n\tRuns: ";
        for(var r: runs){
            s += r;
            s += ", ";
        }
        return s;
    }
}
