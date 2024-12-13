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
import Model.Recruiter;
import Model.TempleCell;

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
        // Disable the button if it's the recruiters turn
        if (currentPlayer instanceof Recruiter) {
            this.setDisabled(true);
        } else {
            super.draw(batch, parentAlpha); // Important
            AbstractCell currentCell = gameController.getGame().getCurrentPlayerCell();
            if (currentCell != null) {
                // Disable the button if it's a temple cell
                if (currentCell instanceof TempleCell) {
                    this.setColor(0.6f, 0.6f, 0.6f, 1f);
                    this.setDisabled(true);
                // Enable the button if it's a normal cell
                } else {
                    this.setColor(Color.WHITE);
                    this.setDisabled(false);
                }
            }
        }
    }
}
