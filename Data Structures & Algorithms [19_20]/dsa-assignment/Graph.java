import java.util.ArrayList; // import the ArrayList class
import java.util.LinkedList; // Used to initalise a queue
import java.util.Map;
import java.util.Queue; // Used from recursive bft algo
import java.util.Stack;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
/**
 * Graph Data Structure
 *
 * @author (your name)
 * @version 30/04/2020
 */

public class Graph implements GraphInterface {
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>(); 
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    /**
     * Class constructor, sets verticies and edges of the graph
     *
     * @param  verticies  The verticies you want the graph to initially have
     * @param  edges  The edges you want the graph to initially have
     */
    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        if (vertices != null) { this.vertices = vertices; }
        if (edges != null) { this.edges = edges; }
    }

    public Vertex insertVertex(String n) {
        if (n == null) { return null; }
        Vertex v = new Vertex(n);
        vertices.add(v);
        return v;
    }

    public String removeVertex(Vertex v) {
        if (v == null) { return null; }

        String name = null;

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).equals(v)) {
                name = v.getName();
                ArrayList<Edge> vEdges = v.getEdgesList();

                for (Edge vIter: vEdges) {
                    for (int j = 0; j < edges.size(); j++) {
                        if ((edges.get(j)).equals(vIter)) {
                            edges.remove(j);
                        }
                    }
                }

                // remove vertex
                vertices.remove(i);
            }
        }

        return name;
    }

    public Edge insertEdge(Vertex v, Vertex w, String n) {
        if (v == null || w == null) { return null; }

        Edge e = new Edge(n, v, w);
        edges.add(e);

        for (Vertex vIter: vertices) {
            if (vIter.equals(v) || vIter.equals(w)) {
                vIter.addEdge(edges.get(edges.size() - 1));
            }
        }

        return e;
    }

    public String removeEdge(Edge e) {
        if (e == null) { return null; }

        Vertex o = e.getOrigin();
        Vertex d = e.getDestination();
        String edgeName = e.getName();

        // need to check if node is even in the list

        for (Vertex vIter: vertices) {
            if (vIter.equals(o) || vIter.equals(d)) {
                vIter.removeEdge(e);
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).equals(e)) {
                edges.remove(i);
            }
        }
        return edgeName;
    }

    public Vertex opposite(Edge e, Vertex v) {
        if (e == null || v == null) { return null; }

        Vertex o = e.getOrigin();
        Vertex d = e.getDestination();

        if (v.equals(o)) {
            return d;
        } else if (v.equals(d)) {
            return o;
        } else {
            return null;
        }
    }

    public ArrayList<Vertex> vertices() {
        return vertices;
    }

    public ArrayList<Edge> edges() {
        return edges;
    }

    public boolean areAdjacent(Vertex v, Vertex w) {

        if (v == null || w == null) { return false; }

        ArrayList<Edge> vEdges = v.getEdgesList();
        ArrayList<Edge> wEdges = w.getEdgesList();

        if (vEdges.size() <= 0 || wEdges.size() <= 0) {
            return false;
        }

        boolean vIsSmaller = vEdges.size() < wEdges.size();

        if (vIsSmaller) {
            // v is smaller
            return checkAdjacent(vEdges, w);

        } else {
            // w is smaller
            return checkAdjacent(wEdges, v);
        }
    }

    private boolean checkAdjacent(ArrayList<Edge> search, Vertex query) {
        for (Edge v: search) {
            if ( !(v.getOrigin().equals(v.getDestination())) && (v.getOrigin()).equals(query) || (v.getDestination()).equals(query)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Edge> incidentEdges(Vertex v) {
        return v.getEdgesList();
    }

    public String rename(NameCommon o, String n) {
        if (o == null || n == null) { return null; }
        String oldName = o.getName();
        o.setName(n);
        return oldName;
    }

    /**
     * ---------------------------------------------------------- Part Two ------------------------------------------------------------  
     */

    /**
     * Breadth first traversal from vertex v, if v is null breadth first traversal over whole graph connected and not connected
     *
     * @param  v  The vertex to be traversed from, if null traverse over whole graph
     */
    public void bftTraverse(Vertex v) {
        if (v == null) {
            for(int i = 0; i < vertices.size(); i++) {
                v = vertices.get(i);
                if (!v.getMark()) {
                    bft(v, false, false);
                    v.setMarked(true);
                }
            }

            System.out.println();
            unmarkAll();

        } else {
            bft(v, true, false);
        }
    }

    private void unmarkAll() {
        for(Vertex v: vertices) {
            if (v.getMark()) {
                v.setMarked(false);
            }
        }
    }

    private ArrayList<Vertex> bft(Vertex origin, boolean small, boolean other) {
        if (origin == null)
            return null;

        ArrayList<Vertex> out = new ArrayList<Vertex>();
        Queue<Vertex> traverseQueue = new LinkedList<Vertex>();

        traverseQueue.clear();
        traverseQueue.add(origin);

        while(!traverseQueue.isEmpty()){
            Vertex v = traverseQueue.remove();
            out.add(v);
            v.setMarked(true);
            if (!other) {
                System.out.print(v.getName() + " ");
            }

            ArrayList<Edge> vEdges = v.getEdgesList();

            for (Edge e: vEdges) {
                Vertex w = opposite(e, v);

                if (!w.getMark()){
                    w.setMarked(true);
                    traverseQueue.add(w);
                }
            }
        }

        if (small) {
            unmarkAll();
            System.out.println();
        }

        return out;
    }

    /**
     * Get all vertcies from vertex v
     *
     * @param  v  The vertex you want to get all the reachable vertcies from
     * @return    All the reachable vertcies from vertex v
     */
    public ArrayList<Vertex> allReachable(Vertex v) { 
        if (v == null ) { return null; }
        return bft(v, true, true);
    }

    /**
     * Returns if all vertcies are connected within the graph
     *
     * @return    True if all are connected, false if otherwise
     */
    public boolean allConnected() {
        if (vertices == null) { return false; }
        ArrayList<Vertex> vArrayList = allReachable(vertices.get(0));
        return (vArrayList.containsAll(vertices));
    }

    /**
     * Gets the shortest path between vertex u and v as an edge list
     *
     * @param  u  The starting vertex
     * @param  v  The destination vertex
     * @return    The shortest path between u and v
     */
    public ArrayList<Edge> mostDirectRoute(Vertex u, Vertex v) {
        if (u == null || v == null) { return null; }

        // Realised here that I could of made stuff so much simpler previously with maps and linked lists
        // but running low on time. Sorry!
        Map<Vertex, Integer> distance = new HashMap<Vertex, Integer>();
        Map<Vertex, Vertex> previous = new HashMap<Vertex, Vertex>();
        Map<Vertex, Boolean> visited = new HashMap<Vertex, Boolean>();
        ArrayList<Vertex> shortestPathVertex = new ArrayList<Vertex>();
        ArrayList<Edge> shortestPath = new ArrayList<Edge>();
        ArrayList<Vertex> neighbours = new ArrayList<Vertex>();

        Vertex origin = u;
        Vertex destination = v;

        int infinity = Integer.MAX_VALUE;
        int visitedCount = 0;

        unmarkAll();

        for (Vertex vertex: vertices) {
            distance.put(vertex, infinity);
            visited.put(vertex, false);
        }

        distance.put(u, 0);

        while (visited.size() > 0) {
            Vertex shortest = null;

            for (Vertex vertex: vertices) {
                if (visited.containsKey(vertex) &&shortest == null) {
                    shortest = vertex;
                } else if (visited.containsKey(vertex) && distance.get(vertex) < distance.get(shortest)) {
                    shortest = vertex;
                }
            }

            ArrayList<Edge> vEdges = shortest.getEdgesList();

            for (Edge e: vEdges) {
                Vertex w = opposite(e, shortest);
                neighbours.add(w);
            }

            for(Vertex neighbour: neighbours) {
                if (visited.containsKey(neighbour) && distance.containsKey(neighbour) && 1 + distance.get(shortest) < distance.get(neighbour)) {
                    distance.put(neighbour, (1 + distance.get(shortest)));
                    previous.put(neighbour, shortest);
                }
            }

            visited.remove(shortest);
        }

        boolean contains = false;
        ArrayList<Vertex> bReachable = allReachable(destination);
        for (Vertex ver: bReachable) {
            if (ver.equals(origin)) {
                contains = true;
            }
        }
        
        if (!contains) {
            return null;
        }
        
        Vertex currentVertex = destination;
        while (!currentVertex.equals(origin)) {
            shortestPathVertex.add(currentVertex);
            currentVertex = previous.get(currentVertex);
        }

        shortestPathVertex.add(origin);
        Collections.reverse(shortestPathVertex);

        for (int i = 1; i < shortestPathVertex.size(); i++) {
            Vertex pre = shortestPathVertex.get(i - 1);
            Vertex cur = shortestPathVertex.get(i);

            for (Edge e: cur.getEdgesList()) {
                if (pre == e.getOrigin() && cur == e.getDestination() || cur == e.getOrigin() && pre == e.getDestination()) {
                    shortestPath.add(e);
                }
            }
        }

        return shortestPath;
    }
}

