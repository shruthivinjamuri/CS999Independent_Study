package AcquireGame;

public class Cell {
	private char row;
	private int col;
	private String name;
	private boolean isMarked;
	private boolean isTileAvailable;
	private boolean isDeadTile;

	public Cell(char row, int col) {
		this.row = row;
		this.col = col;
		setName(this.row, this.col);
		this.isMarked = false;
		this.isTileAvailable = true;
		this.setDeadTile(false);
	}

	private void setName(char row, int col) {
		if (AcquireStatistics.isValidRow(row) && AcquireStatistics.isValidCol(col)) {
			this.name = row +""+ col;
		}
	}

	public boolean equals(String name) {
		return this.name.equals(name);
	}

	public String getName() {
		return name;
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
		return col;
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
