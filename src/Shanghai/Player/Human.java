package Shanghai.Player;

import Deck.StandardCard;
import Shanghai.ShanghaiCard;
import Shanghai.Table.HandWrapper;
import Shanghai.Table.Run;
import Shanghai.Table.Set;
import Shanghai.Table.Table;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Human extends Player {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int numSets = 0;
    int numRuns = 0;
    String messageBacklog = "";
    Table table;
    boolean isDown = false;

    /**
     * Creates a new player
     *
     * @param playerNumber    the number assigned to the player in the game
     * @param numberOfPlayers
     * @param hand
     */
    public Human(int playerNumber, int numberOfPlayers, HandWrapper hand) {
        super(playerNumber, numberOfPlayers, hand);
    }

    /**
     * Called when another player discards a card
     *
     * @param card   The card that was discarded
     * @param player The player's number
     */
    @Override
    public void cardDiscarded(ShanghaiCard card, int player) {
        messageBacklog += card + " discarded by player "+ player + "\n";
    }

    /**
     * Called when the players have the option to buy a card
     *
     * @param card the card available o be bought
     * @return true if the player would like to try to buy the card
     */
    @Override
    public boolean getBuyIntention(ShanghaiCard card) {
        startSomething();
        System.out.println("Card " + card + " is available to be bought.");
        String in = getInput("Enter 'B' to buy");

        endSomething(false);

        return in.equals("B");
    }

    /**
     * Called when this player successfully buys a card. The given cards have already
     * been added to the player's hand
     *
     * @param cards The three cards gained with the buy (the discard and the two random)
     */
    @Override
    public void giveBuy(ArrayList<ShanghaiCard> cards) {
        startSomething();
        System.out.print("You were given these cards in the buy: ");
        for(var c: cards) System.out.print(c + ", ");
        System.out.println();
        endSomething(true);
    }

    /**
     * Called when the a new player begins their turn
     *
     * @param player   the player whose turn it is
     * @param numCards the number of cards the player has at the start of their turn
     */
    @Override
    public void playerStartedTurn(int player, int numCards) {
        messageBacklog += "Player " + player + " has started their turn with " + numCards + " cards in hand.\n";
    }

    /**
     * Called when the a new player begins their turn
     *
     * @param player   the player whose turn it is
     * @param numCards the number of cards the player has at the end of their turn
     */
    @Override
    public void playerEndedTurn(int player, int numCards) {
        messageBacklog += "Player " + player + " has ended their turn with " + numCards + " cards in hand. \n";
    }

    /**
     * Called when a discarded card is picked up
     *
     * @param card   the card that was picked up
     * @param player the player who picked up the discarded card
     */
    @Override
    public void discardPickedUp(ShanghaiCard card, int player) {
        messageBacklog += "Player " + player + " picked up the discard: " + card + "\n";
    }

    /**
     * Called when a discarded card is bought
     *
     * @param card   the card that was bought
     * @param player the player who bought the card
     */
    @Override
    public void discardBought(ShanghaiCard card, int player) {
        messageBacklog += "Player " + player + " bought the discard: " + card + "\n";
    }

    /**
     * Called when a player goes down
     *
     * @param table  the current table
     * @param player the player that went down
     */
    @Override
    public void playerWentDown(Table table, int player) {
        this.table = table;
        messageBacklog += "Player " + player + " went down.\n";
    }

    /**
     * Called when the table changes
     *
     * @param table  the current table
     * @param player the player that changed the table
     */
    @Override
    public void tableChanged(Table table, int player) {
        this.table = table;
        messageBacklog += "The table has been changed by player " + player + ".\n" + table;
    }

    /**
     * Called before a player's turn to know from where they would like to draw
     * Skipped if the card on the discard is dead
     *
     * @param discard the card on top of the discard
     * @return DECK or DISCARD
     */
    @Override
    public int getDrawChoice(ShanghaiCard discard) {
        startSomething();
        System.out.println("Choose to pick up the discard, " + discard + ", or pick from the deck.");
        String in;
        do {
            in = getInput("Enter 'DISCARD' for discard and 'DECK' for deck");
        } while (!in.equals("DISCARD") && !in.equals("DECK"));

        endSomething(false);

        if(in.equals("DISCARD")) return DISCARD;
        if(in.equals("DECK")) return DECK;

        //can't get here but please compile
        return -1;
    }

    /**
     * Called when it is a player's turn
     *
     * @param draw  The card that the player drew
     * @param table The current game table
     * @return The card being discarded
     */
    @Override
    public ShanghaiCard takeTurn(ShanghaiCard draw, Table table) {
        this.table = table;
        startSomething(table);
        System.out.println("It is your turn. You drew " + draw);
        System.out.println("Would you like to play anything on the table?");
        String in = null;
        in = getInput("Enter 'YES' if yes. All else will be considered no.");

        if(in.equals("YES")) {
            System.out.println("\tWould you like to play new sets or runs?");
            in = getInput("\tEnter 'YES' if yes. All else will be considered no.");
            if(in.equals("YES")) {
                isDown = true;

                // Get new sets
                System.out.println("Enter the numbers of the cards in your new set separated by commas. Enter after every set.");
                System.out.println("When you are finished entering sets, type END.");
                do{
                    System.out.print("\t\tTable: " + table + "\n\t\t");
                    printNumberedHand();
                    in = getInput("");
                    if(in.equals("END")) break;
                    var selections = in.split(",");
                    var cards = new ArrayList<ShanghaiCard>();
                    for (String selection : selections) {
                        cards.add(hand.getCard(Integer.parseInt(selection)));
                    }
                    Set set = new Set(cards.get(0).getDenomination());
                    set.addCards(cards);
                    table.addSet(set, hand);
                }while (true);

                // Get new runs
                System.out.println("Enter the numbers of the cards in your new run  in order separated by commas. Enter after every run.");
                System.out.println("When you are finished entering runs, type END.");
                do{
                    in = getInput("");
                    if(in.equals("END")) break;
                    var selections = in.split(",");
                    var cards = new ArrayList<ShanghaiCard>();
                    for (String selection : selections) {
                        cards.add(hand.getCard(Integer.parseInt(selection)));
                    }
                    var firstDenom = cards.get(0).getDenomination();
                    Run run = new Run(cards.get(0).getSuit(), firstDenom);
                    for(int i = 0; i < cards.size(); i++){
                        run.add(cards.get(i), i+firstDenom);
                    }
                    table.addRun(run, hand);
                }while (true);
            }

            System.out.println("\tWould you like to play on existing runs?");
            in = getInput("\tEnter 'YES' if yes. All else will be considered no.");
            if(in.equals("YES")) {
                // Get additions to runs
                System.out.println("Enter the number of the card you are playing and the run you are playing on separated by commas. Enter after every selection.");
                System.out.println("When you are finished entering cards, type END.");
                do{
                    System.out.print("\t\tNumbered runs: ");
                    for(int i = 0; i > table.getNumRuns(); i++){
                        System.out.print(i + ":[" + table.getRuns().get(i) + "], ");
                    }
                    System.out.println();
                    printNumberedHand();

                    in = getInput("");
                    if(in.equals("END")) break;
                    var selections = in.split(",");
                    var selectionsint = new int[2];
                    for(int i = 0; i < 2; i++){
                        selectionsint[i] = Integer.parseInt(selections[i]);
                    }
                    var run = table.getRuns().get(selectionsint[1]);
                    var card = hand.getCard(selectionsint[0]);
                    table.addToRun(run, card, card.getDenomination(), hand);
                }while (true);
            }

            System.out.println("\tWould you like to play on existing sets?");
            in = getInput("\tEnter 'YES' if yes. All else will be considered no.");
            if(in.equals("YES")) {
                // Get additions to runs
                System.out.println("Enter the number of the card you are playing and the set you are playing on separated by commas. Enter after every selection.");
                System.out.println("When you are finished entering cards, type END.");
                do{
                    System.out.print("\t\tNumbered sets: ");
                    for(int i = 0; i > table.getNumSets(); i++){
                        System.out.print(i + ":[" + table.getSets().get(i) + "], ");
                    }
                    System.out.println();
                    printNumberedHand();

                    in = getInput("");
                    if(in.equals("END")) break;
                    var selections = in.split(",");
                    var selectionsint = new int[2];
                    for(int i = 0; i < 2; i++){
                        selectionsint[i] = Integer.parseInt(selections[i]);
                    }
                    var set = table.getSets().get(selectionsint[1]);
                    var card = hand.getCard(selectionsint[0]);
                    table.addToSet(set, card, hand);
                }while (true);
            }
        }

        printNumberedHand();
        in = getInput("Enter number of the card to discard, or nothing if no cards");
        System.out.println("You are discarding " + (in.equals("") ? "" : hand.getCard(Integer.parseInt(in))));

        endSomething(true);

        if(in.equals("")){
            return null;
        }

        return hand.getCard(Integer.parseInt(in));
    }

    public String getInput(String prompt){
        String in = null;
        System.out.println(prompt);
        do {
            try {
                in = reader.readLine();
            } catch (IOException e) {System.out.println("Oops and error occurred. Please try again.");}
        }while (in == null);
        return in;
    }

    /**
     * Called after the player's turn or a table state change to know if the player would like to double knock
     *
     * @return true if double knocking, false otherwise
     */
    @Override
    public boolean getDoubleKnockStatus() {
        if(PlayerUtil.maxPlayableCards(hand, table, numSets, numRuns, isDown) == hand.getNumCards()) {
            startSomething();
            String in = getInput("Enter 'K' to double knock");

            endSomething(false);

            return in.equals("K");
        }
        return false;
    }

    /**
     * Called at the beginning of the game
     */
    @Override
    public void startGame() {
        messageBacklog += "The game is starting\n";
    }

    /**
     * Called at the beginning of a round
     *
     * @param sets         The number of sets required for the round
     * @param runs         The number of runs required for the round
     * @param firstDiscard
     */
    @Override
    public void newRound(int sets, int runs, ShanghaiCard firstDiscard) {
        numRuns = runs;
        numSets = sets;
        messageBacklog += "A new round is starting." + "The discard is: " + firstDiscard + "\n";
    }

    /**
     * Called at the end of a round. All players must return from this function before
     * cards are recalled and the round ends
     *
     * @param roundScores The points gained by the players this round, where player x is at index x
     * @param totalScores The total scores for the game so far, where player x is at index x
     */
    @Override
    public void endRound(int[] roundScores, int[] totalScores) {
        startSomething();
        System.out.println("The round is ending. \n\tThe scores for the round were: " + Arrays.toString(roundScores) +
                "\n\tThe scores for the game so far are: " + Arrays.toString(totalScores));
        endSomething(true);
    }

    /**
     * Called at the end of a game. All players must return from this function before the game ends
     *
     * @param totalScores The scores at the end of the game, where player x is at index x
     */
    @Override
    public void endGame(int[] totalScores) {
        startSomething();
        System.out.println("The round is ending." +
                "\n\tThe final scores for the game are: " + Arrays.toString(totalScores));
        endSomething(true);
    }

    private void printNumberedHand(){
        System.out.print("Your numbered hand: ");
        for(int i = 0; i < hand.getNumCards(); i ++){
            System.out.print(i + ":" + hand.getCard(i) + ", ");
        }
        System.out.println();
    }

    private void startSomething(){
        System.out.println("Player " + playerNumber + " is next");
        getInput("Enter to continue");
        clearScreen();
        System.out.println("@ Player " + playerNumber);
        System.out.println("Game log since last action: \n" + messageBacklog);
        messageBacklog = "";
        System.out.println("Your hand: " + hand);
        System.out.println("Sets: " + numSets + "   Runs: " + numRuns);
    }

    private void startSomething(Table table){
        startSomething();
        System.out.println(table);
    }

    private void endSomething(boolean prompt){
        if(prompt) getInput("Enter to continue");
        clearScreen();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
