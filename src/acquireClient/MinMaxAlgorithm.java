package acquireClient;

import java.util.Comparator;

import AcquireGame.AcquireStatistics;
import AcquireGame.GameException;

public class MinMaxAlgorithm {

	public PlayerAdmin chooseState(PlayerAdmin inState, String evalPlan) throws GameException {
		IEvaluate ev;
		PlayerAdmin state = new PlayerAdmin(inState);
		if (evalPlan.equals("evaluation2")) {
			ev = new evaluation2();
		} else
			return null;

		return minimax(state, 2, 1, ev).getState();
	}

	public Evaluation minimax(PlayerAdmin inState, int depth, int me, IEvaluate evalPlan) throws GameException {
		PlayerAdmin state = new PlayerAdmin(inState);
		int totalPlayers = state.getPlayers().size();
		Evaluation evn;

		if (depth == 0 || checkGameTermination(state)) {
			if (me == 1)
				evn = new Evaluation(state, 0, evalPlan);
			else
				evn = new Evaluation(state, totalPlayers - me + 1, evalPlan);
			return evn;
		}

		if (me == 1) {
			Evaluation bestEvn = new Evaluation(0);
			GameTree gtree = new GameTree(state);
			gtree.generateGameTree();
			for (GameTree gt : gtree.children) {
				// System.out.println(depth);
				evn = minimax(gt.currentState, depth - 1, 2, evalPlan);
				bestEvn = evn.getValue() >= bestEvn.getValue() ? evn : bestEvn;
				if (bestEvn.getValue() == evn.getValue())
					bestEvn.setState(gt.currentState);
			}
			return bestEvn;
		} else {
			Evaluation bestEvn = new Evaluation(1);
			GameTree gtree = new GameTree(state);
			gtree.generateGameTree();
			me = me == totalPlayers ? 1 : me + 1;
			for (GameTree gt : gtree.children) {
				// System.out.println(depth);
				evn = minimax(gt.currentState, depth - 1, me, evalPlan);
				bestEvn = evn.getValue() <= bestEvn.getValue() ? evn : bestEvn;
				if (bestEvn.getValue() == evn.getValue())
					bestEvn.setState(gt.currentState);
			}
			return bestEvn;
		}
	}

	public boolean checkGameTermination(PlayerAdmin state) {
		int count = 0;
		for (Hotel h : state.getCurrentClientState().getActiveHotels()) {
			if (h.getHotelSize() >= 41)
				return true;
			if (h.getHotelSize() >= AcquireStatistics.safeHotelSize)
				count++;
		}
		if (count > 0 && count == state.getCurrentClientState().getActiveHotels().size())
			return true;

		return false;
	}

	class CustomEStateComparator implements Comparator<Evaluation> {
		public int compare(Evaluation e1, Evaluation e2) {

			if (e1.getValue() > e2.getValue())
				return -1;
			else if (e1.getValue() < e2.getValue())
				return 1;
			else
				return 0;

		}
	}

}
