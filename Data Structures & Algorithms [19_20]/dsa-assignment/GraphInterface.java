import java.util.ArrayList;

/**
 * The graphs interface
 *
 * @author (your name)
 * @version 30/04/2020
 */

public interface GraphInterface {
    /**
     * Inserts a vertex into the graph.
     *
     * @param  n  The name you want to assign to the new vertex
     * @return    The new vertex which has been inserted
     */
    Vertex insertVertex(String n);

    /**
     * Removes a vertex from the graph and returns its name
     *
     * @param  v  The vertex you want to search for to remove
     * @return    The name of the vertex you deleted
     */
    String removeVertex(Vertex v);

    /**
     * Inserts a edege between two verticies
     *
     * @param  v  The first vertex you want the edge to connect to
     * @param  w  The second vertex you want to edge to connect to
     * @param  n  The name of the edge you want to create
     */
    Edge insertEdge(Vertex v, Vertex w, String n);

    /**
     * Removes an edge from the graph and retruns the edges name you removed
     *
     * @param  e  The edge you want to remove
     * @return    The name of the edge you removed
     */
    String removeEdge(Edge e);

    /**
     * Gets the vertex opposite of v on edge e
     *
     * @param  e  The edge to want to find the opposite node of
     * @param  v  The vertex you want to find the opposite of
     * @return    The opposite vertex
     */
    Vertex opposite(Edge e, Vertex v);

    /**
     * Returns the verticies of the graph
     *
     * @return    the verticies of the graph
     */
    ArrayList<Vertex> vertices();

    /**
     * Returns the edges of the graph
     *
     * @return    all edges within the graph
     */
    ArrayList<Edge> edges();

    /**
     * Checks if two verticies are adjacent
     *
     * @param  v  The first vertex you want to comapare
     * @param  w  The second vertex you want to comapare
     * @return    If v and w are adjacent, return true, else, false
     */
    boolean areAdjacent(Vertex v, Vertex w);

    /**
     * Gets incident edges of vertex v
     *
     * @param  v  The vertex you want to get the incident edges from
     * @return    the edges incident of v
     */
    ArrayList<Edge> incidentEdges(Vertex v);

    /**
     * Renames the vertex or edge
     *
     * @param  o  the object you want to rename
     * @param  n  the new name for the object
     * @return    the old name of o
     */
    String rename(NameCommon o, String n);
}
