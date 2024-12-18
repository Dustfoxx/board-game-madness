package View.screen.GameScreenComponents;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import Model.Token;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BrainWindow extends Window {

    private List<Token> cellBrains;
    private String brainNoteString = "";

    public BrainWindow(GameController gameController, int row, int col, Skin skin) {
        super("Brain Window", skin);

        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);

        cellBrains = gameController.getGame().getBoard().getCell(row, col).getBrains();

        for (Token brain : cellBrains) {
            if (brain instanceof BrainFact) {
                Label brainFactLabel = new Label("Recruiter was here turn " + ((BrainFact) brain).getTimestamp(),
                        skin);
                brainFactLabel.setFontScale(2f);
                this.add(brainFactLabel).pad(20).row();
            } else {
                brainNoteString = ((BrainNote) brain).getNote();
            }
        }

        Label brainNoteLabel = new Label("Brain Note:", skin);
        TextField nameField = new TextField(brainNoteString, skin);
        nameField.setMaxLength(30);
        brainNoteLabel.setFontScale(2f);
        this.add(brainNoteLabel).pad(20).row();
        this.add(nameField).expandX().fillX().row();

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
        super.draw(batch, parentAlpha); // Important
    }
}