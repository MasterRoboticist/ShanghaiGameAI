package Shanghai;
import Deck.*;
import Shanghai.Player.Human;
import Shanghai.Player.Player;
import Shanghai.Table.Hand;
import Shanghai.Table.Shanghai;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        var players = new ArrayList<Player>();
        var hands = new ArrayList<Hand>();
        hands.add(new Hand());
        hands.add(new Hand());
        players.add(new Human(0, 2, hands.get(0).getHand()));
        players.add(new Human(1, 2, hands.get(1).getHand()));

        Shanghai game = new Shanghai(players, hands, 2);

        game.startGame();

        // 2 things rounds
        int things = 2;
        int rounds = 0;
        int sets = 2;
        int runs = 0;
        while(rounds < 3) {
            game.startRound(sets, runs);

            while(game.play());

            game.endRound();
            if(runs == things){
                things ++;
                runs = 0;
                sets = things;
            }
            else{
                runs ++;
                sets --;
            }
            rounds++;
        }
        game.endGame();
    }
}
