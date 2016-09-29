package interfaces;

public interface IGraphLibrary {
	public void createGraph(String graphName, Double costInterval); 
	public boolean addEdge(String graphName, String from, String to, Double cost);
	public String computePath(String graph, String from, String to);
}
