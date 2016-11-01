package AcquireGame;

public class Cell {
	private char row;
	private int col;
	private boolean isMarked;
	private boolean isTileAvailable;
	private boolean isDeadTile;

	public Cell(char row, int col) {
		this.row = row;
		this.col = col;
		this.isMarked = false;
		this.isTileAvailable = true;
		this.setDeadTile(false);
	}

	public String getName(char row, int col) {
		if (AcquireStatistics.isValidRow(row) && AcquireStatistics.isValidCol(col)) {
			return row +""+ col;
		}
		return "Invalid Cell";
	}

	public boolean equals(char row, int col) {
		return this.row == row && this.col == col;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

	public int getRow() {
		return (row - 65);
	}

	public int getCol() {
		return col - 1;
	}

	public boolean isTileAvailable() {
		return isTileAvailable;
	}

	public void setTileAvailable(boolean isTileAvailable) {
		this.isTileAvailable = isTileAvailable;
	}

	public boolean isDeadTile() {
		return isDeadTile;
	}

	public void setDeadTile(boolean isDeadTile) {
		this.isDeadTile = isDeadTile;
	}
	
}
