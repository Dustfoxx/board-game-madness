package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.AbstractCell;
import Model.Board;
import Model.BrainFact;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Player;
import Model.Token;

public class RecruiterActionController {

    /**
     * Moves the recruiter
     * 
     * @param recruiter   Reference to the recruiter object
     * @param board       The game state board
     * @param newPosition The desired position
     * @return True if footstep placed, else false
     */
    public void move(Board board, Player recruiter, int[] newPosition) {
        AbstractCell newCellPosition = board.getCell(newPosition[0], newPosition[1]);
        int[] currentPosition = board.getPlayerCoord(recruiter);

        AbstractCell previousCellPosition = board.getCell(currentPosition[0], currentPosition[1]);
        previousCellPosition.removePlayer(recruiter);
        newCellPosition.addPlayer(recruiter);
    }

    // Placement for input from view
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

            for (Feature i : features) {
                if (i != null && i == feature) {
                    List<Token> tokens = foundCell.getTokens();

                    Boolean valid = true;
                    for (Token j : tokens) {
                        if (j != null && (j.getClass() == BrainFact.class || j.getClass() == Footstep.class)) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
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
