package acquireClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AcquireGame.AcquireStatistics;

public class Board {
	private Tile[][] board;
	private Set<Hotel> activeHotels;
	private Map<String, Integer> shares;

	public Board(List<Tile> tilesOnBoard, Set<Hotel> activeHotels, Map<String, Integer> shares) {
		initializeBoard();
		for (Tile tileOnBoard: tilesOnBoard) {
			board[tileOnBoard.getRow()][tileOnBoard.getCol()].setIsAvailable(false);
		}
		this.activeHotels.addAll(activeHotels);
		this.shares.putAll(shares);
	}

	private void initializeBoard() {
		for (int rowIdx = 0; rowIdx < AcquireStatistics.rows; rowIdx++) {
			for (int colIdx = 0; colIdx < AcquireStatistics.columns; colIdx++) {
				board[rowIdx][colIdx] = new Tile(AcquireStatistics.rowMapping().get(rowIdx + 1), colIdx + 1);
			}
		}
	}

	private List<Tile> adjascentTiles(Tile tile) {
		int row = tile.getRow();
		int col = tile.getCol();
		List<Tile> adjTiles = new ArrayList<Tile>();
		if ((row - 1 > 0) && (!board[row - 1][col].isAvailable()) 
				&& (!validateDeadTile(board[row - 1][col], adjTiles))) {
			adjTiles.add(board[row - 1][col]);
		}
		if ((col - 1 > 0) && (!board[row][col - 1].isAvailable()) 
				&& (!validateDeadTile(board[row - 1][col], adjTiles))) {
			adjTiles.add(board[row][col - 1]);
		}
		if ((col + 1 < AcquireStatistics.columns) && (!board[row][col + 1].isAvailable()) 
				&& (!validateDeadTile(board[row - 1][col], adjTiles))) {
			adjTiles.add(board[row][col + 1]);
		}
		if ((row + 1 < AcquireStatistics.rows) && (!board[row + 1][col].isAvailable()) 
				&& (!validateDeadTile(board[row - 1][col], adjTiles))) {
			adjTiles.add(board[row + 1][col]);
		}
		return adjTiles;
	}
	
	private boolean validateDeadTile(Tile tile, List<Tile> adjTiles) {
		Set<Hotel> hotels = adjacentHotels(adjTiles);
		int safeHotelCount = 0;
		for (Hotel hotel : hotels) {
			if (hotel.getHotelSize() >= 11) {
				safeHotelCount++;
			}
		}

		return (safeHotelCount == hotels.size());
	}
	
	private Set<Hotel> adjacentHotels(List<Tile> adjTiles) {
		Set<Hotel> hotels = new HashSet<Hotel>();
		for (Hotel hotel: activeHotels) {
			for (Tile adjTile : adjTiles) {
				if (hotel.isInHotel(adjTile)) {
					hotels.add(hotel);
				}
			}
		}
		return hotels;
	}
	
	public Set<String> remainingHotels() {
		Set<String> remainingHotels = new HashSet<String>();
		for (String hotel : AcquireStatistics.hotels) {
			if (!(activeHotels.contains(hotel))) {
				remainingHotels.add(hotel);
			}
		}
		return remainingHotels;
	}
	
	public boolean isFounding(Tile tile) {
		List<Tile> adjTiles = adjascentTiles(tile);
		if(adjTiles.size() < 1) return false;
		for (Tile adjTile: adjTiles) {
			if(!isSingleTileOnBoard(adjTile)) {
				return false;
			}
		}
		return activeHotels.size() < 7;
	}
	
	private boolean isSingleTileOnBoard(Tile tile) {
		int row = tile.getRow();
		int col = tile.getCol();
		if (((row - 1 > 0) && (!board[row - 1][col].isAvailable())) || ((col - 1 > 0) && (!board[row][col - 1].isAvailable())) || 
				((col + 1 < AcquireStatistics.columns) && (!board[row][col + 1].isAvailable()))
				|| ((row + 1 < AcquireStatistics.rows) && (!board[row + 1][col].isAvailable()))) {
			return false;
		}
		return true;
	}

	public Set<Hotel> getActiveHotels() {
		return activeHotels;
	}

	public Map<String, Integer> getShares() {
		return shares;
	}

}
