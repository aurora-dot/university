/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeperpt2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MinefieldTest {
    
    public MinefieldTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Tests if it essentially flips the 
     * boolean which is stored in the mine tile
     */
    @Test
    public void testMark() {
        // Used to set dimentions and max mines
        int r = 10; int c = 10; int mM = 10; 
        Minefield m = new Minefield(r, c, mM);
        m.populate();
        
        // Tests if marks when unmarked
        m.mark(0, 0);
        char s = m.toString().charAt(0);
        assertEquals('+', s);
        
        // Tests if unmarks when marked
        m.mark(0, 0);
        s = m.toString().charAt(0);
        assertEquals('#', s);
    }
    
    /**
     * Tests if works correctly when there are no neighbours
     * next to the selected space
    */
    @Test
    public void testStep_NoNeighbour() {
        // Used to set dimentions and max mines
        int r = 5; int c = 5; int mM = 5;
        Minefield m = new Minefield(r, c, mM); // Sets dimentions and max mines
        
        
        String predefined = "##100\n" +
                            "##100\n" +
                            "11100\n" +
                            "00000\n" +
                            "00000\n";
        
        m.mineTile(1, 1);
        m.step(1,3);
        String result =  m.toString();
        
        assertEquals(predefined, result);
    }
    
    /**
     * Tests of reacts correctly when a mine is stepped on
     */
    @Test
    public void testStep_IsMine() {
        // Used to set dimentions and max mines
        int r = 5; int c = 5; int mM = 5;
        
        Minefield m = new Minefield(r, c, mM); // Sets dimentions and max mines
        
        String predefined = "#####\n" +
                            "**###\n" +
                            "#####\n" +
                            "#####\n" +
                            "#####\n";
        
        m.mineTile(1, 1);
        m.mineTile(1, 0);
        m.step(1,1);
        String result =  m.toString();
                
        assertEquals(predefined, result);
    }
    
    /**
     * Tests if stepping on a neighbour works correctly
     */
    @Test
    public void testStep_IsNeighbour() {
        // Used to set dimentions and max mines
        int r = 5; int c = 5; int mM = 5;
        
        Minefield m = new Minefield(r, c, mM); // Sets dimentions and max mines
        
        String predefined = "#####\n" +
                            "##1##\n" +
                            "#####\n" +
                            "#####\n" +
                            "#####\n";
        
        m.mineTile(1, 1);
        m.step(1,2);
        String result =  m.toString();
        
        assertEquals(predefined, result);
    }
    
    /**
     * Tests if returns true when only and all the mines have been marked
     */
    @Test
    public void tesallMinesRevealed_AllMines() {
        int r = 30; int c = 60; int mM = 40;
        Minefield m = new Minefield(r, c, mM);
        m.populate();
        
        String[] mines = m.getMinePositions();
        
        for (int i = 0; i < mM; i++) {
            String[] temp = mines[i].split(",");
            m.mark(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
        }
        
        assertTrue(m.allMinesRevealed());
    }

    /**
     * Tests if returns true when only the mines have been marked
     * with one being left out
     */
    @Test
    public void tesallMinesRevealed_SomeMines() {
        int r = 30; int c = 60; int mM = 40;
        Minefield m = new Minefield(r, c, mM);
        m.populate();
        
        String[] mines = m.getMinePositions();
        
        for (int i = 0; i < mM - 5; i++) {
            String[] temp = mines[i].split(",");
            m.mark(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
        }
        
        assertFalse(m.allMinesRevealed());
    }

    /**
     * Tests if it returns false when none have been marked
     */
    @Test
    public void tesallMinesRevealed_NoMines() {
        int r = 30; int c = 60; int mM = 40;
        Minefield m = new Minefield(r, c, mM);
        m.populate();
        
        String[] mines = m.getMinePositions();
        
        assertFalse(m.allMinesRevealed());
    }
    
    /**
     * Tests if it returns false all mines and one not mine is marked
     */
    @Test
    public void tesallMinesRevealed_AllMinesAndNot() {
        int r = 30; int c = 60; int mM = 40;
        Minefield m = new Minefield(r, c, mM);
        m.mineTile(1, 1);
        
        String[] mines = m.getMinePositions();
        
        m.mark(1,1);
        m.mark(1,2);
        
        assertFalse(m.allMinesRevealed());
    }
    
    /**
     * Tests if it returns false when only using neighbours
     */
    @Test
    public void tesallMinesRevealed_OnlyNeighbours() {
        int r = 30; int c = 60; int mM = 40;
        Minefield m = new Minefield(r, c, mM);
        m.mineTile(1, 1);
        
        String[] mines = m.getMinePositions();
        
        m.mark(1,2);
        m.mark(1,3);
        m.mark(1,4);
        
        assertFalse(m.allMinesRevealed());
    }
    
}
