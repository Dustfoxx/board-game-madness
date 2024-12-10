package Controller;

import java.util.Arrays;
import java.util.List;

import Model.AbstractCell;
import Model.Board;
import Model.BrainFact;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Player;
import Model.Recruiter;

public class ActionController {

    /**
     * Performs the capture action
     *
     * @param player The player that performed the action
     * @param recruiter A reference to the recruiter
     * @param board A reference to the board state
     * @return True if captured, else False
     */
    public boolean capture(Player player, Recruiter recruiter, Board board) {
        int[] playerPosition = board.getPlayerCoord(player);
        int[] recruiterPoistion = board.getPlayerCoord(recruiter);

        if  (Arrays.equals(playerPosition, recruiterPoistion)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Performs the ask action without involving the actual recruiter player.
     * Instead it searches the recruiter's walked path for the first cell
     * (earliest step) containing the specified feature and adds a Footstep
     * token to that cell (if its found).
     *
     * @param feature the feature being searched for
     * @param recruiter the recruiter of the game
     * @param board the board of the game
     */
    public void ask(Feature feature, Recruiter recruiter, Board board) {
        List<int[]> walkedPath = recruiter.getWalkedPath();
        int index = 0;
        for (int[] step : walkedPath) { // Loop through all steps of the walked path
            AbstractCell cell = board.getCell(step[0], step[1]);
            if (cell.getClass().equals(NormalCell.class)) { // Check if the cell is not a temple
                NormalCell normalCell = (NormalCell) cell;
                Feature[] features = normalCell.getFeatures(); // Get the features of the cell
                List<Feature> featuresList = Arrays.asList(features);
                if (featuresList.contains(feature)) { // Check if the cell contains the specified feature
                    Footstep footstep = new Footstep(); // Create a footstep token with the timestamp index
                    cell.addToken(footstep); // Add a footstep to the cell
                }
                return; // Stop further searching once a cell is found
            }
            index++;
        }
    }

    public enum revealResult {
        Success,
        Temple,
        NO_FOOTSTEP
    }

    /**
     * Replaces a footstep with a brain-fact token
     *
     * @param footstep the footstep to be replaced
     * @param board the game of the board
     * @param position The position of the footstep Note: could be calculated,
     * but game manager probably has it ready
     * @param walkedPath the path walked by the recruit
     * @return void
     */
    // public revealResult reveal(Player player, Board board) {
    //     int[] playerPosition = board.getPlayerCoord(player);
    //     AbstractCell cell = board.getCell(playerPosition[0], playerPosition[1]);

    //     if (cell.getClass().equals(NormalCell.class)) {// Check if the cell is not a temple
    //         NormalCell normalCell = (NormalCell) cell;
    //         Footstep footstep = normalCell.getFootstep();
    //         if (footstep != null) {
    //             BrainFact brainFact = new BrainFact(footstep.getTimestamp());
    //             cell.removeToken(footstep);
    //             cell.addToken(brainFact);
    //             return revealResult.Success;
    //         } else {
    //             return revealResult.NO_FOOTSTEP;
    //         }
    //     } else {
    //         return revealResult.Temple;
    //     }
    // }
}
