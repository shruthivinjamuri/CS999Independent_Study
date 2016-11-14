package AcquireGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import AcquireGame.AcquireStatistics.AfterMoveOptions;
import AcquireGame.AcquireStatistics.TilePlacementType;

public class Game {
	private int noOfPlayers;
	private List<Player> players;
	private Board board;
	private boolean endGame;

	public Game(int noOfPlayers, ArrayList<Player> players) {
		this.noOfPlayers = noOfPlayers;
		this.players = players;
		this.board = new Board();
		for (Player player : players) {
			assignTiles(player);
		}
		endGame = false;
	}

	public void assignTiles(Player player) {
		player.setTiles(board.drawTiles(6 - player.getTiles().size()));
	}

	public int getNoOfPlayers() {
		return noOfPlayers;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Board getBoard() {
		return board;
	}

	public void startGame() {
		while (!endOfGame()) {
			for (Player player : players) {
				System.out.println("Player Name: " + player.getPlayerName());
				Cell tile = null;
				int impossibleTileCount = -1;
				TilePlacementType placeTile = TilePlacementType.Impossible;
				
				while (placeTile.equals(TilePlacementType.Impossible)) {
					impossibleTileCount++;
					if (impossibleTileCount >= 6) {
						improperEndOfGame("impossible tiles", player.getPlayerName());
						return;
					}
					tile = player.playTile();
					placeTile = board.placeTile(tile);
				}
				
				System.out.println("player " + player.getPlayerName() + " playing the tile" + tile.getName());
				System.out.println("Tile status: " + placeTile);
				
				switch (placeTile) {
				case Singleton:
				case Growing:
					break;
				case Founding:
					List<String> remainingHotels = board.remainingHotels();
					board.findHotel(remainingHotels.get((int) ((Math.random() * 50) % remainingHotels.size())), tile);
					break;
				case Merging:
					handleMerging(tile, player);
					break;
				case Error:
					System.out.println("Error Occured");
					return;
				default:
					break;
				}

				// Set the tile played to not available and delete from the
				// player tile list
				tile.setTileAvailable(false);
				player.removeTile(tile);

				List<AfterMoveOptions> options = afterMoveOptions(player);
				for (AfterMoveOptions option : options) {
					System.out.println(option);
					switch (option) {
					case BuyShares:
						buyShares(player);
						System.out.println("Buying shares done");
						break;
					case RemoveDeadTiles:
						removeDeadTiles(player);
						assignTiles(player);
						if(inadequateTiles(player.getTiles(), player)) return;
						break;
					case EndTurn:
						assignTiles(player);
						if(inadequateTiles(player.getTiles(), player)) return;
						break;
					}
				}
			}
		}

		announceWinner();
	}

	private boolean inadequateTiles(List<Cell> tiles, Player player) {
		if (tiles.size() < 6) {
			improperEndOfGame("inadequate tiles", player.getPlayerName());
			return true;
		}
		 return false;
	}

	private void handleMerging(Cell tile, Player player) {
		List<Cell> adjTiles = board.adjascentTiles(tile);
		Set<Hotel> mergingHotels = board.adjacentHotels(adjTiles);
		Set<Hotel> largestHotels = board.getLargeHotel(mergingHotels);
		Hotel largestHotel = null;
		if (largestHotels.size() == 1) {
			for (Hotel hotel : largestHotels) {
				largestHotel = hotel;
			}
		} else {
			largestHotel = player.pickRandomHotel(largestHotels);
		}
		Set<Hotel> dissolvingHotels = board.getDissolvingHotels(mergingHotels, largestHotel);
		distributeBonuses(dissolvingHotels);
		sellShares(dissolvingHotels);
		board.mergingHotels(largestHotel, dissolvingHotels, tile);
		System.out.println("Merging largest hotel:" + largestHotel.getHotelName());
		
	}

	private void announceWinner() {
		Map<String, Double> finalStats = new HashMap<String, Double>();
		for (Player player : players) {
			Double totalCash = player.getPlayerFund();
			for (String hotel : player.getShares().keySet()) {
				totalCash += board.getShareValue(hotel, player.getShares().get(hotel));
			}
			finalStats.put(player.getPlayerName(), totalCash);
		}

		int i = 1;
		
		List<Entry<String, Double>> sortedMap = new ArrayList<Entry<String, Double>>(finalStats.entrySet());
		Collections.sort(sortedMap, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
			
		});
//		for (String playerName : finalStats.keySet()) {
//			System.out.println(i + ". " + playerName + " - " + finalStats.get(playerName));
//			i++;
//		}
		
		System.out.println(sortedMap);
		System.out.println("Game ended gracefully");
	}

	private void removeDeadTiles(Player player) {
		ArrayList<Cell> deadTiles = new ArrayList<Cell>();
		for (Cell tile : player.getTiles()) {
			if (tile.isDeadTile()) {
				tile.setTileAvailable(false);
				deadTiles.add(tile);
			}
		}

		for (Cell deadTile : deadTiles) {
			player.removeTile(deadTile);
		}
	}

