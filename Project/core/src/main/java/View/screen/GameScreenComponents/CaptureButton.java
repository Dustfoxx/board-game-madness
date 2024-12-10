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

public class CaptureButton extends TextButton {

    private AbstractCell cell;

    public CaptureButton(GameController gameController, Skin skin) {

        super("Capture", skin);

        // TODO: This should probably be in controller
        this.cell = gameController.getGame().getBoard().getCell(0, 0);

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NormalCell normalCell = (NormalCell) cell;
                CaptureWindow window = new CaptureWindow(normalCell, true, skin);
                window.setPosition(
                    Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);                
                actor.getStage().addActor(window);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha); // Important
    }
}
