package acquireServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import AcquireGame.AcquireStatistics;
import AcquireGame.Board;
import AcquireGame.Cell;
import AcquireGame.Hotel;
import AcquireGame.AcquireStatistics.TilePlacementType;

public class StrategyGame {
	private List<ServerPlayer> players;
	private Board board;
	private int turn;

	public StrategyGame(List<ServerPlayer> serverPlayers) {
		this.players = serverPlayers;
		this.board = new Board();
		for (ServerPlayer strategyPlayer : serverPlayers) {
			assignTiles(strategyPlayer);
		}
		turn = 0;
	}

	public void assignTiles(ServerPlayer serverPlayer) {
		serverPlayer.setTiles(board.drawTiles(6 - serverPlayer.getTiles().size()));
	}

	public String clientPlayerSetup() {
		String clientSetup = "<setup";
		int i = 1;

		for (ServerPlayer player : players) {
			clientSetup += " player" + i + "=";
			i++;
			int tileCount = 1;
			for (Cell tile : player.getTiles()) {
				if (tileCount == 6) {
					clientSetup += tile.getName();
				} else {
					clientSetup += tile.getName() + "|";
				}
				tileCount++;
			}
		}

		return clientSetup + " />";

	}

	public void playTile(Cell tile) {
		TilePlacementType placeTile = board.placeTile(tile);
		switch (placeTile) {
		case Singleton:
		case Growing:
			break;
		case Merging:
			handleMerging(tile, players.get(turn));
			break;
		case Error:
			System.out.println("Error Occured");
			return;
		default:
			break;
		}
	}

	public void setPlayerTurn() {
		turn++;
		if (turn == players.size()) {
			turn = 0;
		}
	}

	public void findingHotel(Cell tile, String hotelName) {
		board.findHotel(hotelName, tile);
	}

	private boolean inadequateTiles(List<Cell> tiles, ServerPlayer player) {
		if (tiles.size() < 6) {
			improperEndOfGame("inadequate tiles", player.getPlayerName());
			return true;
		}
		return false;
	}

	public boolean afterEachTurn() {
		removeDeadTiles(players.get(turn));
		assignTiles(players.get(turn));
		return (!inadequateTiles(players.get(turn).getTiles(), players.get(turn)));
	}

	private void handleMerging(Cell tile, ServerPlayer serverPlayer) {
		List<Cell> adjTiles = board.adjascentTiles(tile);
		Set<Hotel> mergingHotels = board.adjacentHotels(adjTiles);
		Set<Hotel> largestHotels = board.getLargeHotel(mergingHotels);
		Hotel largestHotel = null;
		if (largestHotels.size() == 1) {
			for (Hotel hotel : largestHotels) {
				largestHotel = hotel;
			}
		} else {
			largestHotel = serverPlayer.pickRandomHotel(largestHotels);
		}
		Set<Hotel> dissolvingHotels = board.getDissolvingHotels(mergingHotels, largestHotel);
		distributeBonuses(dissolvingHotels);
		sellShares(dissolvingHotels);
		board.mergingHotels(largestHotel, dissolvingHotels, tile);
		System.out.println("Merging largest hotel:" + largestHotel.getHotelName());

	}

	public void announceWinner() {
		Map<String, Double> finalStats = new HashMap<String, Double>();
		for (ServerPlayer player : players) {
			Double totalCash = player.getPlayerFund();
			for (String hotel : player.getShares().keySet()) {
				totalCash += board.getShareValue(hotel, player.getShares().get(hotel));
			}
			finalStats.put(player.getPlayerName(), totalCash);
		}

		List<Entry<String, Double>> sortedMap = new ArrayList<Entry<String, Double>>(finalStats.entrySet());
		Collections.sort(sortedMap, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});

		System.out.println(sortedMap);
		System.out.println("Game ended gracefully");
	}

	private void removeDeadTiles(ServerPlayer serverPlayer) {
		ArrayList<Cell> deadTiles = new ArrayList<Cell>();
		for (Cell tile : serverPlayer.getTiles()) {
			if (tile.isDeadTile()) {
				tile.setTileAvailable(false);
				deadTiles.add(tile);
			}
		}

		for (Cell deadTile : deadTiles) {
			serverPlayer.removeTile(deadTile);
		}
	}

	public void buyShares(Map<String, Integer> sharesToBuy) {
		int shares = AcquireStatistics.maxSharesToBuy;
		for (String hotel : sharesToBuy.keySet()) {
			shares = sharesToBuy.get(hotel);
			Double sharesValue = board.getShareValue(hotel, shares);
			if (players.get(turn).getPlayerFund() >= sharesValue) {
				players.get(turn).decrementPlayerFund(sharesValue);
				players.get(turn).getShares().put(hotel, players.get(turn).getShares().containsKey(hotel)
						? players.get(turn).getShares().get(hotel) + shares : shares);
				board.decrementShares(shares, hotel);
			}
		}

	}

	private void sellShares(Set<Hotel> dissolvingHotels) {
		for (Hotel dissolvingHotel : dissolvingHotels) {
			Double singleShareValue = AcquireStatistics.singleShareValue(dissolvingHotel.getHotelName(),
					dissolvingHotel.getHotelSize());
			for (ServerPlayer player : players) {
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
			List<ServerPlayer> majorityShareHolders = new ArrayList<ServerPlayer>();
			List<ServerPlayer> minorityShareHolders = new ArrayList<ServerPlayer>();
			List<Integer> shares = new ArrayList<Integer>();
			for (ServerPlayer player : players) {
				shares.add(player.getShares().get(dissolvingHotel.getHotelName()));
			}
			Arrays.sort(shares.toArray());
			int i = 1;
			for (ServerPlayer player : players) {
				if (player.getShares().get(dissolvingHotel.getHotelName()) == shares.get(0)) {
					majorityShareHolders.add(player);
				}
				while (shares.get(i) == shares.get(0)) {
					i++;
				}

				if (player.getShares().get(dissolvingHotel.getHotelName()) == shares.get(i)) {
					minorityShareHolders.add(player);
				}
			}

			if (majorityShareHolders.size() == 1) {
				majorityShareHolders.get(0).incrementPlayerFund(majorityBonus);
				if (minorityShareHolders.size() == 0) {
					majorityShareHolders.get(0).incrementPlayerFund(minorityBonus);
				} else {
					for (ServerPlayer minorityHolder : minorityShareHolders) {
						minorityHolder.incrementPlayerFund(minorityBonus / minorityShareHolders.size());
					}
				}

			} else if (majorityShareHolders.size() > 1) {
				Double totalBonus = majorityBonus + minorityBonus;
				for (ServerPlayer majorityHolder : majorityShareHolders) {
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
		System.out.println("Game ended due to " + failureMsg + " during " + playerName + "'s turn");
		announceWinner();
	}

	public String currentGameState() {
		String currentState = "<clientState> ";
		currentState += board.getBoardTag();
		currentState += players.get(turn).playerTag();
		if (endOfGame()) {
			currentState += "<error />";
		}
		currentState += "</clientState>";
		return currentState;

	}

}
