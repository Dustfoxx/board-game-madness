package View.screen.GameScreenComponents;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.Game;
import Model.Player;

public class PlayerBar extends Table {

    private GameController gameController;

    public PlayerBar(GameController gameController, Skin skin) {
        this.gameController = gameController;

        // Create labels for each player
        for (Player player : gameController.getGame().getPlayers()) {
            Label playerLabel = new Label(player.getName(), skin, "colored");
            this.add(playerLabel).expandX();
        }

        // Button for simulating that it's the next players turn
        TextButton nextTurnButton = new TextButton("Next turn", skin);
        nextTurnButton.setColor(Color.MAGENTA);
        this.add(nextTurnButton);
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.newTurn();
            }
        });
    }

    public void update() {
        Game game = gameController.getGame();
        List<Player>  players = game.getPlayers();
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
    }
}
