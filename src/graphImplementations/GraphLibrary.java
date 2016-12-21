package graphImplementations;

import java.util.HashMap;

import graphInterfaces.IGraph;
import graphInterfaces.IGraphLibrary;

public class GraphLibrary implements IGraphLibrary {
	private HashMap<String, IGraph> graphs = new HashMap<>();
	
	@Override
	public void createGraph(String graphName, Double costInterval) {
		if(!graphs.containsKey(graphName)){
		IGraph graph = new Graph(graphName, costInterval);
		graphs.put(graphName, graph);
		}
	}

	@Override
	public boolean addEdge(String graphName, String from, String to, Double cost) {
		if(!graphs.containsKey(graphName)) return false;
		IGraph graph = graphs.get(graphName);
		graph.addEdge(from, to, cost);
		return true;
	}

	@Override
	public String computePath(String graph, String from, String to, String pathAlgo) {
		if(!graphs.containsKey(graph)) return "<false />";
		return graphs.get(graph).getPath(from, to, pathAlgo);
		
	}

}
