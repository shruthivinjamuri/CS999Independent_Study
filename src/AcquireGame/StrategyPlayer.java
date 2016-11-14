package AcquireGame;

import java.util.List;
import java.util.Set;

public class StrategyPlayer {

	private IStrategy strategy;
	
	public StrategyPlayer(IStrategy strategy) {
		this.strategy = strategy;
	}
	
	public String pickTileToPlace(List<Cell> tiles) {
		return strategy.pickTileToPlace(tiles);
	}
	public String pickHotelToPlace(Set<Hotel> remainingHotels, Cell tile) {
		return strategy.pickHotelToPlace(remainingHotels, tile);
	}
	public String numOfSharesToBuy(Set<Hotel> activeHotels) {
		return strategy.numOfSharesToBuy(activeHotels);
	}

}
