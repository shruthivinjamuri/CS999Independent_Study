package AcquireGame;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
	private String hotelName;
	private List<Cell> hotelTiles;
	private int hotelSize;
	private int sharesAvailable;

	public Hotel(String hotelName) {
		this.hotelName = hotelName;
		this.hotelTiles = new ArrayList<Cell>();
		getHotelSize();
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
			valueForShares = AcquireStatistics.singleShareValue(this.hotelName, this.hotelSize) * numOfShares;
		}
		return valueForShares;
	}
	
	public boolean isSafe() {
		return (this.hotelSize >= 11);
	}

	public int getSharesAvailable() {
		return sharesAvailable;
	}

	public void setSharesAvailable(int sharesAvailable) {
		this.sharesAvailable = sharesAvailable;
	}
	
	public boolean isInHotel(Cell tile) {
		for(Cell hotelTile: hotelTiles) {
			if(tile.equals(hotelTile)){
				return true;
			}
		}
		return false;
	}
	
	public void findingHotel() {
		
	}

	public String getHotelName() {
		return hotelName;
	}
}
