package View.screen.GameScreenComponents;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.BrainFact;
import Model.BrainNote;
import Model.Recruiter;
import Model.Token;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BrainWindow extends Window {

    private String brainNoteString = "";
    private GameController gameController;
    private TextField brainField;

    public BrainWindow(GameController gameController, int row, int col, Skin skin) {
        super("", skin);

        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);
        this.gameController = gameController;

        List<Token> cellBrains = gameController.getGame().getBoard().getCell(row, col).getBrains();

        for (Token brain : cellBrains) {
            if (brain instanceof BrainFact) {
                Label brainFactLabel = new Label("Recruiter was here turn " + ((BrainFact) brain).getTimestamp(),
                        skin);
                this.add(brainFactLabel).pad(20).row();
            } else {
                brainNoteString = ((BrainNote) brain).getNote();
            }
        }

        Label brainNoteLabel = new Label("Brain Note:", skin);
        this.brainField = new TextField(brainNoteString, skin);
        brainField.setMaxLength(30);
        this.add(brainNoteLabel).pad(20).row();
        this.add(brainField).expandX().fillX().row();

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SnapshotArray<Actor> children = actor.getParent().getChildren();
                for (Actor child : children) {
                    if (child instanceof TextField) {
                        brainNoteString = ((TextField) child).getText();
                    }
                }
                gameController.actionHandler(Actions.BRAINNOTE, new Object[] { brainNoteString, row, col });
                actor.getParent().remove(); // Closes the window
            }
        });
        this.add(confirmButton).colspan(2).padTop(10).center().row();
        this.pack();
        this.setSize(500, 300);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (gameController.getGame().getCurrentPlayer() instanceof Recruiter) {
            brainField.setTouchable(Touchable.disabled);
        } else {
            brainField.setTouchable(Touchable.enabled);
        }
        super.draw(batch, parentAlpha); // Important
    }
}