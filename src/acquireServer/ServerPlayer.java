package acquireServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import AcquireGame.Cell;
import AcquireGame.Hotel;

public class ServerPlayer {

	private String playerName;
	private double playerFund;
	private List<Cell> tiles;
	private Map<String, Integer> shares;

	public ServerPlayer(String playerName, double playerFund, Map<String, Integer> shares) {
		this.playerName = playerName;
		this.playerFund = playerFund;
		tiles = new ArrayList<Cell>();
		shares = this.shares;
	}
	
	public void setTiles(List<Cell> tiles) {
		this.tiles.addAll(tiles);
	}

	public String getPlayerName() {
		return playerName;
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
	
	public Map<String, Integer> getShares() {
		return shares;
	}
	
	public void removeTile(Cell tile) {
		tiles.remove(tile);
	}

	public String playerTag() {
		String playerString = "player name=" + playerName + " cash=" + playerFund +">";
		for(String hotelName: shares.keySet()) {
			playerString += "<share name=" + hotelName + " count=" + shares.get(hotelName) + " />";
		}
		for(Cell tile: tiles) {
			playerString += "<tile column=" + tile.getCol() + " row=" + tile.getRow() + " />";
		}
		playerString += "</player>";
		return playerString;
	}
}
