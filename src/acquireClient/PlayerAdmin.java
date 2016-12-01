package acquireClient;

import java.util.List;
import java.util.Set;

public class PlayerAdmin {

	private int turn;
	private int noOfPlayers;
	private List<StrategyPlayer> players;
	private Board currentClientState;

	public PlayerAdmin(int noOfPlayers) {
		turn = 0;
		this.noOfPlayers = noOfPlayers;
		// initialize players
		initializePlayers();
	}

	public void setCurrentClientState(Board clientState) {
		currentClientState = clientState;
	}

	private void initializePlayers() {
		StrategyPlayer player1 = new StrategyPlayer("Ordered", "Rakesh");
		StrategyPlayer player2 = new StrategyPlayer("Ordered", "Shruthi");
		StrategyPlayer player3 = new StrategyPlayer("Random", "Renu");
		StrategyPlayer player4 = new StrategyPlayer("Random", "Rasher");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
	}

	public String playTurn() {
		String currentTileString = "<turn>";
		Tile tileToPlace = players.get(turn).getTileToPlace();
		Set<String> remainingHotels = currentClientState.remainingHotels();
		if (currentClientState.isFounding(tileToPlace)) {
			currentTileString += players.get(turn).pickHotelToPlace(remainingHotels, tileToPlace);
		} else {
			currentTileString += players.get(turn).pickTileToPlace();
		}
		
		currentTileString += players.get(turn).numOfSharesToBuy(currentClientState.getActiveHotels(), currentClientState.getShares());
		turn++;
		if(turn >= noOfPlayers) {
			turn = 0;
		}
		return currentTileString;
	}
	
	public String setup() {
		String setupString = "<setup>";
		for(StrategyPlayer player: players) {
			setupString += "<player name=" + player.getPlayerName() + " cash=" + player.getPlayerFund() + ">";
			for(String hotel: player.getShares().keySet()) {
				setupString += "share name=" + hotel + " count=" + player.getShares().get(hotel) + " />";
			}
			setupString += "</player>";
		}
		setupString += "</setup>";
		return setupString;
	}

	public int getNoOfPlayers() {
		return noOfPlayers;
	}

	public List<StrategyPlayer> getPlayers() {
		return players;
	}
}
