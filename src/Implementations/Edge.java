package Implementations;

import interfaces.IEdge;
import interfaces.INode;

public class Edge implements IEdge{
    
    private INode origin;
    private INode destination;
    private Double cost;
    
    public Edge(INode origin, INode destination, Double cost){
           this.origin = origin;
           this.destination = destination;
           this.cost = cost;
    }

    public int compareTo(IEdge edge) {
           
           return this.cost.compareTo(edge.getCost());
    }
    
    public INode getOrigin() {
           return origin;
    }

    public void setOrigin(INode origin) {
           this.origin = origin;
    }

    public INode getDestination() {
           return destination;
    }

    public void setDestination(INode destination) {
           this.destination = destination;
    }

    public Double getCost() {

           return cost;
    }

    public void setCost(Double cost) {
           
           this.cost = cost;
    }
    
    public String toString(){
           return String.format("<edge from=%s to=%s cost=%d /> ", origin.getLabel(), destination.getLabel(), cost);
    }


}
