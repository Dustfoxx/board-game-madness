package View.screen.GameScreenComponents;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.RougeAgent;
import Model.AbstractCell;
import Model.Player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class RevealButton extends TextButton {

    private Player player;
    private final GameController gameController;

    public RevealButton(GameController gameController, Skin skin) {
        super("Reveal", skin);
        this.player = gameController.getGame().getCurrentPlayer();
        this.gameController = gameController;

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.actionHandler(Actions.REVEAL);
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
        this.player = gameController.getGame().getCurrentPlayer();
        if (player.getClass().equals(RougeAgent.class)) {
            super.draw(batch, parentAlpha);
            AbstractCell currentCell = gameController.getGame().getCurrentPlayerCell();
                if (currentCell != null && currentCell.containsFootstep() && !currentCell.containsBrainFact()) {
                    this.setColor(Color.WHITE);
                    this.setDisabled(false);
                } else {
                    this.setColor(0.6f, 0.6f, 0.6f, 1f);
                    this.setDisabled(true);
                }
            super.draw(batch, parentAlpha);
            this.setDisabled(false);
        } else {
            this.setDisabled(true);
        }
    }

}
