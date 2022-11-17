package com.example.minesweepergui;

public class Tile {
    // What the actual value of this tile is (bomb, 3, etc)
    private String Actual;

    // What the display value of this tile is (Actual, blank, flag)
    private String Displayed;

    private boolean bIsRevealed = false;

    //constructor
    public Tile(String ActualValue, String ValueToDisplay) {
        this.Actual = ActualValue;
        this.Displayed = ValueToDisplay;
    }

    public String GetDisplay(){
        return this.Displayed;
    }

    public void SetDisplay(String toSet){
        this.Displayed = toSet;
    }

    public String GetActual(){
        return this.Actual;
    }
}