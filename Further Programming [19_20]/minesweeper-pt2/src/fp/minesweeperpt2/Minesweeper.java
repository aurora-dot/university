/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeperpt2;

import java.util.Random;

public class Minesweeper
        
{
    private Parser parser = new Parser();
    private Minefield minefield;
    private boolean lock = true;
    
    public Minesweeper() {
        
    }
    
    private void execute(Command c) {
        switch(c.getCommand()) {
            case NEW:
                if (c.getRow() <= 0 || c.getColumn() <= 0 || c.getRow() > 50 || c.getColumn() > 50) {        
                    System.out.println("Invalid dimentions, "
                            + "please use above 0 to 50 for either dimention.");      
                    
                } else {
                    int mines;
                    Random rand = new Random();
                    do { mines = rand.nextInt(c.getColumn());}
                    while (mines == 0);

                    minefield = new Minefield(c.getRow(), c.getColumn(), mines);
                    minefield.populate();
                    
                    // DEBUG: minefield.getMinePositions();
                    lock = false;
                    System.out.println(minefield.toString());
                }
                               
                break;
            case STEP: case MARK:
                if (!lock) {
                    System.out.println(c);         
                    boolean stepResult = true;

                    if (c.getCommand() == CommandWord.STEP) {
                       stepResult = minefield.step(c.getRow(), c.getColumn());

                    } else if (c.getCommand() == CommandWord.MARK) {
                       minefield.mark(c.getRow(), c.getColumn());
                    }

                    System.out.println(minefield.toString());

                    if (!stepResult) {
                        System.out.println("You Lost!");
                        lock = true;

                    } else if (minefield.allMinesRevealed()) {
                        System.out.println("You won!");
                        lock = true;
                    }
                } else {
                    System.out.println("Please start a new game");
                }
                break;
            case QUIT: System.out.println(c.getMsg());
                break;
            default:
                System.out.println(c);
            }
            printPrompt(c.getMsg());
        }
    
    private void commandLine() {
        printPrompt("Welcome!\nPlease start a new Game.");
        Command c = parser.getCommand();
        while(c.getCommand() != CommandWord.QUIT) {
            execute(c);
            c = parser.getCommand();
        }
    }
    
    private void printPrompt(String msg) {
        System.out.println(msg);
        System.out.print("> ");
    }
    
    public static void main(String args[]) {
        Minesweeper ms = new Minesweeper();
        ms.commandLine();
    }
}
