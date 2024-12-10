package View.buildingBlocks;

import java.lang.reflect.Array;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;

import Controller.ActionController;
import Controller.GameController;
import Controller.RecruiterActionController;
import Model.Board;
import Model.Game;
import Model.Player;
import Model.RougeAgent;

public class VisualBoard {
    private Table board = new Table();
    private Texture bg = new Texture("basic-board.png");
    public VisualBoard(Board boardInfo, Game game) {

        RecruiterActionController actionController = new RecruiterActionController();
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
                        int row = cellName.charAt(0) - '0';
                        int col = cellName.charAt(1) - '0';
                        Player currentPlayer = game.getCurrentPlayer();

                        int[] newCoors = {row, col};
                        actionController.move(game.getBoard(), currentPlayer, newCoors);

                      UpdateAllCells();
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

    public void highlightValidCells(boolean[][] mask) {
        int rows = mask.length;
        int cols = mask[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mask[i][j]) {
                    VisualCell cell = getCell(i, j);
                    if (cell != null) {
                        cell.highlightCell(true);
                    }
                }
            }
        }
    }


    // Used to update individual cells (saves performance opposed to updateAllCells)
    public void UpdateCell(int i, int j){
        getCell(i, j).UpdateCell();

    }

    public VisualCell getCell(int i, int j) {
        String coords = i + "" + j; 
        SnapshotArray<Actor> cellTable = board.getChildren();
        
        for (Actor actor : cellTable) {
            if (actor.getName().equals(coords)) {

                System.out.println(actor);
                return (VisualCell) actor;
            }
        }
        return null;
    }

    public void UpdateAllCells() {
        SnapshotArray<Actor> children = board.getChildren();
        for (Actor actor : children) {
            if (actor instanceof VisualCell) {
                ((VisualCell) actor).UpdateCell();
            }
        }
    }
}
