package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.AbstractCell;
import Model.Board;
import Model.BrainFact;
import Model.BrainNote;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Player;
import Model.Recruiter;
import Model.Token;

public class ActionController {

    /**
     * Performs the capture action
     * 
     * @param player    The player that performed the action
     * @param recruiter A reference to the recruiter
     * @param board     A reference to the board state
     * @return True if captured, else False
     */
    public boolean capture(Player player, Recruiter recruiter, Board board) {
        int[] playerPosition = board.getPlayerCoord(player);
        int[] recruiterPoistion = board.getPlayerCoord(recruiter);

        if (Arrays.equals(playerPosition, recruiterPoistion)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Performs the ask action without involving the actual recruiter player.
     * Instead it searches the recruiter's walked path for the first cell (earliest
     * step) containing the specified feature and adds a Footstep token to that cell
     * (if its found).
     * 
     * @param feature   the feature being searched for
     * @param recruiter the recruiter of the game
     * @param board     the board of the game
     */
    public void ask(Feature feature, Recruiter recruiter, Board board) {
        List<int[]> walkedPath = recruiter.getWalkedPath();
        for (int[] step : walkedPath) { // Loop through all steps of the walked path
            AbstractCell cell = board.getCell(step[0], step[1]);
            if (cell.getClass().equals(NormalCell.class)) { // Check if the cell is not a temple
                NormalCell normalCell = (NormalCell) cell;
                Feature[] features = normalCell.getFeatures(); // Get the features of the cell
                List<Feature> featuresList = Arrays.asList(features);
                if (featuresList.contains(feature)) { // Check if the cell contains the specified feature
                    Footstep footstep = new Footstep();
                    cell.addToken(footstep); // Add a footstep to the cell
                }
                return; // Stop further searching once a cell is found
            }
        }
    }

    /**
     * Replaces a footstep with a brain-fact token
     * 
     * @param footstep   the footstep to be replaced
     * @param board      the game of the board
     * @param position   The position of the footstep Note: could be calculated, but
     *                   game manager probably has it ready
     * @param walkedPath the path walked by the recruit
     * @return void
     */
    public void reveal(Footstep footstep, Board board, int[] position, List<int[]> walkedPath) {
        int time = 0;

        // Finds the index of the position in walked path
        for (int i = 0; i < walkedPath.size(); i++) {
            if (Arrays.equals(walkedPath.get(i), position)) {
                time = i + 1;
                BrainFact brainFact = new BrainFact(time);

                NormalCell cell = (NormalCell) board.getCell(position[0], position[1]);

                cell.addToken(brainFact);
                cell.removeToken(footstep);
                break;
            }
        }
    }

    /**
     * Places a player on the board. Mask
     * decides valid spots
     * 
     * @param player       the player that is being placed
     * @param board        the board it is being placed on
     * @param validityMask the mask defining valid positions. Null if all options
     *                     are valid
     * @param coords       coordinates the player will be placed on
     * @return boolean defining if the action was successful or not
     */
    public boolean movePlayer(Player player, Board board, boolean[][] validityMask, int[] coords) {
        // Did player choose a valid location?
        if (validityMask != null) {
            if (validityMask[coords[0]][coords[1]]) {
                return false;
            }
        }
        // If player on board
        int[] playerCoords = board.getPlayerCoord(player);
        if (playerCoords != null) {
            board.getCell(playerCoords[0], playerCoords[1]).removePlayer(player);
        }
        // Add player to new cell
        board.getCell(coords[0], coords[1]).addPlayer(player);
        return true;
    }

    /**
     * Adds a brainnote to a cell, replaces it if one exists already
     * 
     * @param text  Text it should contain
     * @param row   row of the brainnote
     * @param col   column of the brainnote
     * @param board board to add the note to
     * @return true if the operation succeeded, false otherwise
     */
    public boolean addBrainNote(BrainNote newNote, int row, int col, Board board) {
        for (Token token : board.getCell(row, col).getTokens()) {
            if (token instanceof BrainNote) {

                return true;
            }
        }
        try {
            board.getCell(row, col).addToken(newNote);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetches the information of a cells brains
     * 
     * @param row   the row to fetch from
     * @param col   the columns to fetch from
     * @param board the board the notes exists on
     * @return list of brains of a cell
     */
    public List<Token> fetchBrains(int row, int col, Board board) {
        List<Token> tokens = board.getCell(row, col).getTokens();
        List<Token> brains = new ArrayList<Token>();
        for (Token token : tokens) {
            if (token instanceof BrainNote || token instanceof BrainFact) {
                brains.add(token);
            }
        }
        return brains;
    }
}
