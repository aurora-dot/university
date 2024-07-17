/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeperpt2;

import fp.minesweeperpt2.MineTile;
import java.util.Arrays;
import java.util.Random;

public class Minefield {
    // --- Initialises Vars ---
    // Creates mine arrays
    private MineTile[][] minefield;
    
    // Creates vars to keep track of the current and limit of mines to add
    private int maxMines;
    private int minesShown = 0;
    private int mineCount = 0;
       
    private int flaggedMineCount;
    private int flaggedNotMineCount;
    
    // Keeps track of initial size of array without having to call repeatedly
    private int[] size = new int[2];
    private final String[] mineLocations;
    
    // Constructor, sets up the variables.
    public Minefield(int row, int column, int maxMines) {
        minefield = new MineTile[row][column]; // Sets the dimentions of array
        this.maxMines = maxMines;  // Sets max mines allowed to have in array
        size[0] = row; size[1] = column; // Saves the dimentions size into array
        mineLocations = new String[maxMines];
        clean(); // Initialises every position in the minefield as a minetile
    }
    
    // Add mines to tiles, increments surrounding tiles by one.
    public boolean mineTile(int row, int column) {                
        // Checks if mines was reached sanitises input and checks if mine exists
        if (mineCount >= maxMines || row >= size[0] || column >= size[1] || 
                row < 0 || column < 0 || minefield[row][column].isMined()) {
            return false;
            
        } else {         
            // Adds a mine to position, mine location to array
            minefield[row][column].setMined(true);
            mineLocations[mineCount] = row + "," + column;
            
            // Increment surroundings
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = column - 1; c <= column + 1; c++) {  
                    if (!(r == size[0] || c == size[1] || r == -1 || c == -1)) {
                        minefield[r][c].incrimentNeighbours();
                    }      
                }
            }
            
            // Increments mineCount, returns val
            mineCount++;
            return true;
        }
    }

    // Randomly places mines accross the minefield.
    public void populate() {       
        boolean b; // Used to store if mine was successfully added
        int maxM = maxMines; // Used so you don't alter original val
        Random rand = new Random(); // Used to create random int    
        int r, c = 0; // To store random int values
        
        // Randomly places mines on grid.
        for (int m = 0; m < maxM; m++) {
            
            // Sets random row and column values, repeats if row and column is 0
            do {
                r = rand.nextInt(size[0]); 
                c = rand.nextInt(size[1]);
            }
            while (r == 0 && c == 0);
            
            // Sets tile and gets returned value
            b = mineTile(r, c);
            
            // If returned value was false, you can add a mine if not exceeded
            if (!b && mineCount < maxMines) { maxM++; }
        } 
    }
    
    // Concat: minefield and minedNeighbours, and return String. 
    @Override
    public String toString() {
        String s = ""; // Initialises var to hold minefield
        
        // Concatonates both arrays, minefield "true" is priority
        for (int r = 0; r < size[0]; r++) {
            
            for (int c = 0; c < size[1]; c++) {
                s = s + minefield[r][c].toString();
            }
            
            // Create newline where the next row starts
            s = s + ("\n");
        }
        
        // Returns the string
        return s;
    }
    
    /**
     * Flips the boolean value of mark if it is hidden, and then adds to
     * the appropriate counter to check later if the correct positions have
     * been marked
     */
    public void mark(int row, int column) {
        if (minefield[row][column].isHidden()) {
            minefield[row][column].setMarked(!minefield[row][column].isMarked());
            
            // is mine and maked
            if (minefield[row][column].isMarked() && minefield[row][column].isMined()) { 
                flaggedMineCount++;
              // isn't mine but marked
            } else if (minefield[row][column].isMarked() && !minefield[row][column].isMined()) { 
                flaggedNotMineCount++;
              // is mine but unmarked
            } else if (!minefield[row][column].isMarked() && minefield[row][column].isMined()) { 
                flaggedMineCount--;
              // isn't mined and unmarked
            } else if (!minefield[row][column].isMarked() && !minefield[row][column].isMined()) { 
                flaggedMineCount--;
            }
        }
    }
    
    /**
     * Recursive method, which checks first if you stepped on a mine, otherwise 
     * it will show the surrounding tiles with no neighbours until it reaches 
     * the first neighbour of a mine, like the normal game would do
     */
    public boolean step(int row, int column) {
        if (minefield[row][column].isMined()) {
            showAllMines();
            return false;
            
        } else { 
            
            if (minefield[row][column].getNeighbours() > 0) {
                minefield[row][column].setHidden(false);
                
            } else {
                for (int r = row - 1; r <= row + 1; r++) {
                    for (int c = column - 1; c <= column + 1; c++) {  
                        if (!(r == size[0] || c == size[1] || r == -1 || c == -1)) { 
                            if (minefield[r][c].isHidden()) {
                                if (minefield[r][c].isMarked()) {
                                    mark(r, c);
                                }
                                minefield[r][c].setHidden(false);
                                step(r, c);
                            }
                        }      
                    }
                }                
            }
            
            return true;
        }
    }
    
    /**
     * Checks if all the mines have been revealed and only the mines
     */
    public boolean allMinesRevealed() {
        if (flaggedMineCount == maxMines && flaggedNotMineCount == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Makes all the mines in the minefield visible
     */
    public void showAllMines() {
        for (int i = 0; i < mineCount; i++) {
            String[] temp = mineLocations[i].split(",");
            minefield[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])].setHidden(false);
        }
    }
    
    /**
     * Used to debug when trying to test allMinesRevealed
     */
    public String[] getMinePositions() {
        return mineLocations;
    }
    
    /**
     * Used to initialise the minefield array
     */
    public void clean() {
        for (int r = 0; r < size[0]; r++) {
            for (int c = 0; c < size[1]; c++) {
               minefield[r][c] = new MineTile();
            }
        }
    }
}
