package com.example.minesweepergui;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.Scanner;

import static javafx.application.Application.launch;

public class Grid{
    public Tile[][] GridRows;

    private Button[][] Buttons;
    private int remainingTilesToSelect;
    private int errors = 0;
    private boolean bGameWonOrLost = false;

    private boolean bHasLaunchedJavaFX = false;
    public Grid(int RowsToSet, int Cols) {
        this.GridRows = new Tile[Cols][RowsToSet];
        remainingTilesToSelect = RowsToSet * Cols;
    }

    public void setButtons(Button[][] ButtonArrToSet) {
        Buttons = ButtonArrToSet;
    }

    private boolean bFlag = false;

    public void ToggleFlag(Button FlagButton){
        bFlag = !bFlag;
        if (bFlag){
            FlagButton.setText("Flag ON");
        }
        else FlagButton.setText("Flag OFF");
    }

    public void ButtonClicked(int row, int col){
        System.out.println("That tile's display value is " + GridRows[row][col].GetDisplay());
        if (bGameWonOrLost) return;
        if (bFlag){
            PlaceFlagAt(row, col);
            PrintGrid();
            GameplayLoop();
            return;
        }
        int outcome = MakeTileSelection(row, col);
        PrintGrid();

        if (outcome == 0){
            System.out.println("\n That tile has already been revealed.");
            GameplayLoop();
        }
        if (outcome == 2){
            System.out.println("\n Game lost!");
            for (int i = 0; i < GridRows.length; i++){
                for (int j = 0; j < GridRows[0].length; j++){
                    if (GridRows[i][j].GetActual() == " B "){
                        GridRows[i][j].SetDisplay(" B ");
                    }
                }
            }
            bGameWonOrLost = true;
        }

        if(GetHasWon()){
            System.out.println("\n Game won!");
            bGameWonOrLost = true;
        }
        GameplayLoop();
    }

