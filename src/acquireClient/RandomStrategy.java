package acquireClient;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import AcquireGame.AcquireStatistics;

public class RandomStrategy implements IStrategy {

	private Tile tileToPlace;

	@Override
	public String pickTileToPlace(List<Tile> tiles) {
		tileToPlace = tiles.get((int) ((Math.random() * 50) % tiles.size()));
		return "<place row=" + (char)(tileToPlace.getRow() + 65) + " column=" + (tileToPlace.getCol() + 1) + ">  </place>";
	}

	@Override
	public String pickHotelToPlace(Set<String> remainingHotels, Tile tile) {
		String randomHotel = pickRandomHotel(remainingHotels);
		return "<place row=" + (char)(tile.getRow() + 65) + " column=" + (tile.getCol() + 1) + " hotel=" + randomHotel + ">  </place>";
	}

	@Override
	public String numOfSharesToBuy(Set<Hotel> activeHotels, Map<String, Integer> shares) {
		int sharesToBuy = AcquireStatistics.maxSharesToBuy;
		Set<String> hotelsLeft = new HashSet<>();
		for (Hotel hotel : activeHotels) {
			hotelsLeft.add(hotel.getHotelName());
		}
		String output = "";
		while (hotelsLeft.size() != 0) {
			String hotel = pickRandomHotel(hotelsLeft);
			int sharesInThisHotel = new Random().nextInt(sharesToBuy) + 1;
			if (shares.get(hotel) >= sharesInThisHotel) {
				output += "<share name=" + hotel + " count=" + sharesInThisHotel + " />";
				sharesToBuy -= sharesInThisHotel;
			}
			if (sharesToBuy == 0) {
				break;
			}
			hotelsLeft.remove(hotel);
		}
		return output;
	}

	private String pickRandomHotel(Set<String> hotels) {
		int size = hotels.size();
		int item = new Random().nextInt(size);
		int i = 0;
		for (String hotel : hotels) {
			if (i == item) {
				return hotel;
			}
			i = i + 1;
		}
		return null;
	}

	@Override
	public Tile getTileToPlace() {
		return tileToPlace;
	}

}
