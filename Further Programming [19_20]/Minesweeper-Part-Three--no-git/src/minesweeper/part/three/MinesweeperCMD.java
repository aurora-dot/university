/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper.part.three;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * The simple text version of the minesweeper game, using the parse and commands
 *
 * @author 
 */
public class MinesweeperCMD {


    public static final int DEFAULTROWS = 5;
    public static final int DEFAULTCOLS = 5;
    public static final int DEFAULTMINES = 5;
    private Minefield mines;
    private Parser parser;
    private PrintStream os = System.out;
    private InputStream is = System.in;

    /**
     * Create the desired size of game
     * @param numMines
     * @param rows
     * @param columns 
     */
    public MinesweeperCMD(int numMines, int rows, int columns) {
        this.mines = new Minefield(rows, columns);
        this.mines.populate(numMines);
    }

    /**
     * Create and populate a minefield with default sizes
     */
    public MinesweeperCMD() {
        this(DEFAULTMINES,DEFAULTROWS,DEFAULTCOLS);
    }

    /**
     * Start a game on the console
     * @param args
     */
    public static void main(String[] args) {
        MinesweeperCMD game = new MinesweeperCMD();

        game.commandLine();

    }

    private void printPrompt(String msg) {
        os.print(this.mines);
        os.println(msg);
        os.print("> ");
    }

    private boolean legalBounds(Command c) {
        return c.getRow() >= 0 && c.getRow() < mines.getRows() && c.getColumn() >= 0
                && c.getColumn() < mines.getColumns();
    }

    private boolean execute(Command c) {
        // As in the example code from the lecture, we use the enum CommandWord to 
        // decide on action
        switch (c.getCommand()) {
            case MARK: {
                // We have incorporated a legality check here, guaranteeing we always use
                // legal values.  
                if (legalBounds(c)) {
                    mines.toggleMark(c.getRow(), c.getColumn());
                } else {
                    System.out.println("Row " + c.getRow() + " must be 0 or more and less than "
                            + mines.getRows() + " and columns " + c.getColumn()
                            + " must be 0 or more and less than " + mines.getColumns());
                }
                break;
            }
            case STEP: {
                if (legalBounds(c)) {
                    if (!mines.step(c.getRow(), c.getColumn())) {
                        os.println("BOOM!!!");
                        return false;
                    }
                } else {
                    // Duplicate from the line above - perhaps this should be factored out
                    os.println("Row " + c.getRow() + " must be 0 or more and less than "
                            + mines.getRows() + " and columns " + c.getColumn()
                            + " must be 0 or more and less than " + mines.getColumns());

                }
                break;
            }
            default: break;
        }
        return true;
    }

    public void commandLine() {
        printPrompt("New game");
        this.parser = new Parser(is);
        Command c = parser.getCommand();
        while (c.getCommand() != CommandWord.QUIT) {
            os.println(c);
            if(!execute(c)) break;
            if (mines.areAllMinesFound()) {
                os.println("Hurrah!! All the mines are found.");
                break;
            }
            printPrompt(c.getMsg());
            c = parser.getCommand();
        }

    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }
    
    public void setMines(Minefield m) {
        this.mines = m;
    }

    public Parser getParser() {
        return parser;
    }


    public OutputStream getOs() {
        return os;
    }

    public void setOs(PrintStream os) {
        this.os = os;
    }

    
}
