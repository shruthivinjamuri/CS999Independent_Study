package AcquireGame;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private int noOfPlayers;
	private List<Player> players;
	private Board board;
	
	public Game(int noOfPlayers, ArrayList<Player> players, Board board) {
		this.noOfPlayers = noOfPlayers;
		this.players = players;
		this.board = board;
		for(Player player:players) {
			assignTiles(player);
		}
	}
	
	public void assignTiles(Player player) {
		player.setTiles(board.drawTiles(6 - player.getTiles().size()));
	}

	public int getNoOfPlayers() {
		return noOfPlayers;
	}

	public void setNoOfPlayers(int noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void startGame() {
		for(Player player:players) {
			Cell tile = player.playTile();
			String placeTile = board.placeTile(tile);
			if(placeTile.equals("impossible")) {
				while(placeTile.equals("impossible")) {
					tile = player.playTile();
					placeTile = board.placeTile(tile);
				}
			}
			if(placeTile.equals("singleton") || placeTile.equals("growing")) {
				continue;
			}
			if(placeTile.equals("founding")) {
				List<String> remainingHotels = board.remainingHotels();
				board.findHotel(
						remainingHotels.get((int) (Math.random() % remainingHotels.size())), 
						tile);
			}
			
		}
	}
	
//	public ArrayList<Cell> gameMove(Player player) {
//		for(Cell tile: player.getTiles()) {
//			if(tile.isAdjascentTile(tile)) {
//				if(board.getActiveHotels().size() < 7) {
//					
//				}
//			}
//		}
//	}

}
