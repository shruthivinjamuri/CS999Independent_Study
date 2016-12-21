package acquireClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import AcquireGame.AcquireStatistics;
import AcquireGame.GameException;

public class evaluation2 implements IEvaluate {
	
	public static final int maxHotelSize = 41;
	public static final int totalShares = 25;
	
	@Override
	public double evaluateState(PlayerAdmin state, int PlayerID) throws GameException {
		double pts = 0.0;
		//rating based on player's buying power
		StrategyPlayer cPlayer = state.getPlayers().get(PlayerID);

		if(state.getCurrentClientState().getActiveHotels().size() > 0) {
			ArrayList<Hotel> activeHotels = new ArrayList<>();
			for(Hotel hotel: state.getCurrentClientState().getActiveHotels()) {
				activeHotels.add(hotel);
			}
			Collections.sort(activeHotels, new CustomHotelComparator());
			if(cPlayer.getPlayerFund() >= (3*activeHotels.get(0).totalValueOfShares(1)))
				pts = pts+100;
			else if(cPlayer.getPlayerFund() >= (2*activeHotels.get(0).totalValueOfShares(1)))
				pts = pts+95;
			else if(cPlayer.getPlayerFund() >= activeHotels.get(0).totalValueOfShares(1))
				pts = pts+85;
			else if(cPlayer.getPlayerFund() >=
					(3*activeHotels.get(state.getCurrentClientState().getActiveHotels().size() - 1).totalValueOfShares(1)))
				pts = pts+70;
			else if(cPlayer.getPlayerFund() >=
					(2*activeHotels.get(state.getCurrentClientState().getActiveHotels().size() - 1).totalValueOfShares(1)))
				pts = pts+50;
			else if(cPlayer.getPlayerFund() >=
					activeHotels.get(state.getCurrentClientState().getActiveHotels().size() - 1).totalValueOfShares(1))
				pts = pts+25;
			
		}
		//rating based on player's chance of getting merger bonus
		pts += addValueByBonus(state,PlayerID);
		pts = pts / 500;
		pts = pts > 1 ? 1 : pts;
		//System.out.println(val);
		return pts;
	}
	
	class CustomHotelComparator implements Comparator<Hotel> {
		public int compare(Hotel h1, Hotel h2) {

			if (h1.totalValueOfShares(1) > h2.totalValueOfShares(1)) 
				return -1;
			else if (h1.totalValueOfShares(1) < h2.totalValueOfShares(1)) 
				return 1;
			else 
				return 0;
		}
	}
	
	public  double addValueByBonus(PlayerAdmin state, int PlayerID) throws GameException{
		double val = 0;
		StrategyPlayer p = state.getPlayers().get(PlayerID);
		Board board = state.getCurrentClientState();
		for (String key: p.getShares().keySet()) {
			int remShares = board.getRemSharesAvailable(key);
			int pCount = p.getShares().get(key);
			val  = val + pCount * board.getHotelByName(key).totalValueOfShares(1)/100;
			if(pCount >= 13){
				val = val + AcquireStatistics.singleShareValue(key, board.getHotelByName(key).getHotelSize())/100;
			}
			else if(pCount >= 4 && remShares + pCount == totalShares ){
				val = val + AcquireStatistics.singleShareValue(key, board.getHotelByName(key).getHotelSize())/120;
			}
			else if(remShares + pCount == totalShares ){
				val = AcquireStatistics.singleShareValue(key, board.getHotelByName(key).getHotelSize())/150;
			}
			else if(pCount >= 10 &&   2*pCount > totalShares - remShares ){
				val = AcquireStatistics.singleShareValue(key, board.getHotelByName(key).getHotelSize())/150;
			}
			else if( 2*pCount > totalShares - remShares ){
				val = AcquireStatistics.singleShareValue(key, board.getHotelByName(key).getHotelSize()) /200;
			}
		}
		return val;
	}

}
