package View.screen.GameScreenComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.AbstractCell;
import Model.NormalCell;
import Model.Player;
import Model.RougeAgent;

public class AskButton extends TextButton {

    private final GameController gameController;

    public AskButton(GameController gameController, Skin skin) {

        super("Ask", skin);

        this.gameController = gameController;

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AskWindow askActionWindow = new AskWindow(skin, gameController);
                askActionWindow.setPosition(
                        Gdx.graphics.getWidth() / 2 - askActionWindow.getWidth() / 2,
                        Gdx.graphics.getHeight() / 2 - askActionWindow.getHeight() / 2);
                actor.getStage().addActor(askActionWindow);
            }
        });
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // This override prevents the button from being clickable when disabled
        if (this.isDisabled()) {
            return null;
        } else {
            return super.hit(x, y, touchable);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Player currentPlayer = gameController.getGame().getCurrentPlayer();
        if (currentPlayer instanceof RougeAgent) {
            // Draw the button if it's a rouge agent's turn
            super.draw(batch, parentAlpha); // Important
            // Get the cell of the current player
            AbstractCell currentCell = gameController.getGame().getCurrentPlayerCell();
            if (currentCell != null
                    && currentCell instanceof NormalCell
                    && !currentCell.containsBrainFact()
                    && !currentCell.containsFootstep()
                    && gameController.getGame().isActionAvailable()) {
                // Enable the button if current player is placed at a normal cell
                this.setColor(Color.WHITE);
                this.setDisabled(false);
            } else {
                // Disable and shadow the button otherwise
                this.setColor(0.6f, 0.6f, 0.6f, 1f);
                this.setDisabled(true);
            }
        } else {
            // Disable the button if it's the recruiter's turn
            this.setDisabled(true);
        }
    }
}
