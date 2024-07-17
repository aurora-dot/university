/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper.part.three;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Minefield implements Serializable {

    private MineTile[][] minefield;

    private int rows, columns;

    /**
     * Create an empty minefield of the required size
     *
     * @param rows
     * @param columns
     */
    public Minefield(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        minefield = new MineTile[rows][columns];
        // We must fill out the array with objects this time, as opposed to arrays
        // of primitive values.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = new MineTile();
            }
        }
    }

    /**
     *
     * @return how many rows
     */
    public int getRows() {
        return rows;
    }

    /**
     *
     * @return how many columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Mine a tile
     *
     * @param row
     * @param column
     * @return false if tile already mined, true if successful
     */
    public boolean mineTile(int row, int column) {
        if (minefield[row][column].isMined()) {
            return false;
        }
        minefield[row][column].toggleMined();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (i >= 0 && j >= 0 && i < minefield.length
                        && j < minefield[i].length) {
                    minefield[i][j].incMinedNeighbours();
                }

            }
        }
        return false;
    }

    /**
     *
     * @return the minefield with tiles hidden
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                s += minefield[i][j];

            }
            s += "\n";
        }

        return s;
    }
    
    public void save(File file) throws Exception {
        List<List<Object>> listOfLists = new ArrayList<List<Object>>(); 
        
        ArrayList<Object> dimentions = new ArrayList<Object>();
        dimentions.add(rows);
        dimentions.add(columns);
        listOfLists.add(dimentions);
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                ArrayList<Object> data = new ArrayList<Object>();
                MineTile m = minefield[i][j];

                data.add(m.isMarked());
                data.add(m.isMined());
                data.add(m.isRevealed());
                data.add(m.getMinedNeighbours());
                
                listOfLists.add(data);
            }
        }

        try {      
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
                    
            out.writeObject(listOfLists);
            out.close();
            fout.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Saved Successfully", ButtonType.OK);
            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.toString(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void open(List<List<Object>> listOfLists) throws Exception {
        listOfLists.remove(0);
        int count = 0;
        
        try {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    List<Object> mineFields = listOfLists.get(count);

                    if ((boolean) mineFields.get(0)) {
                        minefield[i][j].toggleMarked();
                    }

                    if ((boolean) mineFields.get(1)) {
                        minefield[i][j].toggleMined();
                    }

                    if ((boolean) mineFields.get(2)) {
                        minefield[i][j].toggleRevealed();
                    }

                    minefield[i][j].setMinedNeighbours((int) mineFields.get(3));
                    
                    count++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.toString(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    /**
     *
     * @return the minefield with everything on show
     */
    public String toStringRevealed() {
        String s = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                s += minefield[i][j].toStringRevealed();

            }
            s += "\n";
        }

        return s;
    }

    /**
     * COnvenience method to print out to the console
     */
    public void printMinefield() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(minefield[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Populate the minefield randomly to num mines
     *
     * @param num
     */
    public void populate(int num) {
        int created = 0;
        while (created < num) {
            int row = (int) (Math.random() * this.rows);
            int col = (int) (Math.random() * this.columns);
            if (!minefield[row][col].isMined() && !(row == 0 && col == 0)) {
                mineTile(row, col);
                created++;
            }

        }
    }

    /**
     * Flag a given tile
     *
     * @param row
     * @param column
     */
    public void toggleMark(int row, int column) {
        this.minefield[row][column].toggleMarked();

    }
    
    public boolean isMarked(int row, int column) {
        return this.minefield[row][column].isMarked();
    }

    public int getMineNeighbour(int row, int column) {
        return this.minefield[row][column].getMinedNeighbours();
    }

    public boolean isRevealed(int row, int column) {
        return this.minefield[row][column].isRevealed();
    }

    public void toggleRevealed(int row, int column) {
        this.minefield[row][column].toggleRevealed();
    }
    
    public boolean isMined(int row, int column) {
        return this.minefield[row][column].isMined();
    }

    private void checkReveal(int row, int column) {
        // walk round the outside of the current tile
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                // Now that you've seen one approach to creating bounds in v1,
                // here we roll the check on bounds inside the loop
                if (i >= 0 && j >= 0 && i < minefield.length
                        && j < minefield[i].length) {
                    // Not the one just shown...
                    if (!(i == row && j == column)) {
                        // If its not already reveaed - recursion termination condition
                        if (!minefield[i][j].isRevealed()) {
                            // Note that this is the check on ending the recursion
                            minefield[i][j].setRevealed(true);
                            if (minefield[i][j].getMinedNeighbours() == 0) {
                                // recurse around here to see what also needs to be revealed
                                checkReveal(i, j);
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Step on a given tile
     *
     * @param row
     * @param column
     * @return false if the world explodes
     */
    public boolean step(int row, int column) {
        if (row >= 0 && row < getRows() && column >= 0 && column < getColumns()) {
            if (minefield[row][column].isMined()) {
                // The BOOM return
                return false;
            } else {
                // Otherwise reveal...
                minefield[row][column].setRevealed(true);
                // ...and show any other tiles needed
                if (minefield[row][column].getMinedNeighbours() == 0) {
                    checkReveal(row, column);
                }
            }

        }
        return true;

    }

    /**
     *
     * @return Has everything been correctly flagged
     */
    public boolean areAllMinesFound() {
        // Check all tiles for mined tiles marked and only mined tiles marked
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if ((minefield[i][j].isMined() && !minefield[i][j].isMarked())
                        || (!minefield[i][j].isMined() && minefield[i][j]
                        .isMarked())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param row
     * @param col
     * @return the desired tile
     */
    public MineTile getMineTile(int row, int col) {
        return minefield[row][col];
    }
}
