/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeper;

import java.util.Random;

public class Minefield {
    // --- Initialises Vars ---
    // Creates mine arrays
    private boolean[][] minefield;
    private int[][] minedNeighbours;
    
    // Creates vars to keep track of the current and limit of mines to add
    private int maxMines;
    private int mineCount = 0;
    
    // Keeps track of initial size of array without having to call repeatedly
    private int[] size = new int[2];
    
    // Constructor, sets up the variables.
    public Minefield(int row, int column, int maxMines) {
        minefield = new boolean [row][column]; // Sets the dimentions of array
        minedNeighbours = new int[row][column]; // Sets the dimentions of array
        this.maxMines = maxMines;  // Sets max mines allowed to have in array
        size[0] = row; size[1] = column; // Saves the dimentions size into array
    }
    
    // Add mines to tiles, increments surrounding tiles by one.
    public boolean mineTile(int row, int column) {                
        // Checks if mines was reached sanitises input and checks if mine exists
        if (mineCount >= maxMines || row >= size[0] || column >= size[1] || 
                row < 0 || column < 0 || minefield[row][column]) {
            return false;
            
        } else {         
            // Adds a mine to position
            minefield[row][column] = true;
            
            // Increment surroundings
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = column - 1; c <= column + 1; c++) {  
                    if (!(r == size[0] || c == size[1] || r == -1 || c == -1)) {
                        minedNeighbours[r][c]++;
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
                
                // If a mine in this location add * to string, otherwise use int
                if (minefield[r][c]) {
                    s = s + "*";
                } else {
                    s = s + minedNeighbours[r][c];
                }
            }
            
            // Create newline where the next row starts
            s = s + ("\n");
        }
        
        // Returns the string
        return s;
    }
}
