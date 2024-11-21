package Controller;

import java.util.ArrayList;
import java.util.List;

public class CheckMove {
    private List<int[]> checkSurroundingCells(int[] coord, int[][] board) {
        int[] boardDims = { board.length - 1, board[0].length - 1 };

        int[] validX = getCoordRange(coord[0], boardDims[0]);
        int[] validY = getCoordRange(coord[1], boardDims[1]);

        List<int[]> possibleCoords = new ArrayList<int[]>();

        for (int x = validX[0]; x <= validX[1]; x++) {
            for (int y = validY[0]; y <= validY[1]; y++) {
                if (board[coord[0]][coord[1]] == 1) {
                    possibleCoords.add(new int[] { x, y });
                } else {
                    if (x == coord[0] || y == coord[1]) {
                        possibleCoords.add(new int[] { x, y });
                    } else if (board[x][y] == 1) {// Should check for temple here
                        possibleCoords.add(new int[] { x, y });
                    }
                }
            }
        }

        return possibleCoords;
    }

    private int[] getCoordRange(int location, int maxVal) {
        int[] range = { 0, 0 };

        if (location != 0) {
            range[0] = location - 1;
        }
        if (location < maxVal) {
            range[1] = location + 1;
        } else {
            range[1] = maxVal;
        }

        return range;
    }

    public void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }

    public int[][] getValidMoves(int player, int[][] board) {// In testing i will assume matrix of ones and zeros
        // First add code to find player on board
        // For now will use arbitrary location

        int[] coords = { 3, 3 };

        List<int[]> firstMove = checkSurroundingCells(coords, board);
        List<int[]> secondMove = new ArrayList<int[]>();

        for (int i = 0; i < firstMove.size(); i++) {
            secondMove.addAll(checkSurroundingCells(firstMove.get(i), board));
        }

        firstMove.addAll(secondMove);

        int[][] possibleMoves = new int[board.length][board[0].length];

        for (int i = 0; i < firstMove.size(); i++) {
            possibleMoves[firstMove.get(i)[0]][firstMove.get(i)[1]] = 1;
        }

        return possibleMoves;
    }
}
