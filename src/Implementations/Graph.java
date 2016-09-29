package Implementations;

import java.util.ArrayList;
import java.util.Collections;
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
    	   if (cost > costInterval){ 
    		   return null;
    	   }
    	   INode origin = addOrGetNode(from);
    	   INode destination = addOrGetNode(to);
    	   IEdge edge = new Edge(origin, destination, cost);
    	   edges.add(edge); 
           origin.addOutEdge(edge);
    	   
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

       public String getPath(String from, String to) {
           INode source = addOrGetNode(from);
           INode destination = addOrGetNode(to);
           HashMap<INode, IEdge> prev = dijkstraShortestPath(source);
           ArrayList<IEdge> path = new ArrayList<>();
           INode target = destination;
           while(target != source){
                  path.add(prev.get(target));
                  target = prev.get(target).getOrigin();
           }
           StringBuilder sb = new StringBuilder();
           sb.append("<path cost="+ destination.getMinCost()+">");
           for(int i = path.size()-1; i>=0; i--){
        	   sb.append(path.get(i).toString());
           }
           sb.append("</path>");
           return sb.toString();
    }
 
    public HashMap<INode, IEdge> dijkstraShortestPath(INode source){
           java.util.PriorityQueue<INode> queue = new java.util.PriorityQueue<INode>(nodes.size());
           HashMap<INode, IEdge> prev = new HashMap<>();
           
           source.setMinCost(0.0);
           
           for(INode node: getAllNodes()){
                  prev.put(node, null);
                  queue.add(node);
           }
           
           while(!queue.isEmpty()){
                  INode u = queue.poll();
                  Double alt = 0.0;
                  for(IEdge outEdge: u.getOutEdges()){
                        alt = u.getMinCost() + outEdge.getCost();
                        if(alt < outEdge.getDestination().getMinCost()){
                               outEdge.getDestination().setMinCost(alt);
                               prev.put(outEdge.getDestination(), outEdge);
                               if(queue.remove(outEdge.getDestination())){
                            	   queue.add(outEdge.getDestination());
                               }
                        }
                  }
           }
           return prev;
           
    }


}

