package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.Board;
import Model.AbstractCell;
import Model.TempleCell;
import Model.Player;
import Model.Recruiter;
import Model.RougeAgent;
import Model.Token;
import Model.Footstep;
import Model.MutableBoolean;

public class CheckAction {
    
    /**
     * Takes player position and returns a list of all valid single step coordinates
     * All orthogonal and including diagonal if some are temples
     * 
     * @param coord Player coordinates
     * @param board Board player inhabits
     * @return List<int[]> List of possible choices as coordinates
     */
    private List<int[]> checkSurroundingCells(int[] coord, Board board) {
        int[] boardDims = board.getDims();
        int[] validRows = getCoordRange(coord[0], boardDims[0]);
        int[] validCols = getCoordRange(coord[1], boardDims[1]);
        List<int[]> possibleCoords = new ArrayList<int[]>();

        for (int row = validRows[0]; row <= validRows[1]; row++) {
            for (int col = validCols[0]; col <= validCols[1]; col++) {
                if (!Arrays.equals(coord, new int[] { row, col })) {
                    if (board.getCell(coord[0], coord[1]) instanceof TempleCell) {
                        possibleCoords.add(new int[] { row, col });
                    } else {
                        if (row == coord[0] || col == coord[1]) {
                            possibleCoords.add(new int[] { row, col });
                        } else if (board.getCell(row, col) instanceof TempleCell) {// Should check for temple here
                            possibleCoords.add(new int[] { row, col });
                        }
                    }
                }
            }
        }
        return possibleCoords;
    }

    /**
     * Gets the range of movement coordinates to iterate over, I.e. position 3 gives
     * array of 2-4.
     * 
     * @param location Position in single dimension
     * @param maxVal   What the maximum possible coordinate is
     * @return Returns an array of three integers
     */
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

