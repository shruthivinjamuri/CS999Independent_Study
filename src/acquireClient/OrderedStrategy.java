package acquireClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import AcquireGame.AcquireStatistics;

public class OrderedStrategy implements IStrategy {
	
	private Tile tileToPlace;
	
	@Override
	public String pickTileToPlace(List<Tile> tiles) {
		Tile[] playerTiles = new Tile[tiles.size()];
		Arrays.sort(tiles.toArray(playerTiles));
		tileToPlace = tiles.get(0);
		return "<place row=" + playerTiles[0].getRow() + " column=" + playerTiles[0].getCol() + ">  </place>";
	}

	@Override
	public String pickHotelToPlace(Set<String> remainingHotels, Tile tile) {
		Set<String> sortedRemainingHotels = new TreeSet<String>();
		sortedRemainingHotels.addAll(remainingHotels);
		return "<place row=" + tile.getRow() + " column=" + tile.getCol() + " hotel="
				+ sortedRemainingHotels.iterator().next() + ">  </place>";
	}

	@Override
	public String numOfSharesToBuy(Set<Hotel> activeHotels, Map<String, Integer> shares) {
		Set<String> sortedActiveHotels = new TreeSet<String>();
		for (Hotel hotel : activeHotels) {
			sortedActiveHotels.add(hotel.getHotelName());
		}
		int sharesToBuy = AcquireStatistics.maxSharesToBuy;
		String output = "";

		while (sortedActiveHotels.iterator().hasNext()) {
			String currentActiveHotel = sortedActiveHotels.iterator().next();

			if (shares.get(currentActiveHotel) >= sharesToBuy) {
				output += "<share name=" + currentActiveHotel + " count=" + sharesToBuy + " />";
				return output;
			} else if (shares.get(currentActiveHotel) > 0 && shares.get(currentActiveHotel) < sharesToBuy) {
				output += "<share name=" + currentActiveHotel + " count=" + sharesToBuy + " />";
				sharesToBuy -= shares.get(currentActiveHotel);
			}

		}

		return output;
	}
	
	public Tile getTileToPlace() {
		return tileToPlace;
	}
}
