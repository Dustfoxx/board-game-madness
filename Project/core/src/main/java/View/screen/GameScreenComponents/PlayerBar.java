package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.Game;
import Model.Player;

import java.util.ArrayList;

public class PlayerBar extends Table{

    private final ArrayList<TextButton> playerButtons = new ArrayList<>();
    private GameController gameController;

    public PlayerBar (GameController gameController, Skin skin) {
        this.gameController = gameController;

        // Create buttons for each player
        for (Player player : gameController.getGame().getPlayers()) {
            TextButton playerButton = new TextButton(player.getName(), skin);
            playerButtons.add(playerButton);
            this.add(playerButton).expandX();
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
        if (game.getPlayers() != null) {
            for (Player player : game.getPlayers()) {
                int currentPlayerIndex = game.getPlayers().indexOf(player);
                if (player == game.getCurrentPlayer()) {
                    playerButtons.get(currentPlayerIndex).setColor(Color.GREEN);
                    playerButtons.get(currentPlayerIndex).getColor().a = 1f;
                } else {
                    playerButtons.get(currentPlayerIndex).setColor(Color.WHITE);
                    playerButtons.get(currentPlayerIndex).getColor().a = 0.3f;
                }
            }
        }
    }

    public ArrayList<TextButton> getPlayerButtons() {
        return this.playerButtons;
    }
}
