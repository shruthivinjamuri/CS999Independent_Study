package AcquireGame;

import java.util.ArrayList;
import java.util.List;

public class Play {

	public static void main(String[] args) throws GameException {
		Player player1 = new Player("Shruthi");
		Player player2 = new Player("Rakesh");
		Player player3 = new Player("Rasher");
		Player player4 = new Player("Renu");
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		Game game = new Game(2, players);
		game.startGame();
	}

}