    public boolean isInteger(String input){
        try {
            Integer.parseInt(input);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        try {
            Integer.valueOf(input);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
    public boolean isInteger(Character input){
        try {
            int tryThis = (int)input;
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e){
            return false;
        }
        return true;
    }

    public void PrintErrorMessage(String Message){
        if (errors < 5){
            System.out.println(Message);
        }
        else if (errors < 6){
            System.out.println("\nDid you read the previous 5 error messages?");
        }
        else if (errors < 7){
            System.out.println("\nI don't think you read the previous 5 error messages!");
        }
        else if (errors < 8){
            System.out.println("\nI'm going to print a few more errors next time you mess up. Try reading them this time!");
        }
        else if (errors < 10){
            System.out.println(Message);
        }
        else if (errors < 11) {
            System.out.println("\nAre you trying to break me?");
        }
        else if (errors < 12){
            System.out.println("\nFine. go ahead. Do that again and I'll break.");
        }
        else{
            for (int i = 0; i < GridRows.length; i++){
                for (int j = 0; j < GridRows[0].length; j++){
                    GridRows[i][j].SetDisplay("NAN");
                }
            }
        }
        errors++;
    }

    public void SetMines(double mineDensity){
        // Calculating mines to set
        int gridSize = this.GridRows.length * this.GridRows[0].length;
        int unsetMines = (int)Math.floor((gridSize * mineDensity));

        // Setting mines
        for (int i = 0; i < unsetMines; i++){
            SetRandomMine();
        }
    }

    public boolean GetHasWon(){
        return remainingTilesToSelect == 0;
    }

    public void SetRandomMine(){
        int counter = 0;
        boolean bMineSet = false;
        while (!bMineSet){
            int r = (int)Math.round(Math.random()*(GridRows.length-1));
            int c = (int)Math.round(Math.random()*(GridRows[0].length-1));
            if (this.GridRows[r][c] == null){
                this.GridRows[r][c] = new Tile(" B "," - ");
                bMineSet = true;
                remainingTilesToSelect--;
                return;
            }
            counter++;
            if (counter > 10){
                System.out.println("Warning: in SetRandomMine(), 10 or more random generation attempts failed");
            }
        }
    }
    // Loops over each and sets a tile if null.
    public void SetRemainingTiles(){
        // r for row, c for col
        for (int r = 0; r < GridRows.length; r++){
            for (int c = 0; c < GridRows[0].length; c++){
                if (GridRows[r][c] == null){
                    GridRows[r][c] = new Tile(FindTileValue(r, c)," - ");
                }
            }
        }
    }

    public String FindTileValue(int row, int col){ // this is inefficient - best to do this on bomb placement
        String output = "   ";
        int append = 0;
        for (int r = -1; r < 2; r++){
            if ((row + r) < GridRows.length && (row+r) >= 0) {
                for (int c = -1; c < 2; c++) {
                    if ((col + c) < GridRows[0].length && (col + c) >= 0){
                        if (GridRows[row + r][col + c] != null){
                            if (GridRows[row + r][col + c].GetActual() == " B ") append++;
                        }
                    }
                }
            }
        }
        if (append > 0) output = " " + append + " ";
        return output;
    }

    public void PrintGrid(){
        System.out.println(" ");
        int y = this.GridRows.length-1;
        int x = 0;
        for (int rows = 0; rows < this.GridRows.length; rows++){
            System.out.print(y);
            if (y < 10) System.out.print(" ");
            System.out.print("|");
            y--;
            for (int cols = 0; cols < this.GridRows[0].length; cols++){
                if (this.GridRows[rows][cols] == null) System.out.print(" x ");
                else System.out.print(this.GridRows[rows][cols].GetDisplay());
            }
            System.out.print("\n");
        }
        // Printing x axis
        System.out.print("   ");
        for (int cols = 0; cols < this.GridRows[0].length; cols++){
            System.out.print("___");
        }
        System.out.print("\n");
        System.out.print("   ");
        for (int cols = 0; cols < this.GridRows[0].length; cols++){
            if (x < 10) System.out.print(" " + x + " ");
            else System.out.print(" " + x);
            x++;
        }
    }

    // Big question
    // Won't this just repeatedly place GameplayLoop(GameplayLoop(GameplayLoop())) on the stack?

    public boolean IsValidGameplayEntry(String Entry){
        // either int,int or flag int,int
        if (Entry.length() < 3 || !Entry.contains(",")) return false;
        if (Entry.contains("flag ")){
            if (!Entry.substring(0,5).equals("flag ") || Entry.length() < 8) return false;
            Entry = Entry.substring(5,Entry.length());
        }
        String[] TwoIntegers = Entry.split(",");
        if (TwoIntegers[0].length() == 0 || TwoIntegers[1].length() == 0) return false;
        if (!(isInteger(TwoIntegers[0]) && isInteger(TwoIntegers[1]))) return false;
        if (Integer.valueOf(TwoIntegers[0]) >= GridRows[0].length || Integer.valueOf(TwoIntegers[1]) >= GridRows.length) return false;
        return true;
    }
    public void GameplayLoop(){

        // DEBUGGING
//        System.out.println("Buttons row length: " + Integer.toString(Buttons.length));
//        System.out.println("Buttons col length: " + Integer.toString(Buttons[0].length));

        // Update buttons UI
        for (int rows = 0; rows < Buttons.length; rows++) {
            for (int cols = 0; cols < Buttons[0].length; cols++) {
                Buttons[cols][rows].setText(GridRows[rows][cols].GetDisplay());
                if (GridRows[rows][cols].GetDisplay() == " - "){
                    Buttons[cols][rows].setTextFill(Color.BLACK);
                    Buttons[cols][rows].setBackground(Background.fill(Color.LIGHTSTEELBLUE));
                }
                else{
                    Buttons[cols][rows].setBackground(Background.fill(Color.WHITESMOKE));
                }
                if (GridRows[rows][cols].GetDisplay() == " B ") Buttons[cols][rows].setTextFill(Color.RED);
                if (GridRows[rows][cols].GetDisplay() == " P "){
                    Buttons[cols][rows].setTextFill(Color.BLACK);
                    Buttons[cols][rows].setBackground(Background.fill(Color.LIGHTSLATEGRAY));
                }
                if (GridRows[rows][cols].GetDisplay().equals(" 1 ")) Buttons[cols][rows].setTextFill(Color.BLUE);
                if (GridRows[rows][cols].GetDisplay().equals(" 2 ")) Buttons[cols][rows].setTextFill(Color.GREEN);
                if (GridRows[rows][cols].GetDisplay().equals(" 3 ")) Buttons[cols][rows].setTextFill(Color.DARKORANGE);
                if (GridRows[rows][cols].GetDisplay().equals(" 4 ")) Buttons[cols][rows].setTextFill(Color.PURPLE);
                if (GridRows[rows][cols].GetDisplay() == "   "){
                    Buttons[cols][rows].setTextFill(Color.BLACK);
                    Buttons[cols][rows].setBackground(Background.fill(Color.WHITESMOKE));
                }
//                // This line for debugging purposes only.
//                Buttons[cols][rows].setText("r" + Integer.toString(rows) + " c" + Integer.toString(cols));
            }
        }
        if (bGameWonOrLost) return;
        if (!bHasLaunchedJavaFX){
            bHasLaunchedJavaFX = true;
            return;
        }


        // the below needs to happen on a new function (on clicked)

        // get input
//        System.out.println("\n Enter an x,y coordinate to check next, or place flag by typing flag x,y");
//        Scanner userInput = new Scanner (System.in);
//        String Response = userInput.nextLine();
//        if (!IsValidGameplayEntry(Response)){
//            PrintErrorMessage("\nError: invalid entry.");
//            GameplayLoop(Buttons);
//        }
//        boolean bPlaceFlag = false;
//        if (Response.contains("flag ")){
//            Response = Response.split(" ")[1];
//            bPlaceFlag = true;
//        }
//
//        int c = -1; // x is col
//        int r = -1; // y is row
//        int temp = 0;
//        for (int i = 0; i < Response.length(); i++){
//            if (Response.charAt(i) == ','){
//                c = temp;
//                temp = 0;
//            }
//            else {
//                if (!isInteger(Response.charAt(i))){
//                    PrintErrorMessage("\nError: input not entered in x,y format. Try again.");
//                    PrintGrid();
//                    GameplayLoop(Buttons);
//                    return;
//                }
//                temp = temp*10 + Character.getNumericValue(Response.charAt(i));
//            }
//        }
//        r = (GridRows.length - 1) - temp;
//
//        // check that r and c are not -1
//
//        if (bPlaceFlag){
//            PlaceFlagAt(r, c);
//            PrintGrid();
//            GameplayLoop(Buttons);
//            return;
//        }
//
//        int outcome = MakeTileSelection(r, c);
//        PrintGrid();
//
//        if (outcome == 0){
//            System.out.println("\n That tile has already been revealed.");
//            GameplayLoop(Buttons);
//        }
//        if (outcome == 2){
//            System.out.println("\n Game lost!");
//            bGameWonOrLost = true;
//            return;
//        }
//
//        if(GetHasWon()){
//            System.out.println("\n Game won!");
//            bGameWonOrLost = true;
//            return;
//        }
//        GameplayLoop(Buttons);
    }

    public void PlaceFlagAt(int row, int col){
        Tile Current = GridRows[row][col];
        if (Current.GetDisplay() == " P "){
            Current.SetDisplay(" - ");
            return;
        }
        if (Current.GetDisplay() == Current.GetActual()){
            System.out.println("You already know what that tile is.");
            System.out.println("Current Display is " + Current.GetDisplay());
            System.out.println("Current Actual is " + Current.GetActual());
            return;
        }
        else Current.SetDisplay(" P ");
    }

    // Updates a tile after it has been selected. Makes it visible.
    // Returns a number based on the outcome. 0: you've already selected this
    // 1: perfectly fine
    // 2: bomb chosen
    public int MakeTileSelection(int row, int col){
        Tile Selected = GridRows[row][col];
        boolean bTileIsRevealed = Selected.GetActual() == Selected.GetDisplay();
        if (bTileIsRevealed) return 0;
        if (GridRows[row][col].GetDisplay() == " P ") return 0;
        if (Selected.GetActual() == " B ") {
            Selected.SetDisplay(Selected.GetActual());
            return 2;
        }
        if (Selected.GetActual() == "   ") RevealAdjacentBlankTiles(row, col);
        else {
            remainingTilesToSelect--;
        }
        Selected.SetDisplay(Selected.GetActual());
        return 1;
    }

    public void RevealAdjacentBlankTiles(int r, int c){
        Tile CurrentTile = GridRows[r][c];
        if (CurrentTile.GetActual() != "   "){
            if (CurrentTile.GetActual() == " B ") return;
            if (CurrentTile.GetActual() == CurrentTile.GetDisplay()) return;
            CurrentTile.SetDisplay(CurrentTile.GetActual());
            remainingTilesToSelect--;
            return;
        }
        if (CurrentTile.GetDisplay() == CurrentTile.GetActual()) return;
        remainingTilesToSelect--;
        CurrentTile.SetDisplay(CurrentTile.GetActual());
        if (r > 0){
            RevealAdjacentBlankTiles(r-1,c);
            if (c > 0) RevealAdjacentBlankTiles(r-1, c-1);
            if (c < (GridRows[0].length - 1)) RevealAdjacentBlankTiles(r-1, c+1);
        }
        if (r < (GridRows.length - 1)){
            RevealAdjacentBlankTiles(r+1,c);
            if (c > 0) RevealAdjacentBlankTiles(r+1, c-1);
            if (c < (GridRows[0].length - 1)) RevealAdjacentBlankTiles(r+1, c+1);
        }
        if (c > 0) RevealAdjacentBlankTiles(r, c-1);
        if (c < (GridRows[0].length - 1)) RevealAdjacentBlankTiles(r, c+1);
    }
}