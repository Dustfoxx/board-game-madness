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
import Model.NormalCell;


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
                // Not sure if this is working now
                Recruiter recruiter = gameController.getGame().getRecruiter();
                List<int[]> walkedPath = recruiter.getWalkedPath();
                Board board = gameController.getGame().getBoard();
                int[] playerPosition = board.getPlayerCoord(player);
                NormalCell cell = (NormalCell) board.getCell(playerPosition[0], playerPosition[1]);
                if (cell.containsFootstep()) {
                    Footstep footstep = cell.getFootstep();
                    gameController.actionController.reveal(footstep,board,playerPosition,walkedPath);
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
            this.player = gameController.getGame().getCurrentPlayer();
            if (player.getClass().equals(RougeAgent.class)){
                super.draw(batch, parentAlpha);
            }
    }

}

