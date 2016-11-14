package AcquireGame;

public class Cell implements Comparable<Cell>{
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
		this.isDeadTile = false;
	}

	public String getName() {
			return row +""+ col;
	}

	public boolean equals(Cell other) {
		return this.getRow() == other.getRow() && this.getCol() == other.getCol();
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
	
	@Override
	public int compareTo(Cell otherTile) {
		if(otherTile.getRow() == this.getRow()) {
			return this.getCol() - otherTile.getCol();
		}
		return this.getRow() - otherTile.getRow();
	}
	
}
