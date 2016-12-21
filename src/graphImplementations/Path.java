package graphImplementations;

import java.util.ArrayList;
import java.util.List;

import graphInterfaces.IEdge;
import graphInterfaces.INode;
import graphInterfaces.IPath;

public class Path implements IPath {
       
       private INode source;
       private INode destination;
       private List<IEdge> path;
       private Double costOfPath;
       
       public Path(INode source, INode destination, Double costOfPath){
              this.source = source;
              this.destination = destination;
              this.path = new ArrayList<>();
              this.costOfPath = costOfPath;
       }

       public Double costOfPath() {
              
              return this.costOfPath;
       }

       public String fromLabel() {

              return source.getLabel();
       }

       public String toLabel() {

              return destination.getLabel();
       }

       public void addEdgeToPath(IEdge edge) {
                     path.add(edge);
              
       }
       
       public List<IEdge> getPath(){
              return path;
       }
       
}

