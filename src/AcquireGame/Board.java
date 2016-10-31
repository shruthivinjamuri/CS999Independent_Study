package AcquireGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
	private Cell[][] board;
	private Set<Hotel> activeHotels;

	public Board() {
		this.board = new Cell[9][12];
		initializeBoard();
		activeHotels = new HashSet<Hotel>();
	}

	public void initializeBoard() {
		for (int rowIdx = 0; rowIdx < 10; rowIdx++) {
			for (int colIdx = 0; colIdx < 13; colIdx++) {
				board[rowIdx][colIdx] = new Cell(AcquireStatistics.rowMapping().get(rowIdx + 1), colIdx + 1);
			}
		}
	}

	public void markCell(Cell Tile) {
		if (Tile.equals(board[Tile.getRow()][Tile.getCol()])) {
			board[Tile.getRow()][Tile.getCol()].setMarked(true);
		}
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
		if (board[row - 1][col].isMarked()) {
			adjTiles.add(board[row - 1][col]);
		}
		if (board[row][col - 1].isMarked()) {
			adjTiles.add(board[row][col - 1]);
		}
		if (board[row][col + 1].isMarked()) {
			adjTiles.add(board[row][col + 1]);
		}
		if (board[row + 1][col].isMarked()) {
			adjTiles.add(board[row + 1][col]);
		}
		return adjTiles;
	}

	public List<Cell> drawTiles(int noOfTiles) {
		List<Cell> tiles = new ArrayList<Cell>();
		while (tiles.size() <= noOfTiles) {
			Cell tile = board[(int) (Math.random() % 9)][(int) (Math.random() % 12)];
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
		if (!(board[row - 1][col].isMarked() && board[row][col - 1].isMarked() && board[row][col + 1].isMarked()
				&& board[row + 1][col].isMarked())) {
			return true;
		}
		return false;
	}

	public String placeTile(Cell tile) {
		if (isSingleTileOnBoard(tile)) {
			board[tile.getRow()][tile.getCol()].setMarked(true);
			return "singleton";
		} else {
			List<Cell> adjTiles = adjascentTiles(tile);
			if (adjTiles.size() == 1) {
				if (activeHotels.size() < 7) {
					for (Hotel hotel : activeHotels) {
						if (hotel.isInHotel(adjTiles.get(0))) {
							hotel.expandHotel(tile);
							return "growing";
						}
					}
					return "founding";
				} else {
					return "impossible";
				}
			} else {
				for (Hotel hotel : activeHotels) {
					int sameHotelCount = 0;
					for (Cell adjTile : adjTiles) {
						if (hotel.isInHotel(adjTile)) {
							sameHotelCount++;
						}
					}
					if (sameHotelCount == adjTiles.size()) {
						hotel.expandHotel(tile);
						return "growing";
					}
				}
				for (Hotel hotel : activeHotels) {
					if (validateDeadTile(tile, adjTiles)) {
						tile.setDeadTile(true);
						return "impossible";
					}
					return "merging";
				}
			}
		}		
		return "error";
	}

	private boolean validateDeadTile(Cell tile, List<Cell> adjTiles) {
		Set<Hotel> hotels = new HashSet<Hotel>();
		for (Hotel hotel : activeHotels) {
			for (Cell adjTile : adjTiles) {
				if (hotel.isInHotel(adjTile)) {
					hotels.add(hotel);
				}
			}
		}
		int safeHotelCount = 0;
		for (Hotel hotel : hotels) {
			if (hotel.getHotelSize() >= 11) {
				safeHotelCount++;
			}
		}

		return (safeHotelCount == hotels.size());
	}

	public List<String> remainingHotels() {
		List<String> remainingHotels = new ArrayList<String>();
		for (String hotel: AcquireStatistics.hotels) {
			if(!(activeHotels.contains(hotel))) {
				remainingHotels.add(hotel);
			}
		}
		return remainingHotels;
	}

	public void findHotel(String hotelName, Cell tile) {
		Hotel newHotel = new Hotel(hotelName);
		List<Cell> adjTiles = adjascentTiles(tile);
		newHotel.expandHotel(tile);
		newHotel.expandHotel(adjTiles.get(0));
		activeHotels.add(newHotel);
	}

}
