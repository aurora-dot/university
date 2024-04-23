
/**
 * Edge representation class for graph
 *
 * @author (your name)
 * @version 30/04/2020
 */

public class Edge extends NameCommon {
    // it's still undirected, just used same var names for a directed edge
    private Vertex origin;
    private Vertex destination;

    /**
     * The constructor - Sets the name, and the two verticies it is connected to
     *
     * @param  name  The name of the edge to be set
     * @param  origin  The first vertex the edge is connected to
     * @param  destination  The second vertex the edge is connected to
     */
    public Edge(String name, Vertex origin, Vertex destination) {
        super(name);
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Gets the first vertex the edge is connected to
     *
     * @return    The first vertex the edge is connected to
     */
    public Vertex getOrigin() {
        return origin;
    }

    /**
     * Gets the second vertex the edge is connected to
     *
     * @return    The second vertex the edge is connected to
     */
    public Vertex getDestination() {
        return destination;
    }
}
