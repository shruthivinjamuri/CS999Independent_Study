package AcquireGame;

import java.util.List;

public class Player {
	private String playerName;
	private double playerFund;
	private int playerTurn;
	private boolean isYourTurn;
	private List<Cell> tiles;
	
	public Player(String playerName, int playerTurn){
		this.playerName = playerName;
		this.playerFund = AcquireStatistics.playerFund;
		this.playerTurn = playerTurn;
		this.isYourTurn = false;
		
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public double getPlayerFund() {
		return playerFund;
	}

	public void setPlayerFund(double playerFund) {
		this.playerFund = playerFund;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(int playerTurn) {
		this.playerTurn = playerTurn;
	}

	public boolean isYourTurn() {
		return isYourTurn;
	}

	public void setYourTurn(boolean isYourTurn) {
		this.isYourTurn = isYourTurn;
	}

	public List<Cell> getTiles() {
		return tiles;
	}

	public void setTiles(List<Cell> tiles) {
		this.tiles = tiles;
	}
	
	public Cell playTile() {
		return tiles.get((int) (Math.random() % tiles.size()));
	}
	
//	if(valueForShares > playerFunds){
//		System.out.println("Player has no enough funds");
}
