/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeper;

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

    @Test
    public void testExistance() {
        Minefield m = new Minefield(10, 10, 10);
        assertTrue(m != null);
    }
    
    @Test
    public void testSuccessfulPopulate() {
        int mM = 4;
        Minefield m = new Minefield(3, 5, mM);
        int mineCount = 0;
        
        m.populate();
        String s = m.toString();
        
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) {
                mineCount++;
            }
        }
        
        assertEquals(mM, mineCount);
    }
    
    @Test
    public void testPopulateAfterAddingMine() {
        int mM = 10;
        Minefield m = new Minefield(10, 10, mM);
        int mineCount = 0;
        
        m.mineTile(5,5);
        m.populate();
        String s = m.toString();
        
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) {
                mineCount++;
            }
        }
        
        assertEquals(mM, mineCount);
    }
    
    @Test
    public void testSuccessfulToString() {  
        int r = 9; int c = 10; int mM = 10;
        Minefield m = new Minefield(r, c, c);
        int mineCount = 0;
        int lineCount = 0;
        
        m.populate();
        String s = m.toString();
        
        for(int i=0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) { mineCount++; }
            if(s.charAt(i) == "\n".charAt(0)) { lineCount++; }
        }
        
        assertEquals(mM, mineCount);
        assertEquals(r, lineCount);
        assertEquals(r * c, s.length() - r);
    }
    
    @Test
    public void testSuccessfulMineTile() {
        Minefield m = new Minefield(3, 7, 3);
        assertTrue(m.mineTile(2,5));
    }
    
    @Test
    public void testExistsMineTile() {
        Minefield m = new Minefield(2, 10, 3);
        m.mineTile(1,5);
        assertFalse(m.mineTile(1,5));
    }
    
    @Test
    public void testFullMineTile() {
        Minefield m = new Minefield(10, 6, 3);
        m.populate();
        assertFalse(m.mineTile(5,5));
    }
}
