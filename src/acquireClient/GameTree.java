package acquireClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import AcquireGame.AcquireStatistics;

public class GameTree {
	public PlayerAdmin currentState;
	public ArrayList<GameTree> children;

	public GameTree(PlayerAdmin currentState) {
		this.currentState = currentState;
		children = new ArrayList<>();
	}

	public void generateGameTree() {
		PlayerAdmin child = new PlayerAdmin(currentState.getTurn(), currentState.getNoOfPlayers(),
				currentState.getPlayers());
		ArrayList<PlayerAdmin> placeStates = generatePlaceStates(child);
		ArrayList<PlayerAdmin> buyStates = generateBuyStates(placeStates);
		for (PlayerAdmin state : buyStates) {
			children.add(new GameTree(state));
		}
	}

	public ArrayList<PlayerAdmin> generatePlaceStates(PlayerAdmin state) {
		ArrayList<PlayerAdmin> placeStates = new ArrayList<PlayerAdmin>();
		for (Tile tile : currentState.getPlayers().get(currentState.getTurn()).getTiles()) {
			Board currentBoard = currentState.getCurrentClientState();
			currentBoard.setTileOnBoard(tile);
			if (currentBoard.isFounding(tile)) {
				for (String hotel : currentBoard.remainingHotels()) {
					Hotel hotelFormed = new Hotel(hotel);
					currentBoard.addHotel(hotelFormed);
					state.setCurrentClientState(currentBoard);
					placeStates.add(state);
				}
				continue;
			}

			state.setCurrentClientState(currentBoard);
			placeStates.add(state);
		}
		return placeStates;
	}

	public ArrayList<PlayerAdmin> generateBuyStates(ArrayList<PlayerAdmin> placeState) {
		ArrayList<PlayerAdmin> shareCombinationList = new ArrayList<PlayerAdmin>();
		for (PlayerAdmin state : placeState) {
			shareCombinationList.add(state); // adding the state for purchase of
												// zeros shares

			for (Hotel hotel : state.getCurrentClientState().getActiveHotels()) // adding
																				// the
																				// state
																				// for
																				// purchase
																				// of
																				// one
																				// shares
			{
				if (currentState.getCurrentClientState().getShares().get(hotel.getHotelName()) < 1) {
					continue;
				}
				if (currentState.getPlayers().get(currentState.getTurn()).getPlayerFund() >= hotel
						.totalValueOfShares(1)) {
					PlayerAdmin child = new PlayerAdmin(currentState.getTurn(), currentState.getNoOfPlayers(),
							currentState.getPlayers());
					Map<String, Integer> sharesToBuy = new HashMap<>();
					sharesToBuy.put(hotel.getHotelName(), 1);
					child.buyShares(sharesToBuy);
					shareCombinationList.add(child);
				}
			}

			for (Hotel firstHotel : state.getCurrentClientState().getActiveHotels()) // adding
																						// the
																						// state
																						// for
																						// purchase
																						// of
																						// two
																						// shares
			{

				for (Hotel secondHotel : state.getCurrentClientState().getActiveHotels()) {
					int hotelFlag = 0;

					if (firstHotel.totalValueOfShares(1) + secondHotel.totalValueOfShares(1) <= currentState
							.getPlayers().get(currentState.getTurn()).getPlayerFund()
							&& currentState.getCurrentClientState().getShares()
									.get(firstHotel.getHotelName()) < AcquireStatistics.sharesPerHotel
							&& currentState.getCurrentClientState().getShares()
									.get(secondHotel.getHotelName()) < AcquireStatistics.sharesPerHotel) {
						if (firstHotel.getHotelName().equals(secondHotel.getHotelName())) {
							if (currentState.getCurrentClientState().getShares()
									.get(firstHotel.getHotelName()) > AcquireStatistics.sharesPerHotel - 2) {
								hotelFlag = 1;
							}
						}
						if (hotelFlag == 0) {
							PlayerAdmin child = new PlayerAdmin(currentState.getTurn(), currentState.getNoOfPlayers(),
									currentState.getPlayers());
							Map<String, Integer> sharesToBuy = new HashMap<>();
							sharesToBuy.put(firstHotel.getHotelName(), 1);
							sharesToBuy.put(secondHotel.getHotelName(), 1);
							child.buyShares(sharesToBuy);
							shareCombinationList.add(child);

						}
					}
				}
			}

			for (Hotel firstHotel : state.getCurrentClientState().getActiveHotels()) // adding
																						// the
																						// state
																						// for
																						// purchase
																						// of
																						// three
																						// shares
			{

				for (Hotel secondHotel : state.getCurrentClientState().getActiveHotels()) {
					for (Hotel thirdHotel : state.getCurrentClientState().getActiveHotels()) {
						int hotelFlag = 0;
						if (firstHotel.totalValueOfShares(1) + thirdHotel.totalValueOfShares(1)
								+ secondHotel.totalValueOfShares(1) <= currentState.getPlayers()
										.get(currentState.getTurn()).getPlayerFund()
								&& currentState.getCurrentClientState().getShares()
										.get(firstHotel.getHotelName()) < AcquireStatistics.sharesPerHotel
								&& currentState.getCurrentClientState().getShares()
										.get(secondHotel.getHotelName()) < AcquireStatistics.sharesPerHotel
								&& currentState.getCurrentClientState().getShares()
										.get(thirdHotel.getHotelName()) < AcquireStatistics.sharesPerHotel) {
							if (firstHotel.getHotelName().equals(secondHotel.getHotelName())) {
								if (currentState.getCurrentClientState().getShares()
										.get(thirdHotel.getHotelName()) > AcquireStatistics.sharesPerHotel - 2) {
									hotelFlag = 1;
								}
							}

							if (firstHotel.getHotelName().equals(thirdHotel.getHotelName())) {
								if (currentState.getCurrentClientState().getShares()
										.get(thirdHotel.getHotelName()) > AcquireStatistics.sharesPerHotel - 2) {
									hotelFlag = 1;
								}
							}
							if (thirdHotel.getHotelName().equals(secondHotel.getHotelName())) {
								if (currentState.getCurrentClientState().getShares()
										.get(thirdHotel.getHotelName()) > AcquireStatistics.sharesPerHotel - 2) {
									hotelFlag = 1;
								}
							}
							if (firstHotel.getHotelName().equals(secondHotel.getHotelName())
									&& secondHotel.getHotelName().equals(thirdHotel.getHotelName())) {
								if (currentState.getCurrentClientState().getShares()
										.get(thirdHotel.getHotelName()) > AcquireStatistics.sharesPerHotel - 3) {
									hotelFlag = 1;
								}
							}
							if (hotelFlag == 0) {
								PlayerAdmin child = new PlayerAdmin(currentState.getTurn(),
										currentState.getNoOfPlayers(), currentState.getPlayers());
								Map<String, Integer> sharesToBuy = new HashMap<>();
								sharesToBuy.put(firstHotel.getHotelName(), 1);
								sharesToBuy.put(secondHotel.getHotelName(), 1);
								sharesToBuy.put(thirdHotel.getHotelName(), 1);
								child.buyShares(sharesToBuy);
								shareCombinationList.add(child);
							}
						}
					}
				}
			}

		}
		return shareCombinationList;
	}

}
