package interfaces;

import java.util.List;

public interface INode extends Comparable<INode> {
	public String getLabel();
	public void addOutEdge();
	public List<IEdge> getOutEdges();
	public String toString();
}
