package Controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import Model.Board;
import Model.BrainFact;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Player;

public class ActionController {

    public enum Actions {
        MOVE,
        ASK,
        REVEAL,
        CAPTURE,
        MINDSLIP
    }

    // Proposed function:

    public void actionHandler(Actions action) {
        // Runs proper action from here
        // Allows centralized control over if the turn should switch.
        // Otherwise all action functions must modify model to match which actions have
        // been taken
    }

    /**
     * Displays a set of features to the player and prompts picking
     * 
     * @param feature The chosen feature
     * @return The feature picked
     */
    private CompletableFuture<Feature> displayAndPickFeatures(Feature[] features) {
        // TODO: Currently just picks first index
        return CompletableFuture.completedFuture(features[0]);
    }

    /**
     * Lets the user ask for a specific feature on it's current cell.
     * 
     * @param cell The cell containing the features
     * @return The feature picked
     */
    public Feature ask(NormalCell cell) {
        Feature[] features = cell.getFeatures();
        CompletableFuture<Feature> featureFuture = displayAndPickFeatures(features);
        return featureFuture.join();
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
}
