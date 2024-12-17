package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.CheckAction;
import Controller.GameController;
import Model.Player;
import View.buildingBlocks.VisualBoard;

public class MoveButton extends TextButton {
    private GameController gameController;
    private CheckAction checkAction = new CheckAction();

    public MoveButton(GameController gameController, Skin skin, VisualBoard visualBoard) {

        super("Move", skin);
        this.gameController = gameController;
        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Player currentPlayer = gameController.getGame().getCurrentPlayer();
                boolean[][] validMoves;
                if (gameController.getGame().getBoard().getPlayerCoord(currentPlayer) == null) {
                    validMoves = checkAction.getValidPlacements(currentPlayer, gameController.getGame().getBoard());
                } else {
                    validMoves = checkAction.getValidMoves(currentPlayer,
                            gameController.getGame().getBoard());
                }
                visualBoard.highlightValidCells(validMoves);
            }
        });
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // This override prevents the button from being clickable when disabled
        if (gameController.getGame().isMovementAvailable()) {
            return super.hit(x, y, touchable);
        } else {
            return null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (gameController.getGame().isMovementAvailable()) {
            this.setColor(1f, 1f, 1f, 1f);
            this.setDisabled(false);
        } else {
            this.setColor(0.6f, 0.6f, 0.6f, 0.6f * parentAlpha);
            this.setDisabled(true);
        }
        super.draw(batch, parentAlpha);
    }

}