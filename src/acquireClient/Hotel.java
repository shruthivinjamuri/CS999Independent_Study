package acquireClient;

import java.util.HashSet;
import java.util.Set;

import AcquireGame.AcquireStatistics;

public class Hotel {
	private String hotelName;
	private Set<Tile> tiles;
	private int sharesAvailable;

	public Hotel(String hotelName) {
		this.hotelName = hotelName;
		tiles = new HashSet<Tile>();
		this.sharesAvailable = AcquireStatistics.sharesPerHotel;
	}
	
	public void addTiles(Set<Tile> hotelTiles) {
		this.tiles.addAll(hotelTiles);
	}

	public int getHotelSize() {
		return this.tiles.size();
	}
	
	public double totalValueOfShares(int numOfShares) {
		return AcquireStatistics.singleShareValue(this.hotelName, getHotelSize()) * numOfShares;
	}
	
	public boolean isInHotel(Tile tile) {
		return tiles.contains(tile);
	}

	public String getHotelName() {
		return hotelName;
	}
	
	public void decrementSharesAvailable(int noOfShares) {
		this.sharesAvailable-=noOfShares;
	}
	
	public void incrementSharesAvailable(int noOfShares) {
		this.sharesAvailable+=noOfShares;
	}

	public int getSharesAvailable() {
		return sharesAvailable;
	}

}
