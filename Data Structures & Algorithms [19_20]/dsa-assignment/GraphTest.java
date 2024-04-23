
import static org.junit.Assert.*;
import java.beans.Transient;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class GraphTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class GraphTest
{
    /**
     * Default constructor for test class GraphTest
     */
    public GraphTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testAreAllReachable() {
        Graph graph = new Graph(null, null);

        Vertex Brighton = graph.insertVertex("Brighton");
        Vertex Hove = graph.insertVertex("Hove");
        Vertex London = graph.insertVertex("London");
        Vertex Manchester = graph.insertVertex("Manchester");

        Edge BrightonToHove = graph.insertEdge(Brighton, Hove, "Brighton <-> Hove");
        Edge BrightonToLondon = graph.insertEdge(Brighton, London, "Brighton <-> London");
        Edge HoveToLondon = graph.insertEdge(Hove, London, "Hove <-> London");
        Edge LondonToManchester = graph.insertEdge(London, Manchester, "London <-> Manchester");

        assertTrue(graph.allConnected());

        Vertex Kingston = graph.insertVertex("Kingston");

        assertFalse(graph.allConnected());
    }

    @Test
    public void testBftTraverse() {
        Graph graph = new Graph(null, null);

        Vertex Brighton = graph.insertVertex("Brighton");
        Vertex Hove = graph.insertVertex("Hove");
        Vertex London = graph.insertVertex("London");
        Vertex Manchester = graph.insertVertex("Manchester");
        Vertex Kingston = graph.insertVertex("Kingston");

        Vertex Lyon = graph.insertVertex("Lyon");
        Vertex Paris = graph.insertVertex("Paris");

        Edge BrightonToHove = graph.insertEdge(Brighton, Hove, "Brighton <-> Hove");
        Edge BrightonToLondon = graph.insertEdge(Brighton, London, "Brighton <-> London");
        Edge HoveToLondon = graph.insertEdge(Hove, London, "Hove <-> London");
        Edge LondonToManchester = graph.insertEdge(London, Manchester, "London <-> Manchester");
        Edge HoveToKingston = graph.insertEdge(Hove, Kingston, "London <-> Manchester");
        Edge ManchesterToKingston = graph.insertEdge(Manchester, Kingston, "London <-> Manchester");

        Edge LyonToParis = graph.insertEdge(Lyon, Paris, "Lyon <=> Paris");

        System.out.println("--- BFT Traverse Test ---");
        graph.bftTraverse(Brighton);
        System.out.println();
        graph.bftTraverse(London);
        System.out.println();
        graph.bftTraverse(null);
    }

    @Test
    public void testAllReachable() {
        Graph graph = new Graph(null, null);

        Vertex Brighton = graph.insertVertex("Brighton");
        Vertex Hove = graph.insertVertex("Hove");
        Vertex London = graph.insertVertex("London");
        Vertex Manchester = graph.insertVertex("Manchester");
        Vertex Kingston = graph.insertVertex("Kingston");

        Vertex Lyon = graph.insertVertex("Lyon");
        Vertex Paris = graph.insertVertex("Paris");

        Edge BrightonToHove = graph.insertEdge(Brighton, Hove, "Brighton <-> Hove");
        Edge BrightonToLondon = graph.insertEdge(Brighton, London, "Brighton <-> London");
        Edge HoveToLondon = graph.insertEdge(Hove, London, "Hove <-> London");
        Edge LondonToManchester = graph.insertEdge(London, Manchester, "London <-> Manchester");
        Edge HoveToKingston = graph.insertEdge(Hove, Kingston, "Hove <-> Kingston");
        Edge ManchesterToKingston = graph.insertEdge(Manchester, Kingston, "Manchester <-> Kingston");

        Edge LyonToParis = graph.insertEdge(Lyon, Paris, "Lyon <=> Paris");

        ArrayList<Vertex> bReachable = graph.allReachable(Brighton);
        String out = "";

        for (Vertex v: bReachable) {
            out += String.format("%s, ", v.getName());
        }

        assertTrue("Brighton, Hove, London, Kingston, Manchester, ".equals(out));
    }

    private String mdrTest(Graph g, Vertex v, Vertex n) {
        String out = "";
        ArrayList<Edge> shortestPath = g.mostDirectRoute(v, n);

        if (shortestPath == null) { out = "null";}
        else {
            for (Edge e: shortestPath) {
                out += String.format("%s, ", e.getName());
            }
        }

        return out;
    }

    @Test
    public void testMostDirectRoute() {
        Graph graph = new Graph(null, null);

        Vertex Brighton = graph.insertVertex("Brighton");
        Vertex Hove = graph.insertVertex("Hove");
        Vertex London = graph.insertVertex("London");
        Vertex Manchester = graph.insertVertex("Manchester");
        Vertex Kingston = graph.insertVertex("Kingston");

        Vertex Lyon = graph.insertVertex("Lyon");
        Vertex Paris = graph.insertVertex("Paris");

        Edge BrightonToHove = graph.insertEdge(Brighton, Hove, "Brighton <-> Hove");
        Edge BrightonToLondon = graph.insertEdge(Brighton, London, "Brighton <-> London");
        Edge HoveToLondon = graph.insertEdge(Hove, London, "Hove <-> London");
        Edge LondonToManchester = graph.insertEdge(London, Manchester, "London <-> Manchester");
        Edge HoveToKingston = graph.insertEdge(Hove, Kingston, "Hove <-> Kingston");
        Edge ManchesterToKingston = graph.insertEdge(Manchester, Kingston, "Manchester <-> Kingston");

        Edge LyonToParis = graph.insertEdge(Lyon, Paris, "Lyon <=> Paris");

        assertTrue("Brighton <-> London, London <-> Manchester, ".equals(mdrTest(graph, Brighton, Manchester)));
        assertTrue("null".equals(mdrTest(graph, Brighton, null)));
        assertTrue("Brighton <-> Hove, Hove <-> Kingston, ".equals(mdrTest(graph, Brighton, Kingston)));
        assertTrue("null".equals(mdrTest(graph, Brighton, Lyon)));

    }
}
