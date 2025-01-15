package View.buildingBlocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
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
import View.screen.GameScreenComponents.BrainWindow;
import io.github.MindMGMT.MindMGMT;

public class VisualBoard {
    private Table board;
    private Texture bg;

    /**
     * Constructing the board using VisualCells
     *
     * @param gameInfo the gamecontroller controlling the game
     */
    public VisualBoard(GameController gameInfo, MindMGMT application) {
        this.board = new Table();
        this.bg = application.assets.get("board-high-res.png", Texture.class);
        Board boardInfo = gameInfo.getGame().getBoard();
        int[] dimensions = boardInfo.getDims();
        // board.setDebug(true);
        board.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                VisualCell cell = new VisualCell(boardInfo.getCell(i, j), gameInfo.getGame().getValidityMask()[i][j],
                        application, gameInfo);
                cell.setName(i + "" + j);

                // This is for movement actions
                cell.addListener(new ClickListener(Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        int[] cellCoords = getCellClicked(event);
                        gameInfo.actionHandler(Actions.MOVE, new Object[] { cellCoords[0], cellCoords[1] });
                    }
                });
                cell.addListener(new ClickListener(Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        int[] cellCoords = getCellClicked(event);
                        BrainWindow brainNoteWindow = new BrainWindow(
                                gameInfo, cellCoords[0], cellCoords[1], application.skin);
                        brainNoteWindow.setPosition(
                                Gdx.graphics.getWidth() / 2 - brainNoteWindow.getWidth() / 2,
                                Gdx.graphics.getHeight() / 2 - brainNoteWindow.getHeight() / 2);
                        cell.getStage().addActor(brainNoteWindow);
                    }
                });

                board.add(cell).uniform().expand();
            }
            board.row();
        }
    }

    public int[] getCellClicked(InputEvent event) {
        Actor target = event.getListenerActor();
        String cellName = target.getName();
        int row = cellName.charAt(0) - '0';
        int col = cellName.charAt(1) - '0';
        return new int[] { row, col };
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
