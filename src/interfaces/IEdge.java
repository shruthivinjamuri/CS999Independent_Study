package interfaces;

public interface IEdge extends Comparable<IEdge>{
    public Double getCost();
    public void setCost(Double cost);
    public String toString();
    public INode getOrigin();
    public void setOrigin(INode origin);
    public INode getDestination();
    public void setDestination(INode destination);
}

