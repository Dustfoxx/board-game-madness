package View.buildingBlocks;

import java.lang.reflect.Array;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;

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

                        boardInfo.getCell(row, col).addPlayer(new RougeAgent(1));
                        cell.UpdateCell();
                    }
                });
                board.add(cell).uniform();
            }
            board.row();
        }
    }

    public Table getVisualBoard() {

        return this.board;
    }

    public void UpdateCell(int i, int j){
        getCell(i, j).UpdateCell();

    }
    public VisualCell getCell(int i, int j) {
        String coords = i + "" + j;  // Create the coordinate string, e.g., "23" for row 2, column 3
        SnapshotArray<Actor> cellTable = board.getChildren();  // Get all children of the Table
        
        for (Actor actor : cellTable) {
            if (actor.getName().equals(coords)) {

                System.out.println("woop");
                System.out.println(actor); // Check if the actor is a VisualCell and if the name matches coordinates
                return (VisualCell) actor;  // Cast to VisualCell and return
            }
        }
        return null;  // Return null if no matching cell is found
    }
}
