package acquireClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

import AcquireGame.AcquireStatistics;
import AcquireGame.GameException;

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

	public PlayerAdmin(int turn, int noOfPlayers, List<StrategyPlayer> players) {
		this.turn = turn;
		this.noOfPlayers = noOfPlayers;
		this.players = players;
	}

	public PlayerAdmin(PlayerAdmin pa) {
		this.turn = pa.getTurn();
		this.noOfPlayers = pa.getNoOfPlayers();
		this.players = pa.getPlayers();
		this.currentClientState = pa.getCurrentClientState();
	}

	public void setCurrentClientState(Board clientState) {
		currentClientState = clientState;
	}

	private void initializePlayers() {
		StrategyPlayer player1 = new StrategyPlayer("Ordered", "Rakesh");
		StrategyPlayer player2 = new StrategyPlayer("Ordered", "Shruthi");
		StrategyPlayer player3 = new StrategyPlayer("Random", "Renu");
		StrategyPlayer player4 = new StrategyPlayer("Random", "Rasher");
		StrategyPlayer player5 = new StrategyPlayer("MinMax", "Narsi");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
	}

	public void buyShares(Map<String, Integer> sharesToBuy) {
		int shares = AcquireStatistics.maxSharesToBuy;
		for (String hotel : sharesToBuy.keySet()) {
			shares = sharesToBuy.get(hotel);
			Double sharesValue = currentClientState.getShareValue(hotel, shares);
			if (players.get(turn).getPlayerFund() >= sharesValue) {
				players.get(turn).decrementPlayerFund(sharesValue);
				players.get(turn).getShares().put(hotel, players.get(turn).getShares().containsKey(hotel)
						? players.get(turn).getShares().get(hotel) + shares : shares);
				currentClientState.decrementShares(shares, hotel);
			}
		}

	}

	public String playTurn() throws GameException {
		String currentTileString = "<turn>";
		if (players.get(turn).getStrategyName().equals("MinMax")) {
			MinMaxAlgorithm minMaxObj = new MinMaxAlgorithm();
			PlayerAdmin pickedState = minMaxObj.chooseState(this, "evaluation2");
			MinMaxStrategy.currentState = pickedState;
		}

		Tile tileToPlace = players.get(turn).getTileToPlace();
		Set<String> remainingHotels = currentClientState.remainingHotels();
		if (currentClientState.isFounding(tileToPlace)) {
			currentTileString += players.get(turn).pickHotelToPlace(remainingHotels, tileToPlace);
		} else {
			currentTileString += players.get(turn).pickTileToPlace();
		}

		currentTileString += players.get(turn).numOfSharesToBuy(currentClientState.getActiveHotels(),
				currentClientState.getShares());
		turn++;
		if (turn >= noOfPlayers) {
			turn = 0;
		}
		return currentTileString;
	}

	public String setup() {
		String setupString = "<setup>";
		for (StrategyPlayer player : players) {
			setupString += "<player name=" + player.getPlayerName() + " cash=" + player.getPlayerFund() + ">";
			for (String hotel : player.getShares().keySet()) {
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

	public int getTurn() {
		return turn;
	}

	public Board getCurrentClientState() {
		return currentClientState;
	}
}
