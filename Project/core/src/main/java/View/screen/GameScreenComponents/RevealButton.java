package View.screen.GameScreenComponents;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.RougeAgent;
import Model.AbstractCell;
import Model.Player;

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
                AbstractCell cell = gameController.getGame().getCurrentPlayerCell();
                if (cell.containsFootstep()) {
                    gameController.actionHandler(Actions.REVEAL);
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.player = gameController.getGame().getCurrentPlayer();
        if (player.getClass().equals(RougeAgent.class)) {
            super.draw(batch, parentAlpha);
            this.setDisabled(false);
        } else {
            this.setDisabled(true);
        }
    }

}
