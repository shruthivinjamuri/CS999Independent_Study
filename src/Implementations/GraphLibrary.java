package Implementations;

import java.util.HashMap;

import interfaces.IGraph;
import interfaces.IGraphLibrary;

public class GraphLibrary implements IGraphLibrary {
	HashMap<String, IGraph> graphs = new HashMap<>();
	@Override
	public void createGraph(String graphName, Double costInterval) {
		// TODO Auto-generated method stub
		Graph graph = new Graph(graphName, costInterval);
	}

	@Override
	public boolean addEdge(String graphName, String from, String to, Double cost) {
		// TODO Auto-generated method stub
		if(!graphs.containsKey(graphName)) return false;
		IGraph graph = graphs.get(graphName);
		graph.addEdge(from, to, cost);
		return true;
	}

	@Override
	public String computePath(String graph, String from, String to) {
		// TODO Auto-generated method stub
		return null;
	}

}
