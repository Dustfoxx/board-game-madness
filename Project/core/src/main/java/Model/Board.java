package Model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Cell[][] cells;

    public Board(int rows, int columns) {
        cells = new Cell[rows][columns];
        createCells();
    }

    private void createCells() {
        List<Feature> features = new ArrayList<>();
        for (int row = 0; row < cells.length; row++) {
            for (int columns = 0; columns < cells[row].length; columns++) {
                cells[row][columns] = new Cell(features);
            }
        }
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }
}
