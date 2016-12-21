package graphInterfaces;

import java.util.List;

public interface INode extends Comparable<INode> {
	public String getLabel();
	public Double getMinCost();
	public void setMinCost(Double cost);
	public void addOutEdge(IEdge edge);
	public List<IEdge> getOutEdges();
	public String toString();
	public boolean equals(INode other);
	public boolean isVisited();
	public void setVisited(boolean isVisited);
	public int getPreClock();
	public void setPreClock(int preClock);
	public int getPostClock();
	public void setPostClock(int postClock);
}
