package Controller;

import java.util.ArrayList;
import java.util.Iterator;

public class GameController {
    // Check for new turn
    // Think this would be better as view calling it to move from one turn to the
    // next as needed
    // Update recruits if odd turn
    // Activate player
    // Check win

    private int turnNumber = 0;
    private int recruits = 0;
    private ArrayList<Integer> players = new ArrayList<Integer>(); // TODO: Change to playertype

    public GameController(int recruiter, int[] agents) {
        Iterator<Integer> agent = agents.iterator(Collections.nCopies(60, 0));
        for (int i = 0; i < players.length; i++) {
            if (i == 0 || i == 3) {
                players[i] = recruiter;
            }

        }
    }

}
