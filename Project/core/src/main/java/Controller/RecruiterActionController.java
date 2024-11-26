package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Board;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Token;

public class RecruiterActionController {

    /**
     * Moves the recruiter
     * 
     */
    public boolean move() {
        return true;
    }

    public boolean mindSlip() {
        // Check Valid Mindslip moves

        // Send valid moves to view (recieve player move)

        // Update board state

        return true;
    }

    // Placement for input for view
    int[] answerView(List<int[]> viableChoices) {
        return viableChoices.get(0);
    }

    /**
     * Answer whether the recruiter has passed a specific feature
     * Sets a footstep token if chosen
     * 
     * @param walkedPath The walked path of the recruiter
     * @param board      The game state board
     * @param feature    The feature asked about
     * @return True if footstep placed, else false
     */
    public boolean answer(Feature feature, Board board, List<int[]> walkedPath) {
        List<int[]> viablePositions = new ArrayList<>();
        for (int[] row : walkedPath) {
            int x = row[0];
            int y = row[1];

            NormalCell foundCell = (NormalCell) board.getCell(x, y);
            Feature[] features = foundCell.getFeatures();

            assert (features.length != 0);

            for (Feature i : features) {
                if (i != null && i == feature) { // Check if the current feature equals x

                    List<Token> tokens = foundCell.getTokens();

                    // NOTE: ASSUMES THAT ONLY ONE TOKEN CAN EXIST ON IT
                    if (tokens.size() == 0) {
                        viablePositions.add(new int[] { x, y });
                    }
                }
            }
        }

        if (!viablePositions.isEmpty()) {

            int[] selection = answerView(viablePositions);

            NormalCell selectedCell = (NormalCell) board.getCell(selection[0], selection[1]);

            Token token = new Footstep();

            selectedCell.addToken(token);
            return true;
        } else {
            return false;
        }
    }
}
