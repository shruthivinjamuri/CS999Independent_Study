package Implementations;


import java.util.ArrayList;
import java.util.List;

import interfaces.IEdge;
import interfaces.INode;

public class Node implements INode{
       
       private String label;
       private List<IEdge> edges;
       private boolean isVisited;
       
       public Node(String label){
              this.label = label;
              this.edges = new ArrayList<>();
       }
       

       public int compareTo(INode node) {
              return label.compareTo(node.getLabel());
       }

       public String getLabel() {
              return label;
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

}
