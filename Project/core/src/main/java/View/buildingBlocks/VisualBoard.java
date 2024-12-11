package View.buildingBlocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.Board;

public class VisualBoard {
    private Table board = new Table();
    private Texture bg = new Texture("basic-board.png");

    public VisualBoard(GameController gameInfo) {
        Board boardInfo = gameInfo.getGame().getBoard();
        int[] dimensions = boardInfo.getDims();
        // board.setDebug(true);
        board.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                VisualCell cell = new VisualCell(boardInfo.getCell(i, j));
                String name = i + "" + j;
                cell.setName(name);
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Actor target = event.getListenerActor();
                        String cellName = target.getName();
                        int row = cellName.charAt(0) - '0';
                        int col = cellName.charAt(1) - '0';
                        System.out.println("Cell clicked");

                        gameInfo.actionHandler(Actions.MOVE, new Object[] { row, col });
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
}
