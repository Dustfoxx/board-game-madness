package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Board class represents the game board, which consists of a grid of cells.
 * Each cell can hold various features that affect gameplay.
 */
public class Board {

    private Cell[][] cells; // 2D array representing the grid of cells on the board

    /**
     * Constructor to initialize the game board with a specified number of rows and
     * columns.
     * It creates a grid of cells and initializes them.
     * 
     * @param rows    The number of rows in the game board.
     * @param columns The number of columns in the game board.
     */
    public Board(int rows, int columns) {
        cells = new Cell[rows][columns]; // Initialize the 2D array of cells
        createCells(); // Create and initialize the cells with features
    }

    /**
     * Creates and initializes each cell on the board.
     * Each cell is initialized with an empty list of features.
     * TODO: In the future, the cells should be filled with features somehow:
     * - Either in the createCells method
     * - Or it's done with a new Cell.addFeatures() method
     */
    private void createCells() {
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                Feature[] features = new Feature[2]; // Array to hold features for each cell
                cells[row][column] = new Cell(features); // Initialize the cell with no features
            }
        }
    }

    /**
     * Gets the cell at the specified row and column.
     * 
     * @param row    The row index of the cell.
     * @param column The column index of the cell.
     * @return The cell at the specified row and column.
     */
    public Cell getCell(int row, int column) {
        return cells[row][column];
    }
}
