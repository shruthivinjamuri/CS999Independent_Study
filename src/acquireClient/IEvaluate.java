package acquireClient;

import AcquireGame.GameException;



public interface IEvaluate {
	
	public double evaluateState(PlayerAdmin state , int PlayerID) throws GameException;
	
}
