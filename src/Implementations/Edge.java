package Implementations;

import interfaces.IEdge;
import interfaces.INode;

public class Edge implements IEdge{
    
    private Node origin;
    private Node destination;
    private Double cost;
    
    public Edge(Node origin, Node destination, Double cost){
           this.origin = origin;
           this.destination = destination;
           this.cost = cost;
    }

    public int compareTo(IEdge edge) {
           
           return this.cost.compareTo(edge.getCost());
    }

    public INode getTo() {

           return destination;
    }

    public INode getFrom() {

           return origin;
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

