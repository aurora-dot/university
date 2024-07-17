/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeperpt2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sun.misc.IOUtils;

public class MinesweeperTest {
    
    public MinesweeperTest() {
    }
    
    // I have no idea how to correctly set up the input and output streams, 
    // this is the main reason why I had to submit late 
    @Test
    public void testNewGame() {
        String[] args = null;
        Minesweeper ms = new Minesweeper();
        ms.main(null);
        byte b [] = "new 10 10".getBytes();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        System.out.println(outContent.toString());
    }
}
