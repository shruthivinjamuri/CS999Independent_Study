package interfaces;

public interface IPath {
	public String toString();
	public Double costOfPath();
	public String fromLabel();
	public String toLabel();
	public void addEdgeToPath(IEdge edge);
}
