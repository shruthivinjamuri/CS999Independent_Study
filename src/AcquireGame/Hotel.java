package AcquireGame;

import java.util.HashSet;
import java.util.Set;

public class Hotel {
	private String hotelName;
	private Set<Cell> hotelTiles;
	private int sharesAvailable;

	public Hotel(String hotelName) throws GameException {
		setName(hotelName);
		this.hotelTiles = new HashSet<Cell>();
		this.sharesAvailable = AcquireStatistics.sharesPerHotel;
	}

	public int getHotelSize() {
		return this.hotelTiles.size();
	}
	
	public boolean setName(String name) throws GameException {
		if(AcquireStatistics.isValidHotelName(name)){
			this.hotelName = name;
			return true;
		}
		else
			throw new GameException("Invalid hotel name");
	}

	public void expandHotel(Cell newTile) {
		this.hotelTiles.add(newTile);
	}

	public double valueForShares(int numOfShares) {
		double valueForShares = -1.0;
		if (this.sharesAvailable < 1) {
			System.out.println("No shares available for " + this.hotelName);
		} else if (this.sharesAvailable < numOfShares) {
			System.out.println("Requested number of shares unavailable");
		} else {
			valueForShares = AcquireStatistics.singleShareValue(this.hotelName, getHotelSize()) * numOfShares;
		}
		return valueForShares;
	}
	
	public double totalValueOfShares(int numOfShares) {
		return AcquireStatistics.singleShareValue(this.hotelName, getHotelSize()) * numOfShares;
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
	
	public boolean equals(Hotel hotel){
		if (this.hotelName.equals(hotel.hotelName) && this.hotelTiles.size() == hotel.getHotelSize()) {
			for(Cell t : this.hotelTiles){
				if(!hotel.isInHotel(t)){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public String getHotelTag() {
		String hotelTag = "<hotel name=" + hotelName +">";
		for(Cell tile: hotelTiles) {
			hotelTag += tile.getTileTag();
		}
		hotelTag += "</hotel>";
		return hotelTag;
	}
	
	public String getShareTag() {
		return "<share name=" + hotelName + "count=" + sharesAvailable + " />";
	}
}
