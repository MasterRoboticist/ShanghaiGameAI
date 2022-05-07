package Shanghai.Table;

import Shanghai.Player.Player;
import Shanghai.*;
import Deck.*;

import java.util.ArrayList;

public class Shanghai {
    private final int JOKERSPERDECK = 0; //TODO: change this back to 2
    private final int CARDSINHAND = 11;
    private final int DECKTOP = 0;
    private final int NUMBUYCARDS = 2;
    private final int MAXBUYS = 2;
    private final int SHANGHAIBONUS = -20;
    private final int FIRSTDOWNBONUS = -10;
    TablePlayer[] players;
    int numDecks;
    Deck<ShanghaiCard> deck;
    Deck<ShanghaiCard> discardDeck = new Deck<ShanghaiCard>();
    Table table;
    int numSets;
    int numRuns;
    private int currPlayerNum;

    public Shanghai(ArrayList<Player> players, ArrayList<Hand> hands, int numDecks){
        assert players.size() <= hands.size();

        this.players = new TablePlayer[players.size()];
        for(int i = 0; i < players.size(); i++) {
            hands.get(i).empty();
            this.players[i] = new TablePlayer(players.get(i), hands.get(i));
        }

        this.numDecks = numDecks; // recommended players:decks = 1:0 2:1 3:2 4:3 5:3 6:3 7:4

        table = new Table(hands);

        currPlayerNum = (int)(Math.random()*this.players.length);
    }

    public void startGame(){
        // start game for all players
        for(var p: players) p.player.startGame();
    }

    public void endGame(){
        // end game for all players
        for(var p: players) p.player.endGame(getAllPoints());
    }

    public void startRound(int numSets, int numRuns){
        this.numRuns = numRuns;
        this.numSets = numSets;

        resetDeck();

        // reset hands
        for(var p: players) p.hand.clear();

        // deal the cards
        for(var p: players) p.hand.addCards(deck.dealMultiple(CARDSINHAND));

        // deal discard
        discardDeck.addCard(deck.deal(), 0);

        // declare new round for all players
        for(var p: players) p.player.newRound(numSets, numRuns, discardDeck.peek(DECKTOP));
    }

    public void endRound(){
        // count hand points
        var points = new int[players.length];
        for(int i = 0; i< players.length; i++) points[i] = players[i].hand.getHandCost() + players[i].roundMinusPoints;

        // add player points to total
        for(int i = 0; i< players.length; i++) players[i].addPoints(points[i]);

        // reset table
        for(var p: players) p.reset();
        table.reset();

        // end round for all players
        for(var p: players) p.player.endRound(points, getAllPoints());
    }

    public boolean play() { //TODO: reshuffle discard
        System.out.println(discardDeck);
        currPlayerNum = (currPlayerNum +1)%players.length;
        var currPlayer = players[currPlayerNum];
        var currDiscard = discardDeck.peek(DECKTOP);

        // remember the number of cards the player has
        int numCardsStart = players[currPlayerNum].hand.getNumCards();
        int numSetsStart = table.getNumSets();
        int numRunsStart = table.getNumRuns();

        // declare player turn to all players
        for(var p: players) p.player.playerStartedTurn(currPlayerNum, numCardsStart);

        // get current player pickup intention
        var choice = currPlayer.player.getDrawChoice(currDiscard);
        ShanghaiCard currPlayerDraw;
        if(choice == Player.DISCARD){
            currPlayerDraw = discardDeck.deal();

            //declare discard picked to all players
            for(var p: players) p.player.discardPickedUp(currDiscard, currPlayerNum);
        }
        else {
            currPlayerDraw = deck.deal();

            // get other player buy intention if not picked up
            TablePlayer buyer = getBuyer(discardDeck.peek(DECKTOP));
            if(buyer != null){
                // declare buy to all players
                for (var p : players)
                    p.player.discardBought(discardDeck.peek(DECKTOP), buyer.player.playerNumber);
                // grant buy
                giveBuy(buyer);
            }
        }

        // get player to take turn
        currPlayer.hand.addCard(currPlayerDraw);
        var nextDiscard = currPlayer.player.takeTurn(currPlayerDraw, table);

        discard(nextDiscard, currPlayer);
        if(currPlayer.hand.getNumCards() == 0) {
            // Make sure double knocked
            if(!currPlayer.doubleknock)
                throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum +
                        " did not double knock before ending a round");

            // Check for Shanghai
            if (!currPlayer.isDown)
                currPlayer.roundMinusPoints += SHANGHAIBONUS;
        }


