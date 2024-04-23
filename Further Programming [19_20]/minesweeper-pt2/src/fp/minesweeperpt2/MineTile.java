/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.minesweeperpt2;

public class MineTile {
    private boolean mined;
    private boolean marked;
    private boolean hidden = true;
    private int neighbours = 0;
    
    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }
    
    public void incrimentNeighbours() {
        neighbours++;
    }
    
    @Override
    public String toString() {
        String r = "test";
        
        if (marked) {
            r = "+";
        } else if (hidden) {
            r = "#";
        } else if (mined) {
            r = "*";
        } else {
            r = String.valueOf(neighbours);
        }
        
        
        return r;
    }
}
