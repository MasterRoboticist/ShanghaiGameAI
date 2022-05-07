package Shanghai.Player;

import Shanghai.ShanghaiCard;
import Shanghai.Table.HandWrapper;
import Shanghai.Table.Table;

import java.util.ArrayList;

public abstract class Player {
    /**
     * The player's current hand
     */
    public final HandWrapper hand;
    /**
     * The number that represents the player in this game
     */
    public final int playerNumber;
    /**
     * The number of players playing in this game
     */
    protected final int numberOfPlayers;

    public static final int DECK = 0;
    public static final int DISCARD = 1;

    /**
     * Creates a new player
     * @param playerNumber the number assigned to the player in the game
     */
    public Player(int playerNumber, int numberOfPlayers, HandWrapper hand){
        this.playerNumber = playerNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.hand = hand;
    }

    /**
     * Called when another player discards a card
     * @param card The card that was discarded
     * @param player The player's number
     */
    abstract public void cardDiscarded(ShanghaiCard card, int player);

    /**
     * Called when the players have the option to buy a card
     * @param card the card available o be bought
     * @return true if the player would like to try to buy the card
     */
    abstract public boolean getBuyIntention(ShanghaiCard card);

    /**
     * Called when this player successfully buys a card. The given cards have already
     * been added to the player's hand
     * @param cards The three cards gained with the buy (the discard and the two random)
     */
    abstract public void giveBuy(ArrayList<ShanghaiCard> cards);

    /**
     * Called when the a new player begins their turn
     * @param player the player whose turn it is
     * @param numCards the number of cards the player has at the start of their turn
     */
    abstract public void playerStartedTurn(int player, int numCards);

    /**
     * Called when the a new player begins their turn
     * @param player the player whose turn it is
     * @param numCards the number of cards the player has at the end of their turn
     */
    abstract public void playerEndedTurn(int player, int numCards);

    /**
     * Called when a discarded card is picked up
     * @param card the card that was picked up
     * @param player the player who picked up the discarded card
     */
    abstract public void discardPickedUp(ShanghaiCard card, int player);

    /**
     * Called when a discarded card is bought
     * @param card the card that was bought
     * @param player the player who bought the card
     */
    abstract public void discardBought(ShanghaiCard card, int player);

    /**
     * Called when a player goes down
     * @param table the current table
     * @param player the player that went down
     */
    abstract public void playerWentDown(Table table, int player);

    /**
     * Called when the table changes
     * @param table the current table
     * @param player the player that changed the table
     */
    abstract public void tableChanged(Table table, int player);

    /**
     * Called before a player's turn to know from where they would like to draw
     * Skipped if the card on the discard is dead
     * @param discard the card on top of the discard
     * @return DECK or DISCARD
     */
    abstract public int getDrawChoice(ShanghaiCard discard);

    /**
     * Called when it is a player's turn
     * @param draw The card that the player drew
     * @param table The current game table
     * @return The card being discarded
     */
    abstract public ShanghaiCard takeTurn(ShanghaiCard draw, Table table);

    /**
     * Called after the player's turn or a table state change to know if the player would like to double knock
     * @return true if double knocking, false otherwise
     */
    abstract public boolean getDoubleKnockStatus();

    /**
     * Called at the beginning of the game
     */
    abstract public void startGame();

    /**
     * Called at the beginning of a round
     * @param sets The number of sets required for the round
     * @param runs The number of runs required for the round
     */
    abstract public void newRound(int sets, int runs, ShanghaiCard firstDiscard);

    /**
     * Called at the end of a round. All players must return from this function before
     * cards are recalled and the round ends
     * @param roundScores The points gained by the players this round, where player x is at index x
     * @param totalScores The total scores for the game so far, where player x is at index x
     */
    abstract public void endRound(int[] roundScores, int[] totalScores);

    /**
     * Called at the end of a game. All players must return from this function before the game ends
     * @param totalScores The scores at the end of the game, where player x is at index x
     */
    abstract public void endGame(int[] totalScores);
}
