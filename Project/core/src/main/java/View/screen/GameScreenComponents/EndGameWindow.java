package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Model.Game;
import Model.Game.gameStates;
import View.screen.MainMenuScreen;
import io.github.MindMGMT.MindMGMT;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EndGameWindow extends Window {

    Game gameState;
    Label messageLabel;

    public EndGameWindow(Game gameState, Skin skin, MindMGMT application) {
        super("EndGame Window", skin);

        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);
        this.gameState = gameState;

        // Add a message to the window
        String message = gameState.getWinner() + " Wins!";
        this.messageLabel = new Label(message, skin);
        messageLabel.setFontScale(2f);
        this.add(messageLabel).pad(20).row();

        // Create a close button
        TextButton closeButton = new TextButton("Close", skin);
        this.add(closeButton).colspan(2).padTop(10).center().row();
        this.pack();
        this.setSize(500, 200);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actor.getParent().remove(); // Closes the window
                application.setScreen(new MainMenuScreen(application));
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.toBack();
        this.messageLabel.setText(gameState.getWinner() + " Wins!");
        if (gameState.getGameState() == gameStates.ENDGAME) {
            this.toFront();
            super.draw(batch, parentAlpha); // Important
        }
    }
}