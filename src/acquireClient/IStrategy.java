package acquireClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IStrategy {
		public String pickTileToPlace(List<Tile> tiles);
		public String pickHotelToPlace(Set<String> remainingHotels, Tile tile);
		public String numOfSharesToBuy(Set<Hotel> activeHotels, Map<String, Integer> shares);
		public Tile getTileToPlace();
}
