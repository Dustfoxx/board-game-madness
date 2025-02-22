package Model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The Board class represents the game board, which consists of a grid of cells.
 */
public class Board {

    private int rowsDim;
    private int colsDim;
    private AbstractCell[][] cells; // 2D array representing the grid of cells on the board

    /**
     * Constructor to initialize the game board with a specified 2D array of cells.
     * Ensures that the grid is not null and all cells are valid.
     *
     * @param cells The 2D array of cells to initialize the game board with.
     * @throws IllegalArgumentException If the grid is null, dimensions are invalid,
     *                                  or any cell is null.
     */
    public Board(AbstractCell[][] cells) {
        checkCells(cells);
        this.cells = cells;
        rowsDim = cells.length;
        colsDim = cells[0].length;
    }

    /**
     * Constructor using a csv file to create the board with features
     *
     * @param boardCsv The Csv data that is used to create the board
     */
    public Board(Csv boardCsv) {
        String[][] boardData = boardCsv.getData();
        int[] dimensions = new int[] { boardData.length, boardData[0].length };

        AbstractCell[][] cells = new AbstractCell[dimensions[0]][dimensions[1]];

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                if (boardData[i][j].isEmpty()) {
                    cells[i][j] = new TempleCell();
                } else {
                    String[] featureStrings = boardData[i][j].split(":");
                    Feature[] features = new Feature[] { Feature.valueOf(featureStrings[0]),
                            Feature.valueOf(featureStrings[1]) };
                    cells[i][j] = new NormalCell(features);
                }
            }
        }
        checkCells(cells);
        this.cells = cells;
        rowsDim = cells.length;
        colsDim = cells[0].length;
    }

    /**
     * Validates the 2D array of cells to ensure it is not null, has valid
     * dimensions,
     * and contains no null rows or cells.
     *
     * @param cells The 2D array of cells to validate.
     * @throws IllegalArgumentException If the cells array is invalid.
     */
    private void checkCells(AbstractCell[][] cells) {
        if (cells == null || cells.length == 0 || cells[0].length == 0) {
            throw new IllegalArgumentException("The cells array must have at least one row and one column.");
        }
        for (AbstractCell[] row : cells) {
            if (row == null) {
                throw new IllegalArgumentException("No row in the cells array can be null.");
            }
            for (AbstractCell cell : row) {
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
    public AbstractCell getCell(int row, int column) {
        return cells[row][column];
    }

    /**
     * Gets the coordinates of a given player
     *
     * @param player player to be found
     * @return coordinates if found, otherwise null
     */
    public int[] getPlayerCoord(Player player) {
        // I hate this function so please if you have a better idea fix it
        // Move through rows
        for (int row = 0; row < rowsDim; row++) {
            // Move through columns
            for (int col = 0; col < colsDim; col++) {
                // Get players from cell
                List<Player> cellPlayers = getCell(row, col).getPlayers();
                // Iterate over players
                for (Player foundPlayer : cellPlayers) {
                    // If player is equal save coords
                    if (foundPlayer.getId() == player.getId()) {
                        return new int[] { row, col };
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the board dimensions.
     *
     * @return The dimensions for the board.
     */
    public int[] getDims() {
        return new int[] { rowsDim, colsDim };
    }

    public void updateDeeply(Board newBoard) {

        this.rowsDim = newBoard.rowsDim;
        this.colsDim = newBoard.colsDim;

        for (int row = 0; row < this.rowsDim; row++) {
            for (int col = 0; col < this.colsDim; col++)
                this.cells[row][col].updateDeeply(newBoard.cells[row][col]);
        }

    }

    @Override
    public String toString() {
        return "Board{" +
                "rowsDim=" + rowsDim +
                ", colsDim=" + colsDim +
                ", cells=" + Arrays.deepToString(cells) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Board board = (Board) o;
        return rowsDim == board.rowsDim &&
                colsDim == board.colsDim &&
                Arrays.deepEquals(cells, board.cells);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rowsDim, colsDim);
        result = 31 * result + Arrays.deepHashCode(cells);
        return result;
    }
}
