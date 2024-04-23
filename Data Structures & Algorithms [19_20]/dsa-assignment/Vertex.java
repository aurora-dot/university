import java.util.ArrayList; // import the ArrayList class
/**
 * Vertex representation class for graph
 *
 * @author (your name)
 * @version 31/04/2020
 */

public class Vertex extends NameCommon {
    // instance variables - replace the example below with your own
    private ArrayList<Edge> edges = new ArrayList<Edge>(); 
    private boolean marked = false;

    /**
     * Constructor - Sets name for vertex
     *
     * @param  name  the name of the vertex you are creating
     */
    public Vertex(String name) {
        super(name);
    }

    /**
     * Adds an edge to its edge list
     *
     * @param  e  The edge you want to add
     */
    public void addEdge(Edge e) {
        edges.add(e);
    }

    /**
     * The edge you want to remove from the vertex
     *
     * @param  e  The edge to remove
     * @return    The old name of the edge
     */
    public String removeEdge(Edge e) {
        for (Edge eIter: edges) {
            if (e.getName() == eIter.getName()) {
                return e.getName();
            }
        }
        return null;
    }

    /**
     * Returns the edges incident to this vertex
     *
     * @return    The edges incident to this vertex
     */
    public ArrayList<Edge> getEdgesList() {
        return edges;
    }

    public void setMarked(boolean b) {
        marked = b;
    }

    public boolean getMark() {
        return marked;
    }
}
