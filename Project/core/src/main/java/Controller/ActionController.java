package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.lang.Math;

import Model.AbstractCell;
import Model.Board;
import Model.BrainFact;
import Model.BrainNote;
import Model.Feature;
import Model.Footstep;
import Model.Game;
import Model.NormalCell;
import Model.Player;
import Model.Recruiter;
import Model.Token;
import Model.Recruiter.RecruiterType;
import Model.Step;

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
            if (cell instanceof NormalCell) { // Check if the cell is not a temple
                NormalCell normalCell = (NormalCell) cell;
                Feature[] features = normalCell.getFeatures(); // Get the features of the cell
                List<Feature> featuresList = Arrays.asList(features);
                if (featuresList.contains(feature)) { // Check if the cell contains the specified feature
                    Footstep footstep = new Footstep();
                    cell.addToken(footstep); // Add a footstep to the cell
                    return; // Stop further searching once a cell is found
                }
            }
        }
    }

    /**
     * Replaces a footstep with a brain-fact token
     * 
     * @param cell the cell which contains the footstep to reveal
     * @return void
     */
    public void reveal(AbstractCell cell) {
        // Find the step token in the cell

        if (cell != null) {
            if (cell.containsFootstep()) {
                if(cell.containsBrainFact()){
                List<Token> tokens = cell.getTokens();
                for (Token token : tokens) {
                    if (token instanceof Step) {
                        int timestamp = ((Step) token).timestamp; // Get the timestamp of the step
                        BrainFact brainfact = new BrainFact(timestamp); // Create a new brainfact with the timestamp
                        cell.removeToken(cell.getFootstep()); // Remove the old footstep
                        cell.addToken(brainfact); // Replace with the new brainfact
                        break;
                    }
                }
            }
            else{
                throw new IllegalStateException("Cell already contains a BrainFact");
            }
            } else {
                throw new IllegalStateException("Footstep missing in cell");
            }
        } else {
            throw new IllegalStateException("Reveal should not be possible when player is not placed");
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
    public boolean movePlayer(Player player, Game gameState, int[] coords) {
        // Did player choose a valid location?
        // If player on board
        int[] playerCoords = gameState.getBoard().getPlayerCoord(player);

        // Add player to new cell

        // Checks whether the new cell is within allowed area
        if (gameState.getValidityMask()[coords[0]][coords[1]].getBoolean()) {
            // If player is on the board
            if (playerCoords != null) {
                // Remove player from current position
                gameState.getBoard().getCell(playerCoords[0], playerCoords[1]).removePlayer(player);

                // If recruiter and new spot is two steps away then it was a mindslipmove
                if (player instanceof Recruiter && (Math.abs(playerCoords[0] - coords[0]) > 1
                        || Math.abs(playerCoords[1] - coords[1]) > 1)) {
                    Recruiter tmpRecruiter = (Recruiter) player;
                    tmpRecruiter.setRecruiterType(RecruiterType.USED);
                    gameState.addMindSlipEvent();
                }
            }
            AbstractCell newCell = gameState.getBoard().getCell(coords[0], coords[1]);
            newCell.addPlayer(player);

            if (player instanceof Recruiter) {
                // Add new cell to walked path
                Recruiter recruiter = (Recruiter) player;
                recruiter.addToWalkedPath(coords[0], coords[1]);

                // Add a Step to the cell
                int timestamp = recruiter.getWalkedPath().size();
                newCell.addToken(new Step(timestamp));

                // Check for matching features and update recruited amount
                AbstractCell currentCell = gameState.getBoard().getCell(coords[0], coords[1]);
                if (currentCell instanceof NormalCell) {
                    NormalCell normalCell = (NormalCell) currentCell;
                    Feature[] features = normalCell.getFeatures();
                    Feature[] featuresOfInterest = recruiter.getFeaturesOfInterest();
                    int commonFeaturesCount = countMatchingFeatures(features, featuresOfInterest);
                    recruiter.addAmountRecruited(commonFeaturesCount);
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Counts the number of matching features between two arrays of {@link Feature}.
     *
     * The iteration stop if two matches are already found
     * since the maximum number of matching features is 2.
     *
     * @param features
     * @param featuresOfInterest
     * @return The number of matching features, up to a maximum of 2.
     */
    private int countMatchingFeatures(Feature[] features, Feature[] featuresOfInterest) {
        int count = 0;
        for (Feature feature : features) {
            for (Feature featureOfInterest : featuresOfInterest) {
                if (feature == featureOfInterest) {
                    count++;
                    break;
                }
            }
            if (count == 2) {
                return count;
            }
        }
        return count;
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
    public boolean addBrainNote(String newNote, int row, int col, Board board) {
        AbstractCell cell = board.getCell(row, col);

        for (Token token : cell.getTokens()) {
            if (token instanceof BrainNote) {
                if (newNote == "") {
                    cell.removeToken(token);
                } else {
                    ((BrainNote) token).setNote(newNote);
                }
                return true;
            }
        }
        try {
            BrainNote brainNote = new BrainNote(newNote);
            cell.addToken(brainNote);
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
        boolean foundNote = false;
        for (Token token : tokens) {
            if (token instanceof BrainNote) {
                brains.add(token);
                foundNote = true;
            } else if (token instanceof BrainFact) {
                brains.add(token);
            }
        }
        if (!foundNote) {
            brains.add(new BrainNote(""));
        }

        return brains;
    }
}
