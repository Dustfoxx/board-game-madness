package View.screen.GameScreenComponents;

import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import java.util.HashSet;
import Controller.GameController;

/**
 * The TurnBar class represents a UI component that displays the current turn and tracks
 * previous turns in a game. It is built using LibGDX's scene2d.ui framework.
 * This class is responsible for updating the turn display and managing revealed turns.
 */
public class TurnBar extends Table {
    private final GameController gameController; // Reference to the game controller
    private String timeValue; // The current time value as a string
    private final Label timeTracker; // Label for displaying the current time
    private final Table pastTurn; // Table for displaying past turns
    private final Set<Table> revealedTurns; // Set of tables representing revealed turns
    private final Set<Integer> revealedTurnsInt; // Set of integers representing revealed turn indices
    private final Skin skin; // Skin for UI styling

    /**
     * Constructor for TurnBar.
     * Initializes the UI elements and prepares the turn bar for updates.
     *
     * @param gameController The GameController that manages game state.
     */

    public TurnBar(GameController gameController) {
        this.setDebug(true); // Enables debug mode for visualizing layout borders
        this.gameController = gameController;
        skin = new Skin(Gdx.files.internal("comicui/comic-ui.json"));

        // Table for displaying the turn clock
        Table turnClock = new Table();
        this.add(turnClock).expandX().fillX().top().row();
        this.timeValue = String.valueOf(gameController.getGame().getCurrentTime());

        // Add leading zero to single-digit times
        if (timeValue.length() == 1) {
            timeValue = "0" + timeValue;
        }

        // Label for displaying the current turn time
        timeTracker = new Label(timeValue + ": 00", skin, "half-tone");
        timeTracker.setAlignment(Align.center);
        turnClock.add(timeTracker).expandX().fillX().pad(10);

        // Table for tracking past turns
        pastTurn = new Table();
        this.add(pastTurn).expand().fill().top();
        revealedTurns = new HashSet<>();
        revealedTurnsInt = new HashSet<>();
    }

    /**
     * Updates the turn bar by refreshing the current turn display and revealing past turns.
     * Reveals additional turns based on the current turn number.
     */

    public void updateTurnbar() {
        String updatedTime = String.valueOf(gameController.getGame().getCurrentTime());

        // Add leading zero to single-digit times
        if (updatedTime.length() == 1) {
            updatedTime = "0" + updatedTime;
        }

        // Update the time tracker label
        timeTracker.setText(updatedTime + ": 00");

        int currentTurn = gameController.getGame().getCurrentTime();

        // Reveal past turns if applicable
        if (currentTurn > 5) {
            if ((currentTurn - 1) % 2 == 1 && !revealedTurnsInt.contains(currentTurn - 1)) {
                revealTurn(currentTurn - 1);
            }
        }
    }

    /**
     * Reveals a turn in the past turn tracker.
     *
     * @param turn The turn number to reveal.
     */
    
    private void revealTurn(int turn) {
        String turnString = (turn > 9) ? String.valueOf(turn) : "0" + turn;

        // Table for the turn row
        Table turnRow = new Table();
        turnRow.setDebug(true); // Enables debug mode for layout visualization
        turnRow.align(Align.center);

        // Mindslip/Recruits (Placeholder for additional data display)
        // TODO: Replace "mindSlip" with actual logic to determine if Mindslip occurs
        if (mindSlip) {
            Label msLabel = new Label("Mindslip", skin, "big");
            msLabel.setAlignment(Align.center);
            turnRow.add(msLabel).expandX().pad(5);
        }

        // Add recruited information
        int unrevealedRecruits = gameController.getGame().getAmountRecruited();
        Label recruitedLabel = new Label(String.valueOf(unrevealedRecruits), skin, "big");
        recruitedLabel.setAlignment(Align.center);
        turnRow.add(recruitedLabel).expandX().pad(5);

        // Add turn time
        Label turnLabel = new Label(turnString + ":00", skin, "big");
        turnLabel.setAlignment(Align.center);
        turnRow.add(turnLabel).expandX().pad(5);

        // Add the turn row to the past turns table if not already revealed
        if (!revealedTurns.contains(turnRow)) {
            revealedTurnsInt.add(turn);
            pastTurn.add(turnRow).expandX();
            pastTurn.row();
            revealedTurns.add(turnRow);
        }
    }
}
