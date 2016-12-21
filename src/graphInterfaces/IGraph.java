package graphInterfaces;
import java.util.List;

/*
 * This is the specification of a directed graph, whose nodes are label with 
 * edges between them with a traversal cost. 
 * Also provides a specification to find a path between two nodes and returns
 * the cost if a path is found.
 */
public interface IGraph {
	// Adds a new edge between 'from' and 'to' nodes with a cost obeying the
	// triangle in-equality
	public IEdge addEdge(String from, String to, Double cost);
	
	// Returns a node, creates if not exists, corresponding to the label
	public INode addOrGetNode(String label);
	
	// Returns a list of all nodes present in the graph
	public List<INode> getAllNodes();
	
	// Returns a list of all edges present in the graph
	public List<IEdge> getAllEdges();
	
	// Joins two graphs that operate on the same cost interval and 
	// disjoint sets of nodes
	public void joinGraph(IGraph graph);
	
	// Returns the maximum cost allowed for the edges
	public Double getCostInterval();
	
	// Returns a Path with cost between 'from' and 'to' nodes
	public String getPath(String from, String to, String pathAlgo);
}
