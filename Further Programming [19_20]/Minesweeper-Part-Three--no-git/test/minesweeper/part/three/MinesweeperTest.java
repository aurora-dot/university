/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper.part.three;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import minesweeper.part.three.Minefield;
import minesweeper.part.three.MinesweeperCMD;

/**
 *
 * @author 
 */
public class MinesweeperTest {

    MinesweeperCMD game;
    ByteArrayOutputStream stringOut;

    @Before
    public void setUp() {
        game = new MinesweeperCMD();
        Minefield m = new Minefield(3, 3);
        m.mineTile(1, 1);
        game.setMines(m);

        stringOut = new ByteArrayOutputStream();
        game.setOs(new PrintStream(stringOut, true));

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGame() {

    }

    @Test
    public void testQuit() {
        String inputString = "q\n";
        String expectedOutput = "...\n...\n...\nNew game\n> ";
        InputStream is = new ByteArrayInputStream(inputString.getBytes());
        game.setIs(is);
        game.commandLine();
        assertEquals(expectedOutput, stringOut.toString());
    }

    @Test
    public void testBoom() {
        String inputString = "s 1 1\n";
        String expectedOutput = "...\n...\n...\nNew game\n> "
                + "Command STEP, row=1, column=1\n"
                + "BOOM!!!\n";
        InputStream is = new ByteArrayInputStream(inputString.getBytes());
        game.setIs(is);
        game.commandLine();

        assertEquals(expectedOutput, stringOut.toString());
    }
    
    @Test
    public void testWin() {
        String inputString = "ma 1 1\n";
        String expectedOutput = "...\n...\n...\nNew game\n> "
                + "Command MARK, row=1, column=1\n"
                + "Hurrah!! All the mines are found.\n";
        InputStream is = new ByteArrayInputStream(inputString.getBytes());
        game.setIs(is);
        game.commandLine();

        assertEquals(expectedOutput, stringOut.toString());
    }
    
    @Test
    public void testBadCommand() {
        String inputString = "mat 1 1\nquit\n";
        String expectedOutput = "...\n...\n...\nNew game\n> "
                + "Command UNKNOWN, row=0, column=0\n"
                + "...\n...\n...\nUnknown command: mat\n> ";
        InputStream is = new ByteArrayInputStream(inputString.getBytes());
        game.setIs(is);
        game.commandLine();
        assertEquals(expectedOutput, stringOut.toString());
    }
}
