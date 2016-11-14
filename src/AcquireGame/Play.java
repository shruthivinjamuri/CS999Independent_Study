package AcquireGame;

import java.util.ArrayList;
import java.util.List;

public class Play {

	public static void main(String[] args) {
		Player player1 = new Player("Shruthi");
		Player player2 = new Player("Rakesh");
		Player player3 = new Player("Rasher");
		Player player4 = new Player("Renuka");
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		Game game = new Game(2, players);
		game.startGame();
	}


}
