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

    // Tests if the minefield exists
    @Test
    public void testExistance() {
        Minefield m = new Minefield(10, 10, 10);
        assertTrue(m != null);
    }
    
    // Tests if populated with mines successfully
    @Test
    public void testSuccessfulPopulate() {
        int mM = 4; // Sets max mines to 4
        Minefield m = new Minefield(3, 5, mM); // Creates minefield with mM
        int mineCount = 0; // Sets the amount of counted mines to 0
        
        m.populate(); // Populates the arrays
        String s = m.toString(); // Gets the contents of minefield
        
        // Counts the mines
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) {
                mineCount++;
            }
        }
        
        // Checks if all mines were placed
        assertEquals(mM, mineCount);
    }
    
    // Tests if adding a mine then populating doesn't go over the maxMine of 10
    @Test
    public void testPopulateAfterAddingMine() {
        int mM = 10; // Sets max mines to 10
        Minefield m = new Minefield(10, 10, mM); // Creates minefield
        int mineCount = 0; // Sets amount of mines counted to 0
        
        m.mineTile(5,5); // Places a single mine
        m.populate(); // Populates minefield
        String s = m.toString(); // Converts minefield to string
        
        // Counts the mines
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) {
                mineCount++;
            }
        }
        
        // If maxMine is the amount of mines counted, success
        assertEquals(mM, mineCount);
    }
    
    // Tests if populating then adding a mine doesn't go over the maxMine of 10
    @Test
    public void testMineAfterPopulate() {
        int mM = 10; // Sets max mines to 10
        Minefield m = new Minefield(10, 10, mM); // Creates minefield
        int mineCount = 0; // Sets amount of mines counted to 0
        
        m.populate(); // Populates minefield
        m.mineTile(5,5); // Places a single mine
        String s = m.toString(); // Converts minefield to string
        
        // Counts the mines
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) {
                mineCount++;
            }
        }
        
        // If maxMine is the amount of mines counted, success
        assertEquals(mM, mineCount);
    }
    
    // Tests of all the details of the toString is correct and formatted
    @Test
    public void testSuccessfulToString() {  
        int r = 9; int c = 10; int mM = 10; // Sets dimentions and max mines
        Minefield m = new Minefield(r, c, mM); // Creates minefield
        int mineCount = 0; // Sets mines counted to 0
        int lineCount = 0; // Sets lines counted to 0
        
        m.populate(); // Populates the mninefield
        String s = m.toString(); // Gets minefield result
        
        // Counts lines and mines
        for(int i=0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) { mineCount++; }
            if(s.charAt(i) == "\n".charAt(0)) { lineCount++; }
        }
        
        // Assertions
        assertEquals(mM, mineCount); // Checks if correct amount of mines
        assertEquals(r, lineCount); // Checks for correct amount of lines
        assertEquals(r * c, s.length() - r); // Checks for correct element count
    }
    
    // Tests if a mine is added successfullt
    @Test
    public void testSuccessfulMineTile() {
        Minefield m = new Minefield(3, 7, 3); // Creates a minefield
        assertTrue(m.mineTile(2,5)); // Place mine and see if added successfully
    }
    
    // Tests if adding two mines in the same place returns false 2nd time adding
    @Test
    public void testExistsMineTile() {
        Minefield m = new Minefield(2, 10, 3); // Creates minefield
        m.mineTile(1,5); // Adds mine at 1,5
        assertFalse(m.mineTile(1,5)); // Tries again to see if returns false
    }
    
    // Tests if adding a mine after max mines is reached returns false
    @Test
    public void testFullMineTile() {
        Minefield m = new Minefield(10, 6, 3); // Creates minefield
        m.populate(); // Populates minefield to max
        assertFalse(m.mineTile(5,5)); // Asserts to see if doesn't go over cap
    }
    
    // Tests if it returns false to handled a dimention too large
    @Test
    public void testTooLargeMineTile() {
        Minefield m = new Minefield(2, 2, 1); // Creates minefield
        
        // Testing with ints larger than both or one dimentions
        assertFalse(m.mineTile(3,3)); // Tests out of bounds in both directions 
        assertFalse(m.mineTile(3,1)); // Tests out of bounds on row
        assertFalse(m.mineTile(1,3)); // Tests out of bounds on column
    }
    
    // Tests if it returns false to handled a dimention too small
    @Test
    public void testTooSmallMineTile() {
        Minefield m = new Minefield(2, 2, 1); // Creates minefield

        // Testing with ints smaller than both or one dimentions
        assertFalse(m.mineTile(-1,-1)); // Tests out of bounds in both direction
        assertFalse(m.mineTile(-1,1)); // Tests out of bounds on row
        assertFalse(m.mineTile(1,-1)); // Tests out of bounds on column
    }
    
    // Tests if correct amount of 1's and *'s are added
    @Test
    public void testSurroundingCountMineTile() {
        Minefield m = new Minefield(10, 10, 10); // Creates minefield
        int mineCount = 0; // Sets mines counted to 0
        int numCount = 0; // Sets number count to 0
        
        // Places a mine and gets the string of the array
        m.mineTile(5,5);
        String s = m.toString();
        
        // Counts each mine and each number
        for(int i=0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) { mineCount++; }
            if(s.charAt(i) == "1".charAt(0)) { numCount++; }
        }
        
        // Checks if correct amount is there
        assertEquals(1, mineCount);
        assertEquals(8, numCount);
    }
    
    // Tests if mine and numbers are in the correct place
    @Test
    public void testSurroundingMineTile() {
        Minefield m = new Minefield(10, 10, 10); // Creates minefield
        int cornerCount = 0; // Sets corner count to 0
        
        // Places a mine and gets the string of the array
        m.mineTile(5,5);
        String s = m.toString();
        
        // Splits the text into an array on each line break
        String[] split = s.split("\\n");
        
        // Iterates around the four corners of the mine numbers
        for(int r = 4; r < 7; r += 2) {
            for(int c = 4; c < 7; c += 2) {
                if (split[r].charAt(c) == "1".charAt(0)) {
                    cornerCount++;
                }
            }
        }
        
        // Tests if mine is in the correct position, tests if 4 corners exist
        assertEquals(5, split[5].indexOf("*"));
        assertEquals(4, cornerCount);   
    }
    
    // Tests with predefined strings to see if mine was placed
    @Test
    public void testToStringPreCalcString() {
        Minefield m = new Minefield(10, 10, 10); // Creates minefield
        
        // Correct way of displaying mines for mineTile below
        String preOne = "0000111000";
        String preTwo = "00001*1000";
        String preThree = "0000111000";
        
        // Sets mine, gets it as string, splits the array on each line break
        m.mineTile(5,5);
        String s = m.toString();    
        String[] split = s.split("\\n");
        
        // Tests if each line is outputted correctly
        assertEquals(preOne, split[4]);
        assertEquals(preTwo, split[5]);
        assertEquals(preThree, split[6]);
    }
    
    // Tests if it can handle a large dimention
    @Test
    public void testLargePopulate() {       
        // Used to set dimentions and max mines
        int r = 500; int c = 500; int mM = 5000; 
        
        Minefield m = new Minefield(r, c, mM); // Sets dimentions and max mines
        int mineCount = 0; // Sets the mines counted to 0
        int lineCount = 0; // Sets the lines counted to 0
        
        // Populates the array and gets the string result
        m.populate();
        String s = m.toString();
        
        // Iterates through the output to check foir each mine and correct lines
        for(int i=0; i < s.length(); i++) {
            if(s.charAt(i) == "*".charAt(0)) { mineCount++; }
            if(s.charAt(i) == "\n".charAt(0)) { lineCount++; }
        }
        
        // Checks if the mine cap was reached, correct rows and correct elements
        assertEquals(mM, mineCount);
        assertEquals(r, lineCount);
        assertEquals(r * c, s.length() - r);
    }
}
