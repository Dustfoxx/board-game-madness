package View.screen.GameScreenComponents;

import Controller.GameController;
import Model.Recruiter;
import Model.RougeAgent;
import Model.Board;
import Model.Player;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Model.Footstep;


public class RevealButton extends TextButton {
    private Player player;
    public RevealButton(GameController gameController, Skin skin) {
        super("Reveal", skin);
        this.player = gameController.getGame().getCurrentPlayer();

        // I don't think this is the right way to do the reveal action
        Recruiter recruiter = gameController.getGame().getRecruiter();
        List<int[]> walkedPath = recruiter.getWalkedPath();
        Board board = gameController.getGame().getBoard();
        //int[] playerPosition = board.getPlayerCoord(player);
        //Footstep footstep =gameController.getGame().getBoard().getCell(playerPosition[0], playerPosition[1]).getFootstep();


        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //gameController.actionController.reveal(footstep,board,playerPosition,walkedPath);
            }
        });
    }


    //Probably not working now
    @Override
    public void act(float delta) {
        super.act(delta);

        // Only visible and enabled for RougeAgent
        if (player.getClass().equals(RougeAgent.class)) {
            this.setVisible(true);
            this.setDisabled(false);
        } else {
            this.setVisible(false);
            this.setDisabled(true);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.isVisible()) {
            super.draw(batch, parentAlpha);
        }
    }
}

