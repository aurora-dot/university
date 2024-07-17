/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper.part.three;

import java.io.ByteArrayInputStream;

public class CommandExample
        
{
    Parser parser = new Parser(System.in);
    public CommandExample() {
        
    }
    
    private void execute(Command c) {
        
        switch(c.getCommand()) {
            case STEP: case MARK: 
                System.out.println(c);
                break;
            case QUIT: System.out.println(c.getMsg());
                break;
            default:
                System.out.println(c);
            }
            printPrompt(c.getMsg());
        }
    
    private void commandLine() {
        printPrompt("New Game");
        Command c = parser.getCommand();
        while(c.getCommand() != CommandWord.QUIT) {
            execute(c);
            c = parser.getCommand();
        }
    }
    private void printPrompt(String msg) {
        System.out.println(msg);
        System.out.print(">");
    }
public static void main(String args[]) {
    CommandExample ct = new CommandExample();
    ct.commandLine();
}
}
