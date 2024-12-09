package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.AbstractCell;
import Model.NormalCell;

public class AskButton extends TextButton {

    Stage stage;
    Skin skin;
    String selectedFeature;
    GameController gameController;
    AbstractCell cell;

    public AskButton(Skin skin, Stage stage, GameController gameController) {

        super("Ask", skin);
        this.stage = stage;
        this.skin = skin;
        this.gameController = gameController;

        // TODO: This should probbly be in controller
        this.cell = gameController.getGame().getBoard().getCell(0, 0);

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NormalCell normalCell = (NormalCell) cell;
                AskWindow askActionWindow = new AskWindow(skin, normalCell, stage);
                stage.addActor(askActionWindow);
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
        // Enable the button
        if (cell.getClass().equals(NormalCell.class)) {
            this.setDisabled(false);
            // Disable the button
        } else {
            this.setColor(0.8f, 0.8f, 0.8f, 1f);
            this.setDisabled(true);
        }
        super.draw(batch, parentAlpha); // Important
    }

}
