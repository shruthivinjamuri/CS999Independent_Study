package Implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.IEdge;
import interfaces.IGraph;
import interfaces.INode;
import interfaces.IPath;

public class Graph implements IGraph {
       private String graphName;
       private HashMap<String, Node> nodes;
       private List<IEdge> edges;
       private Double costInterval;
       
       public Graph(String graphName, Double costInterval){
              this.graphName = graphName;
              this.nodes = new HashMap<>();
              this.edges = new ArrayList<>();
              this.costInterval = costInterval;
       }


       public IEdge addEdge(String from, String to, Double cost) {
           Node origin = new Node(from);
           Node destination = new Node(to);
    	   IEdge edge = new Edge(origin, destination, cost);
           edges.add(edge);  
           return edge;
       }
       
//       private boolean checkTriangleEquality(Node from, Node to, int cost){
//    	   for(Edge firstEdge: from.getOutEdges()){
//    	    for(Edge secondEdge: firstEdge.getTo().getOutEdges())
//    	     if(secondEdge.getTo().compareTo(to) == 0){
//    	      if (firstEdge.getCost() + secondEdge.getCost() < cost || 
//    	       cost + secondEdge.getCost() < firstEdge.getCost() ||
//    	       firstEdge.getCost() + cost < secondEdge.getCost())
//    	        return false;
//    	    }
//    	   }
//    	   
//    	   return true;

       public INode addOrGetNode(String label) {
              if(!nodes.containsKey(label)){
                     nodes.put(label, new Node(label));       
              }
              return nodes.get(label);
       }

       public List<INode> getAllNodes() {
              List<INode> allNodes = new ArrayList<>();
              for(String key: nodes.keySet()){
                     allNodes.add(nodes.get(key));
              }
              return allNodes;
       }

       public List<IEdge> getAllEdges() {
              return this.edges;
       }

       public void joinGraph(IGraph graph) {
    	   	  for(String key: nodes.keySet()){
    	   		  for(INode toNode: graph.getAllNodes()){
    	   			  graph.addEdge(nodes.get(key).getLabel(), toNode.getLabel(), costInterval);
       	   		  }
    	   	  }
       }

       public Double getCostInterval() {
              return this.costInterval;
       }

       public IPath getPath(String from, String to) {
              return null;
       }

}

