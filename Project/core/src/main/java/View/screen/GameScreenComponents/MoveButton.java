package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.CheckAction;
import Controller.GameController;
import Controller.RecruiterActionController;
import Model.AbstractCell;
import Model.NormalCell;
import Model.Player;
import Model.Recruiter;
import Model.RougeAgent;
import View.buildingBlocks.VisualBoard;

public class MoveButton extends TextButton {

    RecruiterActionController controller = new RecruiterActionController();
    GameController gameController;
    CheckAction checkAction = new CheckAction();
    private AbstractCell cell;

    public MoveButton(GameController gameController, Skin skin, VisualBoard visualBoard) {

        super("Move", skin);
        this.gameController = gameController;
        this.cell = gameController.getGame().getBoard().getCell(0, 0);
        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Player currentPlayer = gameController.getGame().getCurrentPlayer();
                boolean[][] validMoves;
                if(gameController.getGame().getBoard().getPlayerCoord(currentPlayer) == null){
                        validMoves = checkAction.getValidPlacements(gameController.getGame().getBoard());
                }
                else {
                if(currentPlayer instanceof RougeAgent) {
                 validMoves = checkAction.getValidMoves((RougeAgent) currentPlayer, gameController.getGame().getBoard());
                }
                else{
                validMoves = checkAction.getValidMoves((Recruiter) currentPlayer, gameController.getGame().getBoard(), 0);
                }                    
            }
                visualBoard.highlightValidCells(validMoves);
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