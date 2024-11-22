package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTests {

    private Board board;
    private int rows;
    private int columns;
    private AbstractCell[][] cells;

    @BeforeEach
    void setUp() {
        rows = 3;
        columns = 3;
        cells = new AbstractCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = new NormalCell(new Feature[2]);
            }
        }
        board = new Board(cells);
    }

    @Test
    void testBoardInitialization() {
        assertNotNull(board, "Board should be initialized.");
        assertTrue(rows > 0, "Rows should be larger than 0.");
        assertTrue(columns > 0, "Columns should be larger than 0.");
    }

    @Test
    void testCellInitialization() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                AbstractCell cell = board.getCell(row, column);
                assertNotNull(cell, "Each cell should be initialized.");
            }
        }
    }

    @Test
    void testGetCell() {
        // Test row out of bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getCell(-1, 0),
                "Getting a cell with a negative row index should throw an exception.");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getCell(rows, 0),
                "Getting a cell with a row index equal to or greater than the amount of rows should throw an exception.");

        // Test column out of bounds
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getCell(0, -1),
                "Getting a cell with a negative column index should throw an exception.");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getCell(0, columns),
                "Getting a cell with a column index equal to or greater than the amount of columns should throw an exception.");
    }
}
