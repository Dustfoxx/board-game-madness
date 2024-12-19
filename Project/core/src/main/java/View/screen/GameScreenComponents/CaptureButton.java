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
import Model.Player;
import Model.RougeAgent;
import Controller.GameController.Actions;

public class CaptureButton extends TextButton {

    private final GameController gameController;

    public CaptureButton(GameController gameController, Skin skin) {

        super("Capture", skin);
        this.gameController = gameController;

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                // Perform the capture
                boolean wasCaptureSuccessful = gameController.actionHandler(Actions.CAPTURE);

                // Create a window displaying the result
                CaptureWindow window = new CaptureWindow(wasCaptureSuccessful, skin);
                window.setPosition(
                        Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                        Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
                actor.getStage().addActor(window);

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
            if (currentCell != null && gameController.getGame().isActionAvailable()) {
                // Enable the button if current player is placed at board
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
