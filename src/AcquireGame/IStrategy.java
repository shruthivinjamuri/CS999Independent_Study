package AcquireGame;

import java.util.List;
import java.util.Set;

public interface IStrategy {
		public String pickTileToPlace(List<Cell> tiles);
		public String pickHotelToPlace(Set<Hotel> remainingHotels, Cell tile);
		public String numOfSharesToBuy(Set<Hotel> activeHotels);
}
