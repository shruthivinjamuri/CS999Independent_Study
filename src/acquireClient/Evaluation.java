package acquireClient;

import AcquireGame.GameException;

public class Evaluation {
	private PlayerAdmin eState;
	private double eValue;
	
	public Evaluation(PlayerAdmin eState, double eValue) {
		this.eState = eState;
		this.eValue = eValue;
	}
	
	public Evaluation(double eValue) {
		this.eState = null;
		this.eValue = eValue;
	}
	
	public Evaluation(PlayerAdmin state,int PlayerID,IEvaluate EvalPlan) throws GameException{
		this.eState = state;
		this.eValue = EvalPlan.evaluateState(state,PlayerID);

	}
	
	public void setState(PlayerAdmin eState) {
		this.eState = eState;
	}
	
	public double getValue() {
		return this.eValue;
	}
	
	public PlayerAdmin getState() {
		return this.eState;
	}
}
