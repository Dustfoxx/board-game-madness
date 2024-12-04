package View.buildingBlocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Model.Board;
import Model.RougeAgent;

public class VisualBoard {
    private Table board = new Table();
    private Texture bg = new Texture("basic-board.png");

    public VisualBoard(Board boardInfo) {
        int[] dimensions = boardInfo.getDims();
        // board.setDebug(true);
        board.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                VisualCell cell = new VisualCell(boardInfo.getCell(i, j));
                Table cellTable = cell.getVisualCell();
                String name = i + "" + j;
                cellTable.setName(name);
                cellTable.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Actor target = event.getListenerActor();
                        String cellName = target.getName();
                        System.out.println(cellName);
                        int row = cellName.charAt(0) - '0';
                        int col = cellName.charAt(1) - '0';
                        System.out.println(row);
                        System.out.println(col);

                        boardInfo.getCell(row, col).addPlayer(new RougeAgent(2));
                    }
                });
                board.add(cellTable).uniform();
            }
            board.row();
        }
    }

    public Table getVisualBoard() {
        return this.board;
    }
}