    /**
     * Prints the board based on cells. Mainly used for testing.
     * 
     * @param board The board that should be printed
     */
    public void printBoard(AbstractCell[][] board) {
        for (AbstractCell[] row : board) {
            for (AbstractCell cell : row) {
                System.out.print(cell instanceof TempleCell ? "0 " : "1 ");
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }

    /**
     * Prints the created movement mask. Only used for testing.
     * 
     * @param mask The mask that should be printed
     */
    public void printMask(int[][] mask) {
        for (int[] row : mask) {
            for (int cell : row) {
                System.out.print(cell);
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }

    /**
     * Creates a mask from a list of coordinates and the size of the board
     * 
     * @param rows       amount of rows
     * @param cols       amount of columns
     * @param foundMoves allowed movement coordinates
     * @return a matrix of zeroes and ones. ones are possible movement
     */
    private MutableBoolean[][] createMask(int rows, int cols, List<int[]> foundMoves) {
        MutableBoolean[][] possibleMoves = new MutableBoolean[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                possibleMoves[i][j] = new MutableBoolean();
            }
        }

        for (int i = 0; i < foundMoves.size(); i++) {
            possibleMoves[foundMoves.get(i)[0]][foundMoves.get(i)[1]] = new MutableBoolean(true);
        }

        return possibleMoves;
    }

    public MutableBoolean[][] createUniformMask(Board board, boolean newVal){
        int[] dims = board.getDims();

        MutableBoolean[][] mask = new MutableBoolean[dims[0]][dims[1]];

        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                mask[i][j] = new MutableBoolean(newVal);
            }
        }
        return mask;
    }

    /**
     * A single movement for any player. Extracted since its the same for recruiter
     * and agent
     * 
     * @param player Which player is currently choosing movement
     * @param board  the board the player inhabits
     * @return List of possible movement coordinates
     */
    private List<int[]> initialMove(Player player, Board board) {
        int[] coords = board.getPlayerCoord(player);

        if (coords == null) {
            throw new IllegalStateException("Player ID not found");
        }

        return checkSurroundingCells(coords, board);
    }

    /**
     * Figures out possible mindslip moves
     * 
     * @param player Recuiter in the current game
     * @param board  Board the player inhabits
     * @param type   Type of mindslip equipped. 0 for orhogonal and 1 for diagonal
     * @return Possible coordinates to visit after mindslip
     */
    private List<int[]> mindSlip(Recruiter player, Board board, int type) {
        int mindSlipType = type;// player.getMindSlip() NEEDS TO BE AVAILABLE
        int[] coords = board.getPlayerCoord(player);
        int[] dims = board.getDims();

        int[] movement = new int[] { -2, 2 };

        List<int[]> possibleSlips = new ArrayList<int[]>();
        int[] newCoord;

        for (int i = 0; i < movement.length; i++) {
            for (int j = 0; j < movement.length; j++) {
                if (j + i % 2 == 0) {
                    newCoord = new int[] { coords[0] + movement[i], coords[1] + movement[j] * mindSlipType };
                } else {
                    newCoord = new int[] { coords[0] + movement[i] * mindSlipType, coords[1] + movement[j] };
                }
                if (newCoord[0] >= 0 && newCoord[0] < dims[0] && newCoord[1] >= 0 && newCoord[1] < dims[1]) {
                    possibleSlips.add(newCoord);
                }
            }
        }
        return possibleSlips;

    }

    public MutableBoolean[][] getValidMoves(Player player, Board board){
        if(board.getPlayerCoord(player) == null){
            return getValidPlacements(board);
        }

        if(player instanceof Recruiter){
            return getValidMoves((Recruiter) player, board, 0);
        } else {
            return getValidMoves((RougeAgent) player, board);
        }
    }

    /**
     * Gets valid moves for an agent
     * 
     * @param player Agent that is being considered
     * @param board  Board that the player inhabits
     * @return Returns a mask that contains booleans saying where a player
     *         can move
     */
    public MutableBoolean[][] getValidMoves(RougeAgent player, Board board) {
        int[] dims = board.getDims();

        List<int[]> firstMove = initialMove(player, board);
        List<int[]> secondMove = new ArrayList<int[]>();
        for (int i = 0; i < firstMove.size(); i++) {
            secondMove.addAll(checkSurroundingCells(firstMove.get(i), board));
        }

        firstMove.addAll(secondMove);

        return createMask(dims[0], dims[1], firstMove);
    }

    /**
     * Gets valid moves for the recruiter
     * 
     * @param player recruiter
     * @param board  Board that the player inhabits
     * @param type   type of mindslip, 0 for orthogonal, 1 for diagonal
     * @return Returns a mask that contains booleans saying where a player
     *         can move
     */
    public MutableBoolean[][] getValidMoves(Recruiter player, Board board, int type) {
        int[] dims = board.getDims();

        List<int[]> firstMove = initialMove(player, board);
        List<int[]> walkedList = player.getWalkedPath();

        firstMove.addAll(mindSlip(player, board, type));

        for (int i = 0; i < walkedList.size(); i++) {
            for (int j = 0; j < firstMove.size(); j++) {
                if (Arrays.equals(walkedList.get(i), firstMove.get(j))) {
                    firstMove.remove(j);
                }
            }
        }

        return createMask(dims[0], dims[1], firstMove);
    }

    /**
     * Generic function to create a mask for initial placements of rogue agents
     * 
     * @param board board is only needed for size
     * @return mask for valid placements of agents
     */
    public MutableBoolean[][] getValidPlacements(Board board) {
        int[] dims = board.getDims();

        MutableBoolean[][] mask = new MutableBoolean[dims[0]][dims[1]];
        boolean value = false;

        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                value = false;
                if (i == 0 || i == dims[0] - 1) {
                    value = true;
                } else if (j == 0 || j == dims[1] - 1) {
                    value = true;
                }
                mask[i][j] = new MutableBoolean(value);
            }
        }

        return mask;

    }

    /**
     * Returns if a player can take an ask action
     * 
     * @param player RougeAgent we are checking
     * @param board  board player inhabits
     * @return True if action is allowed, false otherwise.
     */
    public boolean checkAskAction(RougeAgent player, Board board) {
        int[] playerCoord = board.getPlayerCoord(player);
        if (board.getCell(playerCoord[0], playerCoord[1]) instanceof TempleCell) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether a player can take a reveal action
     * 
     * @param player player we are checking
     * @param board  board player inhabits
     * @return True if action is allowed, false otherwise
     */
    public boolean checkRevealAction(RougeAgent player, Board board) {
        int[] playerCoord = board.getPlayerCoord(player);
        for (Token token : board.getCell(playerCoord[0], playerCoord[1]).getTokens()) {
            if (token instanceof Footstep) {
                return true;
            }
        }
        return false;
    }

}
