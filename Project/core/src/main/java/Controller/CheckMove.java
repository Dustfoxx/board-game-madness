package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Board;
import Model.Cell;
import Model.Player;

public class CheckMove {
    /*
     * private List<int[]> checkSurroundingCells(int[] coord, Board board) {
     * int[] boardDims = board.getDims();
     * 
     * int[] validRows = getCoordRange(coord[0], boardDims[0]);
     * int[] validCols = getCoordRange(coord[1], boardDims[1]);
     * 
     * List<int[]> possibleCoords = new ArrayList<int[]>();
     * 
     * for (int row = validRows[0]; row <= validRows[1]; row++) {
     * for (int col = validCols[0]; col <= validCols[1]; col++) {
     * if (board.getCell(coord[0], coord[1]) instanceof TempleCell) {
     * possibleCoords.add(new int[] { row, col });
     * } else {
     * if (row == coord[0] || col == coord[1]) {
     * possibleCoords.add(new int[] { row, col });
     * } else if (board.getCell(row, col) instanceof TempleCell) {// Should check
     * for temple here
     * possibleCoords.add(new int[] { row, col });
     * }
     * }
     * }
     * }
     * return possibleCoords;
     * }
     * 
     * private int[] getCoordRange(int location, int maxVal) {
     * int[] range = { 0, 0 };
     * 
     * if (location != 0) {
     * range[0] = location - 1;
     * }
     * if (location < maxVal) {
     * range[1] = location + 1;
     * } else {
     * range[1] = maxVal;
     * }
     * 
     * return range;
     * }
     * 
     * public void printBoard(Cell[][] board) {
     * for (Cell[] row : board) {
     * for (Cell cell : row) {
     * System.out.print(cell instanceof Temple ? "0 " : "1 ");
     * }
     * System.out.println("\n");
     * }
     * System.out.println("\n");
     * }
     * 
     * public int[][] getValidMoves(Player player, Board board) {// In testing i
     * will assume matrix of ones and zeros
     * int[] dims = board.getDims();
     * 
     * int[] coords = null;
     * List<Player> cellPlayers = null;
     * 
     * // I hate this function so please if you have a better idea fix it
     * // Move through rows
     * for (int row = 0; row < dims[0]; row++) {
     * // Move through columns
     * for (int col = 0; col < dims[1]; col++) {
     * // Get players from cell
     * cellPlayers = board.getCell(row, col).getPlayers();
     * // Iterate over players
     * for (Player foundPlayer : cellPlayers) {
     * // If player is equal save coords
     * coords = foundPlayer.getId() == player.getId() ? new int[] { row, col } :
     * null;
     * // If player has been found exit loops
     * if (coords != null) {
     * row = dims[0];
     * col = dims[1];
     * break;
     * }
     * }
     * }
     * }
     * 
     * if (coords == null) {
     * throw new IllegalStateException("Player ID not found");
     * }
     * 
     * List<int[]> firstMove = checkSurroundingCells(coords, board);
     * List<int[]> secondMove = new ArrayList<int[]>();
     * 
     * for (int i = 0; i < firstMove.size(); i++) {
     * secondMove.addAll(checkSurroundingCells(firstMove.get(i), board));
     * }
     * 
     * firstMove.addAll(secondMove);
     * 
     * int[][] possibleMoves = new int[dims[0]][dims[1]];
     * 
     * for (int i = 0; i < firstMove.size(); i++) {
     * possibleMoves[firstMove.get(i)[0]][firstMove.get(i)[1]] = 1;
     * }
     * 
     * return possibleMoves;
     * }
     */
}
