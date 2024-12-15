package View.buildingBlocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.Board;
import View.screen.GameScreenComponents.BrainWindow;

public class VisualBoard {
    private Table board = new Table();
    private Texture bg = new Texture("basic-board.png");

    /**
     * Constructing the board using VisualCells
     * 
     * @param gameInfo the gamecontroller controlling the game
     */
    public VisualBoard(GameController gameInfo, Skin skin) {
        Board boardInfo = gameInfo.getGame().getBoard();
        int[] dimensions = boardInfo.getDims();
        // board.setDebug(true);
        board.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                VisualCell cell = new VisualCell(boardInfo.getCell(i, j));
                String name = i + "" + j;
                cell.setName(name);
                // This is for movement actions
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        int[] cellCoords = getCellClicked(event);
                        System.out.println("Cell clicked");

                        gameInfo.actionHandler(Actions.MOVE, new Object[] { cellCoords[0], cellCoords[1] });
                    }
                });
                cell.addListener(new ClickListener(Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        int[] cellCoords = getCellClicked(event);
                        BrainWindow brainNoteWindow = new BrainWindow(
                                gameInfo.actionHandler(Actions.BRAINNOTE,
                                        new Object[] { cellCoords[0], cellCoords[1] }),
                                gameInfo, cellCoords[0], cellCoords[1], skin);
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
}
