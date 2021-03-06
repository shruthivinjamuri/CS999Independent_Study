package AcquireGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AcquireGame.AcquireStatistics.TilePlacementType;

public class Board {
	private Cell[][] board;
	private Set<Hotel> activeHotels;

	public Board() {
		this.board = new Cell[AcquireStatistics.rows][AcquireStatistics.columns];
		initializeBoard();
		activeHotels = new HashSet<Hotel>();
	}

	public void initializeBoard() {
		for (int rowIdx = 0; rowIdx < AcquireStatistics.rows; rowIdx++) {
			for (int colIdx = 0; colIdx < AcquireStatistics.columns; colIdx++) {
				try {
					board[rowIdx][colIdx] = new Cell(AcquireStatistics.rowMapping().get(rowIdx + 1), colIdx + 1);
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	public void addHotel(Hotel hotel) {
		activeHotels.add(hotel);
	}

	public void markCell(Cell Tile) {
			board[Tile.getRow()][Tile.getCol()].setMarked(true);
	}

	public Set<Hotel> getActiveHotels() {
		return activeHotels;
	}

	public boolean isAdjascentTile(Cell tile) {
		int row = tile.getRow();
		int col = tile.getCol();
		if (board[row - 1][col].isMarked() || board[row][col - 1].isMarked() || board[row][col + 1].isMarked()
				|| board[row + 1][col].isMarked()) {
			return true;
		}
		return false;
	}

	public List<Cell> adjascentTiles(Cell tile) {
		int row = tile.getRow();
		int col = tile.getCol();
		List<Cell> adjTiles = new ArrayList<Cell>();
		if ((row - 1 > 0) && (board[row - 1][col].isMarked())) {
			adjTiles.add(board[row - 1][col]);
		}
		if ((col - 1 > 0) && (board[row][col - 1].isMarked())) {
			adjTiles.add(board[row][col - 1]);
		}
		if ((col + 1 < AcquireStatistics.columns) && (board[row][col + 1].isMarked())) {
			adjTiles.add(board[row][col + 1]);
		}
		if ((row + 1 < AcquireStatistics.rows) && (board[row + 1][col].isMarked())) {
			adjTiles.add(board[row + 1][col]);
		}
		return adjTiles;
	}

	public List<Cell> drawTiles(int noOfTiles) {
		List<Cell> tiles = new ArrayList<Cell>();
		int availableTileCount = 0;
		for (int rowIdx = 0; rowIdx < AcquireStatistics.rows; rowIdx++) {
			for (int colIdx = 0; colIdx < AcquireStatistics.columns; colIdx++) {
				if(board[rowIdx][colIdx].isTileAvailable()) {
					availableTileCount++;
				}
			}
		}
		if(noOfTiles > availableTileCount) return tiles;
		while (tiles.size() < noOfTiles) {
			Cell tile = board[(int) ((Math.random() * 50) % 9)][(int) ((Math.random() * 50) % 12)];
			if (tile.isTileAvailable()) {
				tiles.add(tile);
				tile.setTileAvailable(false);
			}
		}
		return tiles;
	}

	public boolean isSingleTileOnBoard(Cell tile) {
		int row = tile.getRow();
		int col = tile.getCol();
		if (((row - 1 > 0) && (board[row - 1][col].isMarked())) || ((col - 1 > 0) && (board[row][col - 1].isMarked())) || 
				((col + 1 < AcquireStatistics.columns) && (board[row][col + 1].isMarked()))
				|| ((row + 1 < AcquireStatistics.rows) && (board[row + 1][col].isMarked()))) {
			return false;
		}
		return true;
	}

	public TilePlacementType placeTile(Cell tile) {
		if (isSingleton(tile)) {
			return TilePlacementType.Singleton;
		}
		List<Cell> adjTiles = adjascentTiles(tile);
		if(isGrowing(tile, adjTiles)) {
			return TilePlacementType.Growing;
		}
		if(isFounding(tile, adjTiles)) {
			return TilePlacementType.Founding;
		}
		if(isMerging(tile, adjTiles)) {
			return TilePlacementType.Merging;
		}
		if(isImpossible(tile, adjTiles)) {
			return TilePlacementType.Impossible;
		}
		return TilePlacementType.Error;
	}

	private boolean isSingleton(Cell tile) {
		if (isSingleTileOnBoard(tile)) {
			markCell(tile);
			return true;
		}
		return false;
	}

	private boolean isGrowing(Cell tile, List<Cell> adjTiles) {
		for (Hotel hotel : activeHotels) {
			int sameHotelCount = 0;
			for (Cell adjTile : adjTiles) {
				if (hotel.isInHotel(adjTile)) {
					sameHotelCount++;
				}
			}
			if (sameHotelCount == adjTiles.size()) {
				markCell(tile);
				hotel.expandHotel(tile);
				return true;
			}
		}
		return false;
	}
	
	private boolean isMerging(Cell tile, List<Cell> adjTiles) {
		if(!isGrowing(tile, adjTiles) && adjTiles.size() > 1) {
			if (!validateDeadTile(tile, adjTiles)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isImpossible(Cell tile, List<Cell> adjTiles) {
		if(isFounding(tile, adjTiles) && activeHotels.size() >= 7) {
				return true;
		}
		if(validateDeadTile(tile, adjTiles)) {
			tile.setDeadTile(true);
			return true;
		}
		return false;
	}
	
	private boolean isFounding(Cell tile, List<Cell> adjTiles) {
		if(adjTiles.size() < 1) return false;
		for (Cell adjTile : adjTiles) {
			if(!isSingleTileOnBoard(adjTile)) {
				return false;
			}
		}
		return activeHotels.size() < 7;
	}

	private boolean validateDeadTile(Cell tile, List<Cell> adjTiles) {
		Set<Hotel> hotels = adjacentHotels(adjTiles);
		int safeHotelCount = 0;
		for (Hotel hotel : hotels) {
			if (hotel.getHotelSize() >= 11) {
				safeHotelCount++;
			}
		}

		return (safeHotelCount == hotels.size());
	}
	
	public Set<Hotel> adjacentHotels(List<Cell> adjTiles) {
		Set<Hotel> hotels = new HashSet<Hotel>();
		for (Hotel hotel : activeHotels) {
			for (Cell adjTile : adjTiles) {
				if (hotel.isInHotel(adjTile)) {
					hotels.add(hotel);
				}
			}
		}
		return hotels;
	}

	public List<String> remainingHotels() {
		List<String> remainingHotels = new ArrayList<String>();
		for (String hotel : AcquireStatistics.hotels) {
			if (!(activeHotels.contains(hotel))) {
				remainingHotels.add(hotel);
			}
		}
		return remainingHotels;
	}

	public void findHotel(String hotelName, Cell tile) throws GameException {
		Hotel newHotel = new Hotel(hotelName);
		List<Cell> adjTiles = adjascentTiles(tile);
		newHotel.expandHotel(tile);
		markCell(tile);
		newHotel.expandHotel(adjTiles.get(0));
		activeHotels.add(newHotel);
		System.out.println("Found hotel: " + hotelName);
	}
	
	public void mergingHotels(Hotel largestHotel, Set<Hotel> dissolvingHotels, Cell mergingTile) {
		for(Hotel dissolvingHotel: dissolvingHotels) {
			for(Cell tile: dissolvingHotel.getHotelTiles()) {
				largestHotel.expandHotel(tile);
			}
			activeHotels.remove(dissolvingHotel);
			dissolvingHotel.defunctHotel();
		}
		largestHotel.expandHotel(mergingTile);
		
	}
	
	public Set<Hotel> getDissolvingHotels(Set<Hotel> mergingHotels, Hotel largestHotel) {
		mergingHotels.remove(largestHotel);
		return mergingHotels;
	}

	
	public Set<Hotel> getLargeHotel(Set<Hotel> mergingHotels) {
		Hotel largestHotel = null;
		Set<Hotel> largestHotels = new HashSet<Hotel>();
		int largestHotelSize = Integer.MIN_VALUE;
		for(Hotel hotel: mergingHotels) {
			if(hotel.getHotelSize() >= largestHotelSize) {
				largestHotel = hotel;
				largestHotelSize = hotel.getHotelSize();
			}
		}
		largestHotels.add(largestHotel);
		for(Hotel hotel: mergingHotels) {
			if(hotel.getHotelSize() == largestHotelSize) {
				largestHotels.add(hotel);
			}
		}
		return largestHotels;
	}

	public Double getShareValue(String hotel, Integer numOfShares) {
		for(Hotel activeHotel: activeHotels) {
			if(activeHotel.getHotelName().equals(hotel)) {
				return activeHotel.totalValueOfShares(numOfShares);
			}
		}
		return 0.0;
	}
	
	public void decrementShares(int noOfShares, String hotelName) {
		for(Hotel activeHotel: activeHotels) {
			if(activeHotel.getHotelName().equals(hotelName)) {
				activeHotel.decrementSharesAvailable(noOfShares);
			}
		}
	}
	
	public String getBoardTag() {
		String boardTag = "<board> ";
		for (int rowIdx = 0; rowIdx < AcquireStatistics.rows; rowIdx++) {
			for (int colIdx = 0; colIdx < AcquireStatistics.columns; colIdx++) {
				if(board[rowIdx][colIdx].isMarked()) {
					boardTag += board[rowIdx][colIdx].getTileTag();
				}
			}
		}
		for(Hotel activeHotel: activeHotels) {
			boardTag += activeHotel.getHotelTag();
		}
		boardTag += "</board>";
		return boardTag;
	}
	
	public String getLargestHotelTag() {
		String largestHotelTag = "";
		Set<Hotel> largestHotels = getLargeHotel(activeHotels);
		for(Hotel largestHotel: largestHotels) {
			largestHotelTag += "<hotel label=" + largestHotel.getHotelName() + " />";
		}
		return largestHotelTag;
		
	}

}
