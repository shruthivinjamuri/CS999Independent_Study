package Implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import interfaces.IEdge;
import interfaces.IGraph;
import interfaces.INode;
import interfaces.IPath;

public class Graph implements IGraph {
       private String graphName;
       private HashMap<String, Node> nodes;
       private List<IEdge> edges;
       private Double costInterval;
       private int clock;
       
       public Graph(String graphName, Double costInterval){
              this.graphName = graphName;
              this.nodes = new HashMap<>();
              this.edges = new ArrayList<>();
              this.costInterval = costInterval;
              this.clock = 1;
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
    
    public HashMap<INode, IEdge> bellmanFordShortestPath(INode source){
    	HashMap<INode, IEdge> predecessor = new HashMap<>();
    	
    	// Initializations
    	source.setMinCost(0.0);
    	for(INode node: getAllNodes()){
            predecessor.put(node, null);
    	} 
    	
    	// Relax edges repeatedly
    	for(int i = 0; i<getAllNodes().size()-1; i++){
    		for(IEdge edge: getAllEdges()){
    			if((edge.getOrigin().getMinCost() + edge.getCost()) < edge.getDestination().getMinCost()){
    				edge.getDestination().setMinCost(edge.getOrigin().getMinCost() + edge.getCost());
    				predecessor.put(edge.getDestination(), edge);
    			}
    		}
    	}
    	
    	// Check for negative-weight cycles
    	for(IEdge edge: getAllEdges()){
    		if((edge.getOrigin().getMinCost() + edge.getCost()) < edge.getDestination().getMinCost()){
    			System.out.println("Graph contains a negative-weight cycle");
    		}
    	}
    	
    	return predecessor;
    }
    
    public HashMap<INode, IEdge> improvedBellmanFord(INode source){
    	HashMap<INode, IEdge> predecessor =  new HashMap<>();
    	Queue<INode> queue = new LinkedList<>();
    	
    	// Initializations
    	source.setMinCost(0.0);
    	for(INode node: getAllNodes()){
    		predecessor.put(node, null);
    	}
    	
    	// Assuming source has an improvement
    	queue.add(source);
    	
    	// Relax edges repeatedly
    	while(!queue.isEmpty()){
    		INode u = queue.poll();
    		for(IEdge edge: u.getOutEdges()){
    			if(edge.getDestination().getMinCost() > u.getMinCost() + edge.getCost()){
    				edge.getDestination().setMinCost(u.getMinCost() + edge.getCost());
    				predecessor.put(u, edge);
    				queue.add(edge.getDestination());
    			}
    		}
    	}
    	return predecessor;
    }
    
    public void DFS(){
    	for(INode node: getAllNodes()){
    		if(!node.isVisited()){
    			Explore(node);
    		}
    	}
    }
    
    public void Explore(INode node){
    	node.setVisited(true);
    	node.setPreClock(clock);
    	clock+=1;
    	for(IEdge edge: node.getOutEdges()){
    		if(!edge.getDestination().isVisited()){
    			Explore(edge.getDestination());
    		}   		
    	}
    	node.setPostClock(clock);
    	clock+=1;
    }
    
    public HashMap<INode, IEdge> BFS(INode source){
    	HashMap<INode, IEdge> predecessor = new HashMap<>();
    	
    	for(INode node: getAllNodes()){
    		predecessor.put(node, null);
    	}
    	source.setMinCost(0.0);
    	Queue<INode> queue = new LinkedList<INode>();
    	queue.offer(source);
    	while(!queue.isEmpty()){
    		INode temp = queue.poll();
    		for(IEdge edge: temp.getOutEdges()){
    			if(edge.getDestination().getMinCost() == Double.MAX_VALUE){
    				edge.getDestination().setMinCost(edge.getOrigin().getMinCost()+1.0);
    				predecessor.put(edge.getDestination(), edge);
    				queue.offer(edge.getDestination());
    			}
    		}
    	}
    	return predecessor;
    }
}

