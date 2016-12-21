package acquireClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MinMaxStrategy implements IStrategy {

	public static PlayerAdmin currentState;
	private Tile tileToPlace;

	@Override
	public String pickTileToPlace(List<Tile> tiles) {
		for (Tile tile : tiles) {
			if (currentState.getCurrentClientState().isTileOnBoard(tile)) {
				tileToPlace = tile;
			}
		}
		return "<place row=" + tileToPlace.getRow() + " column=" + tileToPlace.getCol() + ">  </place>";
	}

	@Override
	public String pickHotelToPlace(Set<String> remainingHotels, Tile tile) {
		String hotelPicked = null;
		for (Hotel hotel : currentState.getCurrentClientState().getActiveHotels()) {
			if (!remainingHotels.contains(hotel.getHotelName())) {
				hotelPicked = hotel.getHotelName();
			}
		}
		return "<place row=" + tile.getRow() + " column=" + tile.getCol() + " hotel=" + hotelPicked + ">  </place>";
	}

	@Override
	public String numOfSharesToBuy(Set<Hotel> activeHotels, Map<String, Integer> shares) {
		String output = "";
		for (Hotel hotel : activeHotels) {
			if (currentState.getCurrentClientState().getShares().get(hotel.getHotelName()) != shares
					.get(hotel.getHotelName())) {
				int sharesToBuy = currentState.getCurrentClientState().getShares().get(hotel.getHotelName())
						- shares.get(hotel.getHotelName());
				output += "<share name=" + hotel.getHotelName() + " count=" + sharesToBuy + " />";
			}
		}
		return output;
	}

	@Override
	public Tile getTileToPlace() {
		return tileToPlace;
	}

}
