package AcquireGame;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OrderedStrategy implements IStrategy {
	
	
	@Override
	public String pickTileToPlace(List<Cell> tiles) {
		Cell[] playerTiles = new Cell[tiles.size()];
		Arrays.sort(tiles.toArray(playerTiles));
		return "<place row="+playerTiles[0].getRow()+ " column=" + playerTiles[0].getCol()+ ">  </place>";
	}

	@Override
	public String pickHotelToPlace(Set<Hotel> remainingHotels, Cell tile) {
		String[] sortedRemainingHotels = sortHotels(remainingHotels);		
		return "<place row=" + tile.getRow()+ " column=" + tile.getCol() + " hotel=" + sortedRemainingHotels[0] +">  </place>";
	}

	@Override
	public String numOfSharesToBuy(Set<Hotel> activeHotels) {
		String[] sortedActiveHotels = sortHotels(activeHotels);
		String output = "";
		int idx = 0;
		int sharesToBuy = AcquireStatistics.maxSharesToBuy;
		 for(Hotel activeHotel: activeHotels) {
			 if(activeHotel.getHotelName().equals(sortedActiveHotels[idx])) {
				 if(activeHotel.getSharesAvailable() >= sharesToBuy) {
					 output += "<share name=" + activeHotel.getHotelName() + " count=" + sharesToBuy + " />";
					 sharesToBuy = 0;
				 } else if (activeHotel.getSharesAvailable() > 0 && activeHotel.getSharesAvailable() < sharesToBuy) {
					 output += "<share name=" + activeHotel.getHotelName() + " count=" + sharesToBuy + " />";
					 sharesToBuy -= activeHotel.getSharesAvailable();
				 }
			 }
			 if(sharesToBuy == 0) {
				 return output;
			 }
		 }
		
		return output;
	}
	
	private String[] sortHotels(Set<Hotel> hotels) {
		String[] sortedHotels = new String[hotels.size()];
		int i = 0;
		for(Hotel hotel: hotels) {
			sortedHotels[i] = hotel.getHotelName(); 
		}
		 Arrays.sort(sortedHotels);
		 return sortedHotels;
	}
	
}
