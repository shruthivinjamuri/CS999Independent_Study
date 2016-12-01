package acquireClient;

public class Tile implements Comparable<Tile> {
	private char row;
	private int col;
	private boolean isAvailable;

	public Tile(char row, int col) {
		this.row = row;
		this.col = col;
		isAvailable = true;
	}

	@Override
	public int compareTo(Tile otherTile) {
		if (otherTile.row == this.row) {
			return this.col - otherTile.col;
		}
		return this.row - otherTile.row;
	}

	public int getRow() {
		return row - 65;
	}

	public int getCol() {
		return col - 1;
	}

	public boolean isAvailable() {
		return isAvailable;
	}
	
	public void setIsAvailable(boolean status) {
		isAvailable = status;
	}
}
