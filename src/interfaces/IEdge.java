package interfaces;

public interface IEdge extends Comparable<IEdge>{
	public INode getTo();
	public INode getFrom();
	public Double getCost();
	public void setCost();
	public String toString();
}
