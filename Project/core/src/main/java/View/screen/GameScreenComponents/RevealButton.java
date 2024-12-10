package View.screen.GameScreenComponents;

import Controller.GameController;
import Model.Player;
import Model.RougeAgent;
import Model.Board;
import Controller.ActionController.revealResult;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class RevealButton extends TextButton {

    public RevealButton(GameController gameController, Skin skin) {
        super("Reveal", skin);

        Player player = gameController.getGame().getCurrentPlayer();
        //System.out.println(player);
        Board board = gameController.getGame().getBoard();

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //perform the reveal action
                revealResult result= gameController.actionController.reveal(player,board);
                //show whether the reveal action can be functioned
                //or there is no footstep to reveal
                RevealWindow window = new RevealWindow(result, skin);
                window.setPosition(
                    Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
                actor.getStage().addActor(window);

            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Show the button when the player is a RougeAgent
        // if (player.getClass().equals(RougeAgent.class)) {
        //     this.setDisabled(false);

        // }
        super.draw(batch, parentAlpha); // Important
    }

}