	private void buyShares(Player player) {
		int shares = AcquireStatistics.maxSharesToBuy;
		int turns = 3;
		while (turns > 0 && shares > 0 && minFundsAvailable(player)) {
			if (!(adequateSharesAvailable(shares)))
				return;
			Hotel hotel = player.pickRandomHotel(board.getActiveHotels());
			int sharesInThisHotel = new Random().nextInt(shares) + 1;
			System.out.println(sharesInThisHotel);
			Double sharesValue = hotel.valueForShares(sharesInThisHotel);
			if (player.getPlayerFund() >= sharesValue) {
				player.decrementPlayerFund(sharesValue);
				player.getShares().put(hotel.getHotelName(), player.getShares().containsKey(hotel.getHotelName())
						? player.getShares().get(hotel.getHotelName()) + sharesInThisHotel : sharesInThisHotel);
				hotel.decrementSharesAvailable(sharesInThisHotel);
				shares -= sharesInThisHotel;
			} else {
				System.out.println("Not enough funds with the player");
			}
			turns--;
		}

	}

	private boolean adequateSharesAvailable(int shares) {
		int sharesAvailable = 0;
		for (Hotel activeHotel : board.getActiveHotels()) {
			sharesAvailable += activeHotel.getSharesAvailable();
		}
		return shares <= sharesAvailable;
	}

	private boolean minFundsAvailable(Player player) {
		if (board.getActiveHotels().size() == 0) {
			System.out.println("No active hotels on the board yet!!");
			return false;
		}

		Double minShareValue = Double.MAX_VALUE;
		for (Hotel hotel : board.getActiveHotels()) {
			if (hotel.valueForShares(1) < minShareValue) {
				minShareValue = hotel.valueForShares(1);
			}
		}
		if (player.getPlayerFund() == 0 || player.getPlayerFund() < minShareValue) {
			System.out.println("Player has not enough funds to buy even a single share");
			return false;
		}
		return true;
	}

	private List<AfterMoveOptions> afterMoveOptions(Player player) {
		List<AfterMoveOptions> options = new ArrayList<AfterMoveOptions>();
		if (player.canBuyShares()) {
			options.add(AfterMoveOptions.BuyShares);
		}
		if (player.hasDeadTiles()) {
			options.add(AfterMoveOptions.RemoveDeadTiles);
		}
		options.add(AfterMoveOptions.EndTurn);
		return options;
	}

	private void sellShares(Set<Hotel> dissolvingHotels) {
		for (Hotel dissolvingHotel : dissolvingHotels) {
			Double singleShareValue = AcquireStatistics.singleShareValue(dissolvingHotel.getHotelName(),
					dissolvingHotel.getHotelSize());
			for (Player player : players) {
				if (player.getShares().containsKey(dissolvingHotel.getHotelName())) {
					player.incrementPlayerFund(
							player.getShares().get(dissolvingHotel.getHotelName()) * singleShareValue);
					dissolvingHotel.incrementSharesAvailable(player.getShares().get(dissolvingHotel.getHotelName()));
					player.getShares().put(dissolvingHotel.getHotelName(), 0);

				}
			}
		}
	}

	private void distributeBonuses(Set<Hotel> dissolvingHotels) {
		for (Hotel dissolvingHotel : dissolvingHotels) {
			Double singleShareValue = AcquireStatistics.singleShareValue(dissolvingHotel.getHotelName(),
					dissolvingHotel.getHotelSize());
			Double majorityBonus = 10 * singleShareValue;
			Double minorityBonus = 5 * singleShareValue;
			List<Player> majorityShareHolders = new ArrayList<Player>();
			List<Player> minorityShareHolders = new ArrayList<Player>();
			for (Player player : players) {
				if (player.getMajorityShareHolder().contains(dissolvingHotel)) {
					majorityShareHolders.add(player);
				} else if (player.getMinorityShareHolder().contains(dissolvingHotel)) {
					minorityShareHolders.add(player);
				}
			}
			if (majorityShareHolders.size() == 1) {
				majorityShareHolders.get(0).incrementPlayerFund(majorityBonus);
				if (minorityShareHolders.size() == 0) {
					majorityShareHolders.get(0).incrementPlayerFund(minorityBonus);
				} else {
					for (Player minorityHolder : minorityShareHolders) {
						minorityHolder.incrementPlayerFund(minorityBonus / minorityShareHolders.size());
					}
				}

			} else if (majorityShareHolders.size() > 1) {
				Double totalBonus = majorityBonus + minorityBonus;
				for (Player majorityHolder : majorityShareHolders) {
					majorityHolder.incrementPlayerFund(totalBonus / majorityShareHolders.size());
				}
			}
		}
	}

	public boolean endOfGame() {
		Set<Hotel> activeHotels = board.getActiveHotels();
		int safeHotelCount = 0;
		for (Hotel activeHotel : activeHotels) {
			if (activeHotel.getHotelSize() >= 41) {
				return true;
			}
			if (activeHotel.isSafe()) {
				safeHotelCount++;
			}
			if (safeHotelCount == activeHotels.size()) {
				return true;
			}
		}
		return false;
	}
	
	private void improperEndOfGame(String failureMsg, String playerName) {
		System.out.println("Game ended due to " + failureMsg + " during "+playerName+"'s turn");
		announceWinner();
	}

}
