package com.example.minesweepergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import java.util.*;

import java.io.IOException;

public class MinesweeperGUI extends Application {
    public static Button[][] ButtonArray;
    public static Grid GameGrid;
    @Override
    public void start(Stage window) throws IOException {
        window.setTitle("Stage Title");

        GridPane Grid = new GridPane();
        Grid.setPadding(new Insets(10, 10, 10, 10));
        Grid.setVgap(8);
        Grid.setHgap(8);

        // adds each button to the grid in the right location, and binds it to an action.
        for (int i = 0; i < ButtonArray.length; i++){
            for (int j = 0; j < ButtonArray[0].length; j++){
                Button button = ButtonArray[i][j];
                GridPane.setConstraints(button, i, j);
                Grid.getChildren().add(button);
            }
        }
        Button FlagButton = new Button("Flag OFF");
        FlagButton.setOnAction(e -> {
            GameGrid.ToggleFlag(FlagButton);
        });
        GridPane.setConstraints(FlagButton, ButtonArray.length, 0);

        Grid.getChildren().add(FlagButton);

        Scene scene = new Scene(Grid, 40*ButtonArray.length + 65, 35*ButtonArray[0].length);
        window.setScene(scene);
        window.show();
    }

    public static boolean isInteger(String input){
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
    public static boolean isInteger(Character input){
        try {
            int tryThis = (int)input;
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e){
            return false;
        }
        return true;
    }
    public static Grid StartGame(){
        // Get Rows and Cols from player input on console.
        System.out.println("New game: Setup");
        System.out.println("Which size grid would you like? Enter a number below to select.");
        System.out.println("0. Small (5x5) \n1. Medium (8x8) \n2. Large (12x12) \n3. Custom");
        Scanner userInput = new Scanner (System.in);
        String Response = userInput.nextLine();
        int size;
        if (isInteger(Response)){
            size = Integer.valueOf(Response);
        }
        else{
            System.out.println("Input error. Type a number only (e.g. type 0, 1, 2, or 3)");
            return StartGame();
        }
        int[] dimensions = switchOnGridSize(size);
        int rows = dimensions[0];
        int cols = dimensions[1];
        ButtonArray = new Button[rows][cols];
        Grid OutGrid = new Grid(rows, cols);

        //populate button array
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                Button button = new Button();
                ButtonArray[i][j] = button;
                int row = i;
                int col = j;
                button.setOnAction(e -> {
                    GameGrid.ButtonClicked(col, row);
                });
            }
        }

        // set mines
        System.out.println("Select difficulty option. \n 0. Sparse mine density \n 1. Regular mine density \n " +
                "2. Heavy mine density \n 3. Insane mine density");
        Response = userInput.nextLine();
        int densitySelection;
        if (isInteger(Response)) {
            densitySelection = Integer.valueOf(Response);
        }
        else{
            OutGrid.PrintErrorMessage("\nInput error. Insane mode selected.");
            densitySelection = 3;
        }
        double mineDensity = switchOnMineDensity(densitySelection);
        OutGrid.SetMines(mineDensity);

        // Set the remaining tiles
        OutGrid.SetRemainingTiles();
        OutGrid.PrintGrid();
        OutGrid.setButtons(ButtonArray);

        //On to the gameplay loop.
        OutGrid.GameplayLoop();

        return OutGrid;
    }

    // returns an integer as [rows, cols]
    public static int[] switchOnGridSize(int selection){
        switch(selection){
            case 0:
                return new int[] {5, 5};
            case 1:
                return new int[] {8, 8};
            case 2:
                return new int[] {12, 12};
            case 3:
                System.out.println("Enter how many rows should the grid have?");
                Scanner switchScanner = new Scanner (System.in);
                String rows = switchScanner.nextLine();
                System.out.println("Enter how many columns should the grid have?");
                String cols = switchScanner.nextLine();
                if (!(isInteger(rows) && isInteger(cols))){
                    System.out.println("\nError: non-integer input. Try that again.");
                    return switchOnGridSize(selection);
                }
                int rowsOut = Integer.valueOf(rows);
                if (rowsOut < 0) rowsOut = rowsOut*(-1);
                int colsOut = Integer.valueOf(cols);
                if (colsOut < 0) colsOut = colsOut*(-1);
                return new int[] {rowsOut, colsOut};
            default:
                System.out.println("Input error. Type a number only (e.g. type 0, 1, 2, or 3)");
                Scanner errorScanner = new Scanner (System.in);
                int choice = Integer.valueOf(errorScanner.nextLine());
                return switchOnGridSize(choice);
        }
    }

    public static double switchOnMineDensity(int selection){
        switch(selection){
            case 0: return 0.07;
            case 1: return 0.1;
            case 2: return 0.15;
            case 3: return 0.28;
            default:
                System.out.println("Input error. Type a number only (e.g. type 0, 1, 2, or 3)");
                Scanner errorScanner = new Scanner (System.in);
                int choice = Integer.valueOf(errorScanner.nextLine());
                return switchOnMineDensity(choice);
        }
    }

    public static void main(String[] args) {
        GameGrid = StartGame();
        launch(); // problem is with the launch.
    }
}