        // check if they went down
        if(wentDown(currPlayer, numCardsStart, numRunsStart, numSetsStart)){
            //First down bonus and shanghai bonus
            if(!someoneDown())
                currPlayer.roundMinusPoints += FIRSTDOWNBONUS;

            // declare down to all players
            currPlayer.isDown = true;
            for(var p: players) p.player.playerWentDown(table, currPlayerNum);

        }

        // check if played cards
        if(currPlayer.hand.getNumCards() < numCardsStart) {
            if(!currPlayer.isDown){
                throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + " player cards before " +
                        "going down. That is not allowed.");
            }
            if(table.getTableJokers() > 0)
                throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + " did not play the joker " +
                        "they replaced during their turn.");
            // declare table change to all players
            declareTableChange();
        }

        // declare discard to all players
        for(var p: players) p.player.cardDiscarded(nextDiscard, currPlayerNum);

        // end turn for all players
        for(var p: players) p.player.playerEndedTurn(currPlayerNum, currPlayer.hand.getNumCards());

        // check for round end
        return currPlayer.hand.getNumCards() != 0;
    }

    public int[] getAllPoints(){
        var points = new int[players.length];
        for(int i = 0; i < players.length; i++) points[i] = players[i].points;
        return points;
    }

    private void resetDeck(){
        deck = new Deck<ShanghaiCard>(
                DeckUtil.getStandardDeck(numDecks, JOKERSPERDECK * numDecks, ShanghaiCard.getCardFactory()));
        deck.shuffle();
        discardDeck.empty();
    }

    private void giveBuy(TablePlayer player){
        var cards = new ArrayList<ShanghaiCard>();
        cards.add(discardDeck.deal());
        cards.addAll(deck.dealMultiple(NUMBUYCARDS));

        player.hand.addCards(cards);
        player.buys++;
        player.player.giveBuy(cards);
    }

    private TablePlayer getBuyer(ShanghaiCard discard){
        for(int i = currPlayerNum + 1; i < currPlayerNum + 1 + players.length; i++) {
            int playernum = i%players.length;
            if (players[playernum].buys < MAXBUYS){
                if (players[playernum].player.getBuyIntention(discard)) {
                    return players[playernum];
                }
            }
        }
        return null;
    }

    private void discard(ShanghaiCard nextDiscard, TablePlayer currPlayer) {
        //Remove the card from their hand
        if(nextDiscard != null && currPlayer.hand.find(nextDiscard) == -1)
            throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + " tried to discard a card that "+
                    "isn't in their hand");
        currPlayer.hand.deal(currPlayer.hand.find(nextDiscard));

        // if does not return a card, check that they have no more cards
        if(nextDiscard == null){
            if(currPlayer.hand.getNumCards() != 0){
                throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + "failed to discard a card " +
                        "when they had " + currPlayer.hand.getNumCards() + " cards left in their hand");
            }
        }
        // if they do return a card, check that they have cards in their hand
        else if (currPlayer.hand.getNumCards() == 0)
            throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + " tried to discard their last " +
                    "card. That is not allowed. You must get rid of all cards without discarding to win.");

        // add discard to discard pile
        discardDeck.addCard(nextDiscard, 0);
    }

    private boolean wentDown(TablePlayer currPlayer, int numCardsStart, int numRunsStart, int numSetsStart){
        if(!currPlayer.isDown && currPlayer.hand.getNumCards() < numCardsStart) {
            // ensure legal down for round
            if (table.getNumRuns() != numRunsStart + numRuns)
                throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + " should have gone down " +
                        "with " + numRuns + " runs but went down with " + (table.getNumRuns() - numRunsStart));
            if (table.getNumSets() != numSetsStart + numSets)
                throw new CheaterCheaterPumpkinEaterException("Player " + currPlayerNum + " should have gone down " +
                        "with " + numSets + " sets but went down with " + (table.getNumSets() - numSetsStart));
            return true;
        }
        else return false;

    }

    private boolean someoneDown(){
        for(var p: players) if(p.isDown) return true;
        return false;
    }

    private void declareTableChange(){

        for(var p: players) {
            // declare table change
            p.player.tableChanged(table, currPlayerNum);

            // update double knock status
            p.doubleknock = p.player.getDoubleKnockStatus();
        }
    }

    private static class TablePlayer {
        Player player;
        Hand hand;
        boolean isDown = false;
        int points = 0;
        int roundMinusPoints = 0;
        int buys = 0;
        boolean doubleknock = false;

        TablePlayer(Player player, Hand hand){
            this.player = player;
            this.hand = hand;
        }

        void reset(){
            isDown = false;
            roundMinusPoints = 0;
            buys = 0;
            doubleknock = false;
        }

        void addPoints(int points){
            this.points += points;
        }
    }
}
