package minesweeper.part.three;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import minesweeper.part.three.Minefield;

/**
 *
 * @author 
 */
public class MinefieldTest {

    // Mine the top row, then the corner
    private final String topCentre = "1*1\n111\n000\n";
    private final String thenBottomRight = "1*1\n122\n01*\n";
    // Do it again, but with a clean minefield
    private final String cleanTopRight = "01*\n011\n000\n";
    private final String thenBottomLeft = "01*\n121\n*10\n";
    private final String hiddenBottomRight = "....\n....\n....\n....\n";
    private final String revealedBottomRight = "0000\n0000\n0011\n001.\n";
    private final String markedBottomRight = "....\n....\n....\n...@\n";
   
    @Test
    public void testInsertion() {
        // This is the minimum set of tests to ensure correctness of
        // the updating of adjacent tiles.
        // Thee are many other more complete approaches
        Minefield mf = new Minefield(3, 3);
        mf.mineTile(0, 1); 
        assertEquals(topCentre, mf.toStringRevealed());
        mf.mineTile(2, 2);
        assertEquals(thenBottomRight,mf.toStringRevealed());
        Minefield mf1 = new Minefield(3, 3);
        mf1.mineTile(0, 2);
        assertEquals(cleanTopRight,mf1.toStringRevealed());
        mf1.mineTile(2,0);
        assertEquals(thenBottomLeft,mf1.toStringRevealed());
        
    }

    @Test
    public void testPopulate() {
        // This is a one off test.  One might argue that this is a random 
        // method, so should be called multiple times.  However, given the
        // mine occupancy used, there are multiple collisions with very
        // high probability
        Minefield mf = new Minefield(10, 10);
        mf.populate(90);
        String s = mf.toStringRevealed();
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '*') {
                counter++;
            }
        }
        assertEquals(90, counter);

    }
    
    @Test
    public void testReveal() {
        Minefield mf = new Minefield(4,4);
        mf.mineTile(3,3);
        assertEquals(hiddenBottomRight,mf.toString());
        mf.step(0, 0);
        assertEquals(revealedBottomRight,mf.toString());
    }
    
    @Test
    public void testMark() {
        Minefield mf = new Minefield(4,4);
        mf.toggleMark(3,3);
        assertEquals(markedBottomRight,mf.toString());
    }
    
    @Test
    public void testBoom() {
        Minefield mf = new Minefield(4,4);
        mf.mineTile(3,3);
        assertEquals(false,mf.step(3,3));
    }
    
    @Test
    public void testAreAllMinesFound() {
        Minefield mf = new Minefield(4,4);
        mf.mineTile(2,2);
        mf.mineTile(3,3);
        mf.toggleMark(2,2);
        assertEquals(false,mf.areAllMinesFound());
        mf.toggleMark(2,3);
        assertEquals(false,mf.areAllMinesFound());
        mf.toggleMark(3,3);
        assertEquals(false,mf.areAllMinesFound());
        mf.toggleMark(2,3);
        assertEquals(true,mf.areAllMinesFound());
                
    }
}
