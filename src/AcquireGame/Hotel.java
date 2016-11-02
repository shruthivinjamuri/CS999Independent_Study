package AcquireGame;

import java.util.HashSet;
import java.util.Set;

public class Hotel {
	private String hotelName;
	private Set<Cell> hotelTiles;
	private int sharesAvailable;

	public Hotel(String hotelName) {
		this.hotelName = hotelName;
		this.hotelTiles = new HashSet<Cell>();
		this.sharesAvailable = AcquireStatistics.sharesPerHotel;
	}

	public int getHotelSize() {
		return this.hotelTiles.size();
	}

	public void expandHotel(Cell newTile) {
		this.hotelTiles.add(newTile);
	}

	public double valueForShares(int numOfShares) {
		double valueForShares = -1.0;
		if (this.sharesAvailable < 1) {
			System.out.println("No shares available for" + this.hotelName);
		} else if (this.sharesAvailable < numOfShares) {
			System.out.println("Requested number of shares unavailable");
		} else {
			valueForShares = AcquireStatistics.singleShareValue(this.hotelName, getHotelSize()) * numOfShares;
		}
		return valueForShares;
	}
	
	public boolean isSafe() {
		return (this.hotelTiles.size() >= 11);
	}

	public int getSharesAvailable() {
		return sharesAvailable;
	}

	public void decrementSharesAvailable(int noOfShares) {
		this.sharesAvailable-=noOfShares;
	}
	
	public void incrementSharesAvailable(int noOfShares) {
		this.sharesAvailable+=noOfShares;
	}
	
	public boolean isInHotel(Cell tile) {
		return hotelTiles.contains(tile);
	}
	
	public String getHotelName() {
		return hotelName;
	}

	public Set<Cell> getHotelTiles() {
		return hotelTiles;
	}
	
	public boolean defunctHotel() {
		return hotelTiles.removeAll(hotelTiles);
	}
}
