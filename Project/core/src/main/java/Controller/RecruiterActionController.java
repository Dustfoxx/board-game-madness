package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Board;
import Model.Cell;
import Model.Feature;
import Model.Footstep;
import Model.Game;
import Model.Token;

public class RecruiterActionController {

    /**
     * Moves the recruiter
     * Sets token if passed
     * 
     * @param feature The feature asked about
     * @return True if moved, else false
     */
    public boolean move() {
        return true;
    }

    /**
     * Asks the recruiter about crossed features
     * Sets token if passed
     * 
     * @param void
     * @return True if moved, else false
     */
    public boolean mindSlip() {
        // Check Valid Mindslip moves

        // Send valid moves to view (recieve player move)

        // Update board state

        return true;
    }


    // Placement for input for view
    int[] answerView (List<int[]> viableChoices){
        return viableChoices.get(0);
    }

    public boolean answer(Feature feature, Board board, List<int[]> walkedPath) {
        List<int[]> viablePositions = new ArrayList<>();
        for (int[] row : walkedPath) {
            int x = row[0];
            int y = row[1];

            Cell foundCell = board.getCell(x, y);
            Feature[] features = foundCell.getFeatures();

            assert(features.length != 0);

            for (Feature i : features) {
                if (i != null  && i.equals(x)) { // Check if the current feature equals x
                    
                    List<Token> tokens = foundCell.getTokens();

                    // NOTE: ASSUMES THAT ONLY ONE TOKEN CAN EXIST ON IT
                    if(tokens.size() == 0){
                        viablePositions.add(new int[] { x, y });
                    }
                }
            }
        }

        int[] selection = answerView(viablePositions);

        Cell selectedCell = board.getCell(selection[0], selection[1]);

        Token token = new Footstep();

        selectedCell.addToken(token);
        return true;
    }
}
