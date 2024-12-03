package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerBar extends Table{

    private final List<TextButton> playerButtons = new ArrayList<>();
    private MockedGame mockedGame;

    public PlayerBar (MockedGame mockedGame, int playerAmount, Skin skin) {
        this.mockedGame = mockedGame;
        // Buttons for each player
        // TODO: Update to use real model and not fake data
        for (int i = 0; i < playerAmount; i++) {
            int currentTurn = mockedGame.getTurn();
            TextButton playerButton = new TextButton("Player " + (i + 1), skin);
            if (i == currentTurn) {
                playerButton.setColor(Color.GREEN);
            }
            playerButtons.add(playerButton);
            this.add(playerButton).expandX();
        }

        // Button for simulating that it's the next players turn
        // TODO: Remove button below
        TextButton nextTurnButton = new TextButton("Next turn", skin);
        nextTurnButton.setColor(Color.MAGENTA);
        this.add(nextTurnButton);
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int currentTurn = mockedGame.getTurn();
                playerButtons.get(currentTurn).setColor(Color.WHITE);
                if (currentTurn == playerAmount - 1) {
                    mockedGame.setTime(mockedGame.getTime() + 1);
                }
                mockedGame.setTurn((currentTurn + 1) % playerAmount);
                playerButtons.get(mockedGame.getTurn()).setColor(Color.GREEN);
            }
        });

        //get the player list from the game controller
        //there is no getgame method in gamecontroller yet, and not sure if it's needed
        // for (Player player : gameController.getGame().getPlayers()) {
        //     String playerName = player.getName();
        //     TextButton playerButton = new TextButton(playerName, skin);
        //     playerBar.add(playerButton).expandX();

        //     //highlight the current player
        //     if (player.equals(gameController.getCurrentPlayer())) {
        //         playerButton.setColor(0, 1, 0, 1);
        //     }
        // }

    }
}
