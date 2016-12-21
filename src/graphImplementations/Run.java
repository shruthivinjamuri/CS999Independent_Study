package graphImplementations;

import java.util.List;

import graphInterfaces.IEdge;

public class Run {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Edge ab = new Edge(a, b, 1.0);
		Edge bc = new Edge(b, c, 2.0);
		Edge ac = new Edge(a, c, 3.0);
		Graph graph1 = new Graph("Graph1", 5.0);
		graph1.addEdge(a.getLabel(),b.getLabel(), 1.0d);
		graph1.addEdge(b.getLabel(), c.getLabel(), 2.0d);
		graph1.addEdge(a.getLabel(), c.getLabel(), 3.0d);
		System.out.println(graph1);
		List<IEdge> edges = graph1.getAllEdges();
		for(IEdge edge: edges){
			System.out.println(edge.toString());
		}
		
		
	}

}
