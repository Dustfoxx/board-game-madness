package View.screen.GameScreenComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.Player;
import Model.RougeAgent;
import Controller.GameController.Actions;

public class CaptureButton extends TextButton {

    private final GameController gameController;
    private Player player;

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
    public void draw(Batch batch, float parentAlpha) {
        this.player = gameController.getGame().getCurrentPlayer();
        // Only draw the button if the current player is a rouge agent
        if (player.getClass().equals(RougeAgent.class)){
                this.setDisabled(false);
                super.draw(batch, parentAlpha);
        }else{
            this.setDisabled(true);
        }

    }
}
