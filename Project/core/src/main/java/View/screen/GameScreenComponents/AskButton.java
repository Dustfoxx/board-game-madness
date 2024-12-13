package View.screen.GameScreenComponents;

import com.badlogic.gdx.Gdx;
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

    private AbstractCell cell;
    private final GameController gameController;
    private Player player;

    public AskButton(GameController gameController, Skin skin) {

        super("Ask", skin);

        // TODO: This should probbly be in controller
        this.cell = gameController.getGame().getBoard().getCell(0, 0);
        this.gameController = gameController;

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NormalCell normalCell = (NormalCell) cell;
                AskWindow askActionWindow = new AskWindow(skin, normalCell, gameController);
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
        if (cell.getClass().equals(NormalCell.class)) {
            return super.hit(x, y, touchable);
        } else {
            return null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.player = gameController.getGame().getCurrentPlayer();
        if (player.getClass().equals(RougeAgent.class)) {
            super.draw(batch, parentAlpha); // Important
            // Enable the button
            if (cell.getClass().equals(NormalCell.class)) {
                this.setDisabled(false);
                // Disable the button
            } else {
                this.setColor(0.8f, 0.8f, 0.8f, 1f);
                this.setDisabled(true);
            }
        } else {
            this.setDisabled(true);
        }

    }

}
