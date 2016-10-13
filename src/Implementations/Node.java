package Implementations;


import java.util.ArrayList;
import java.util.List;

import interfaces.IEdge;
import interfaces.INode;

public class Node implements INode{
       
       private String label;
       private List<IEdge> edges;
       private Double minCost;
       private boolean isVisited;
       private int preClock;
       private int postClock;
       
       public Node(String label){
              this.label = label;
              this.edges = new ArrayList<>();
              this.minCost = Double.MAX_VALUE;
              this.isVisited = false;
       }
       

       public int compareTo(INode node) {
              return minCost.compareTo(node.getMinCost());
       }
       
       public boolean equals(INode other){
    	   return this.label.equals(other.getLabel());
       }

       public String getLabel() {
              return label;
       }
       
       public Double getMinCost(){
              return minCost;
       }
       
       public void setMinCost(Double cost){
              this.minCost = cost;
       }

       public void addOutEdge(IEdge edge) {
              edges.add(edge);
       }

       public List<IEdge> getOutEdges() {
              return edges;
       }
       
       public boolean isVisited(){
              return isVisited;
       }
       
       public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }


	public int getPreClock() {
		return preClock;
	}


	public void setPreClock(int preClock) {
		this.preClock = preClock;
	}


	public int getPostClock() {
		return postClock;
	}


	public void setPostClock(int postClock) {
		this.postClock = postClock;
	}

}
