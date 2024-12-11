package View.screen.GameScreenComponents;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.Game;
import Model.Player;

/**
 * Represents a UI component that displays the players and whos turn it is.
 * This class extends the {@link Table} class to organize and layout its child
 * actors.
 */
public class PlayerBar extends Table {

    private final GameController gameController;

    /**
     * Constructs a PlayerBar.
     *
     * @param gameController the {@link GameController} that manages game logic
     */
    public PlayerBar(GameController gameController, Skin skin) {

        this.gameController = gameController;
        this.debug();

        // Create a label for each player and add it to the table
        for (Player player : gameController.getGame().getPlayers()) {
            Label playerLabel = new Label(player.getName(), skin, "narration");
            playerLabel.setFontScale(2f);
            this.add(playerLabel).expandX(); // Expand each label equally along the X-axis
        }

        // Create a button to simulate advancing to the next player's turn - TODO:
        // Remove when not useful anymore
        TextButton nextTurnButton = new TextButton("End Turn", skin);
        nextTurnButton.setColor(Color.MAGENTA);
        this.add(nextTurnButton);

        // Add an event listener to handle button clicks
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.newTurn();
            }
        });
    }

    /**
     * Updates the PlayerBar to visually indicate the current player by
     * highlighting its label in green and dimming the others.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Game game = gameController.getGame();
        List<Player> players = game.getPlayers();
        Player currentPlayer = game.getCurrentPlayer();

        if (players != null) {
            for (Player player : players) {
                int playerIndex = players.indexOf(player);
                Label playerLabel = (Label) this.getChildren().get(playerIndex);
                if (player == currentPlayer) {
                    playerLabel.setColor(Color.GREEN);
                    playerLabel.getColor().a = 1f;
                } else {
                    playerLabel.setColor(Color.WHITE);
                    playerLabel.getColor().a = 0.3f;
                }
            }
        }
        super.draw(batch, parentAlpha); // Important
    }
}
