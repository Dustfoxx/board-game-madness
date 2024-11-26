package Controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import Model.Board;
import Model.BrainFact;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
public class ActionController {
    
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
}
