package acquireClient;

import java.util.HashSet;
import java.util.Set;

public class Hotel {
	private String hotelName;
	private Set<Tile> tiles;

	public Hotel(String hotelName) {
		this.hotelName = hotelName;
		tiles = new HashSet<Tile>();
	}
	
	public void addTiles(Set<Tile> hotelTiles) {
		this.tiles.addAll(hotelTiles);
	}

	public int getHotelSize() {
		return this.tiles.size();
	}
	
	public boolean isInHotel(Tile tile) {
		return tiles.contains(tile);
	}

	public String getHotelName() {
		return hotelName;
	}
}
