package AcquireGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Player {
	private String playerName;
	private double playerFund;
	private List<Cell> tiles;
	private Map<String, Integer> shares;
	private Set<Hotel> majorityShareHolder;
	private Set<Hotel> minorityShareHolder;

	public Player(String playerName) {
		this.playerName = playerName;
		this.playerFund = AcquireStatistics.playerFund;
		tiles = new ArrayList<Cell>();
		shares = new HashMap<String, Integer>();
		majorityShareHolder = new HashSet<Hotel>();
		minorityShareHolder = new HashSet<Hotel>();
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

	public List<Cell> getTiles() {
		return tiles;
	}

	public void setTiles(List<Cell> tiles) {
		this.tiles.addAll(tiles);
	}

	public Cell playTile() {
		return tiles.get((int) ((Math.random() * 50) % tiles.size()));
	}

	public void removeTile(Cell tile) {
		tiles.remove(tile);
	}

	public Hotel pickRandomHotel(Set<Hotel> hotels) {
		int size = hotels.size();
		int item = new Random().nextInt(size); 
		int i = 0;
		for(Hotel hotel : hotels)
		{
		    if (i == item){
		    	System.out.println("Random hotel returned is: " + hotel.getHotelName());
		        return hotel;}
		    i = i + 1;
		}
		return null;
	}

	public Set<Hotel> getMajorityShareHolder() {
		return majorityShareHolder;
	}

	public Set<Hotel> getMinorityShareHolder() {
		return minorityShareHolder;
	}

	public Map<String, Integer> getShares() {
		return shares;
	}

	public boolean canBuyShares() {
		return playerFund > 0.0;
	}

	public boolean hasDeadTiles() {
		for (Cell tile : tiles) {
			if (tile.isDeadTile()) {
				return true;
			}
		}
		return false;
	}

	// if(valueForShares > playerFunds){
	// System.out.println("Player has no enough funds");
}
