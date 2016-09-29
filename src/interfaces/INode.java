package interfaces;

import java.util.List;

public interface INode extends Comparable<INode> {
	public String getLabel();
	public Double getMinCost();
	public void setMinCost(Double cost);
	public void addOutEdge(IEdge edge);
	public List<IEdge> getOutEdges();
	public String toString();
	public boolean equals(INode other);
}
