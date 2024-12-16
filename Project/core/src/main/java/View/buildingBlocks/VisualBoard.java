package View.buildingBlocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.Board;

public class VisualBoard {
    private Table board = new Table();
    private Texture bg = new Texture("basic-board.png");

    /**
     * Constructing the board using VisualCells
     * 
     * @param gameInfo the gamecontroller controlling the game
     */
    public VisualBoard(GameController gameInfo) {
        Board boardInfo = gameInfo.getGame().getBoard();
        int[] dimensions = boardInfo.getDims();
        // board.setDebug(true);
        board.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                VisualCell cell = new VisualCell(boardInfo.getCell(i, j));
                cell.setName(i + "" + j);
                
                // This is for movement actions
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Actor target = event.getListenerActor();
                        String cellName = target.getName();
                        int row = cellName.charAt(0) - '0';
                        int col = cellName.charAt(1) - '0';
                        gameInfo.actionHandler(Actions.MOVE, new Object[] { row, col });
                        dehighlightValidCells(dimensions[0], dimensions[1]);
                    }
                });
                board.add(cell).uniform().expand();
            }
            board.row();
        }
    }

    public Table getVisualBoard() {
        return this.board;
    }

    public void highlightValidCells(boolean[][] mask) {
        int rows = mask.length;
        int cols = mask[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mask[i][j]) {
                    VisualCell cell = getVisualCell(i, j);
                    if (cell != null) {
                        cell.highlightCell(true);
                    }
                }
            }
        }
    }

    private void dehighlightValidCells(int dimx, int dimy) {
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                    VisualCell cell = getVisualCell(i, j);
                    if (cell != null) {
                        cell.highlightCell(false);
                    }
                }
            }
        }
        

    private VisualCell getVisualCell(int i, int j) {
        String coords = i + "" + j; 
        SnapshotArray<Actor> cellTable = board.getChildren();
        
        for (Actor actor : cellTable) {
            if (actor.getName().equals(coords)) {
                return (VisualCell) actor;
            }
        }
        return null;
    }
}
