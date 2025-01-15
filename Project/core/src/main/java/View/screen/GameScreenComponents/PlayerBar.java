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
import com.badlogic.gdx.utils.SnapshotArray;

import Controller.GameController;
import Model.Game;
import Model.Player;
import Model.User;

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
        // this.debug();

        // Create a label for each user and add it to the table
        for (User user : gameController.getGame().getUsers()) {
            Table userSlot = createUserSlot(user, skin);
            this.add(userSlot).expandX(); // Expand each label equally along the X-axis
        }

        // Create a button to simulate advancing to the next player's turn - TODO:
        // Remove when not useful anymore
        TextButton nextTurnButton = new TextButton("End Turn", skin);
        nextTurnButton.setColor(new Color(168 / 255f, 193 / 255f, 187 / 255f, 1f));

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
     * Creates a table containing the player name and which characters they control
     *
     * @param user User to display
     * @param skin skin for the labels
     * @return table containing the correct labels
     */
    Table createUserSlot(User user, Skin skin) {
        Table userSlot = new Table();
        if (!user.getUserName().isEmpty()) {
            Label userLabel = new Label(user.getUserName(), skin, "big");
            userSlot.add(userLabel).center().expandX().colspan(user.getPlayerAmount()).pad(10);
            userSlot.row();
        }

        for (Player player : gameController.getGame().getPlayers()) {
            if (user.ownsPlayerPiece(player)) {
                Label playerLabel = new Label(player.getName(), skin, "big");
                userSlot.add(playerLabel).expandX().pad(10);
            }
        }
        return userSlot;
    }

    /**
     * Updates the PlayerBar to visually indicate the current player by
     * highlighting its label in green and dimming the others.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Game game = gameController.getGame();
        List<User> users = game.getUsers();
        Player currentPlayer = game.getCurrentPlayer();

        if (users != null) {
            for (User user : users) {
                int userIndex = users.indexOf(user);
                Table userSlot = (Table) this.getChildren().get(userIndex);
                SnapshotArray<Actor> labels = userSlot.getChildren();
                for (int i = 0; i < labels.size; i++) {
                    Label playerLabel = (Label) labels.get(i);
                    if (user.ownsPlayerPiece(currentPlayer)) {
                        if (playerLabel.textEquals(user.getUserName())
                                || playerLabel.textEquals(currentPlayer.getName())) {
                            
                            playerLabel.setColor(203 / 255f, 122 / 255f, 137 / 255f, 1); // Ligth blue
                            
                            playerLabel.setColor(75 / 255f, 104 / 255f, 112 / 255f, 1); // Dark blue        
                            playerLabel.setColor(173 / 255f, 82 / 255f, 97 / 255f, 1); // Dark pink

                            playerLabel.getColor().a = 1f;
                        } else {
                            playerLabel.setColor(Color.WHITE);
                            playerLabel.setColor(223 / 255f, 152 / 255f, 165 / 255f, 1); // Light pink
                            playerLabel.getColor().a = 1;
                        }
                    } else {
                        playerLabel.setColor(Color.WHITE);
                        playerLabel.getColor().a = 0.3f;
                    }
                }

            }
        }
        super.draw(batch, parentAlpha); // Important
    }
}
