/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper.part.three;

import javafx.scene.control.Button;

/**
 *
 * @author epann
 */
public class MineButton extends Button {
    private int[] position;
    
    public MineButton(String buttonContent, int row, int column) {
        super(buttonContent);
        position = new int[2];
        position[0] = row;
        position[1] = column;
    }
    
    public int[] getPosition() {
        return position;
    }
}
