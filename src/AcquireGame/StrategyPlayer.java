package AcquireGame;
public class StrategyPlayer {

	private IStrategy strategy;
	
	public StrategyPlayer(IStrategy strategy) {
		this.strategy = strategy;
	}

}
