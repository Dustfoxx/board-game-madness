package View.buildingBlocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Model.Board;

public class VisualBoard {
    private Table board = new Table();
    private Texture bg = new Texture("basic-board.png");

    public VisualBoard(Board boardInfo) {
        int[] dimensions = boardInfo.getDims();
        board.setDebug(true);
        board.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                VisualCell cell = new VisualCell(boardInfo.getCell(i, j));
                board.add(cell.getVisualCell()).uniform();
            }
            board.row();
        }
    }

    public Table getVisualBoard() {
        return this.board;
    }
}
