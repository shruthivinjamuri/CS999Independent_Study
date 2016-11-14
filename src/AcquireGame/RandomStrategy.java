package AcquireGame;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomStrategy implements IStrategy {

	@Override
	public String pickTileToPlace(List<Cell> tiles) {
		Cell tileToPlace = tiles.get((int) ((Math.random() * 50) % tiles.size()));
		return "<place row=" + tileToPlace.getRow() + " column=" + tileToPlace.getCol() + ">  </place>";
	}

	@Override
	public String pickHotelToPlace(Set<Hotel> remainingHotels, Cell tile) {
		Hotel randomHotel = pickRandomHotel(remainingHotels);
		return "<place row=" + tile.getRow() + " column=" + tile.getCol() + " hotel=" + randomHotel.getHotelName()
				+ ">  </place>";
	}

	@Override
	public String numOfSharesToBuy(Set<Hotel> activeHotels) {
		int shares = AcquireStatistics.maxSharesToBuy;
		Set<Hotel> hotelsLeft = activeHotels;
		String output = "";
		while (hotelsLeft.size() != 0) {
			Hotel hotel = pickRandomHotel(hotelsLeft);
			int sharesInThisHotel = new Random().nextInt(shares) + 1;
			if(hotel.getSharesAvailable() >= sharesInThisHotel) {
				 output += "<share name=" + hotel.getHotelName() + " count=" + sharesInThisHotel + " />";
				 shares -= sharesInThisHotel;
			 } 
			if(shares == 0) {
				break;			 
			 }
			hotelsLeft.remove(hotel);
		}
		return output;
	}

	private Hotel pickRandomHotel(Set<Hotel> hotels) {
		int size = hotels.size();
		int item = new Random().nextInt(size);
		int i = 0;
		for (Hotel hotel : hotels) {
			if (i == item) {
				System.out.println("Random hotel returned is: " + hotel.getHotelName());
				return hotel;
			}
			i = i + 1;
		}
		return null;
	}

}
