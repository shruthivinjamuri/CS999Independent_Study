package acquireClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AcquireGame.AcquireStatistics;

public class StrategyPlayer {

	private String strategyName;
	private IStrategy strategy;
	private String playerName;
	private double playerFund;
	private List<Tile> tiles;
	private Map<String, Integer> shares;

	public StrategyPlayer(String strategyName, String playerName) {
		this.strategyName = strategyName;
		this.playerName = playerName;
		this.playerFund = AcquireStatistics.playerFund;
		tiles = new ArrayList<Tile>();
		shares = new HashMap<String, Integer>();
		initializeStrategy();
	}

	private void initializeStrategy() {
		if (strategyName.equals("Ordered")) {
			strategy = new OrderedStrategy();
		} else if (strategyName.equals("Random")) {
			strategy = new RandomStrategy();
		}
	}

	public String pickTileToPlace() {
		return strategy.pickTileToPlace(tiles);
	}

	public String pickHotelToPlace(Set<String> remainingHotels, Tile tile) {
		return strategy.pickHotelToPlace(remainingHotels, tile);
	}

	public String numOfSharesToBuy(Set<Hotel> activeHotels, Map<String, Integer> shares) {
		return strategy.numOfSharesToBuy(activeHotels, shares);
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public double getPlayerFund() {
		return playerFund;
	}

	public void incrementPlayerFund(double amount) {
		this.playerFund += amount;
	}

	public void decrementPlayerFund(double amount) {
		this.playerFund -= amount;
	}
	
	public void updatePlayerFund(double amount) {
		playerFund = amount;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles.addAll(tiles);
	}
	
	public Tile getTileToPlace() {
		return strategy.getTileToPlace();
	}
	
	public void setShares(Map<String, Integer> playerShares) {
		shares.putAll(playerShares);
	}

	public Map<String, Integer> getShares() {
		return shares;
	}

}
