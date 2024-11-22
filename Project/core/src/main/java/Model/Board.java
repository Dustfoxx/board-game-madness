package Model;

/**
 * The Board class represents the game board, which consists of a grid of cells.
 */
public class Board {

    private Cell[][] cells; // 2D array representing the grid of cells on the board

    /**
     * Constructor to initialize the game board with a specified 2D array of cells.
     * Ensures that the grid is not null and all cells are valid.
     * 
     * @param cells The 2D array of cells to initialize the game board with.
     * @throws IllegalArgumentException If the grid is null, dimensions are invalid, or any cell is null.
     */
    public Board(Cell[][] cells) {
        checkCells(cells);
        this.cells = cells;
    }

    /**
     * Validates the 2D array of cells to ensure it is not null, has valid dimensions,
     * and contains no null rows or cells.
     * 
     * @param cells The 2D array of cells to validate.
     * @throws IllegalArgumentException If the cells array is invalid.
     */
    private void checkCells(Cell[][] cells) {
        if (cells == null || cells.length == 0 || cells[0].length == 0) {
            throw new IllegalArgumentException("The cells array must have at least one row and one column.");
        }
        for (Cell[] row : cells) {
            if (row == null) {
                throw new IllegalArgumentException("No row in the cells array can be null.");
            }
            for (Cell cell : row) {
                if (cell == null) {
                    throw new IllegalArgumentException("No cell in the grid can be null.");
                }
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
