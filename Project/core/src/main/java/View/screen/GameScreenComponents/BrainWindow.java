package View.screen.GameScreenComponents;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Controller.GameController.Actions;
import Model.BrainFact;
import Model.BrainNote;
import Model.Token;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BrainWindow extends Window {

    public BrainWindow(boolean isBrainsActive, GameController gameController, Skin skin) {
        super("Brain Window", skin);

        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);

        List<Token> cellBrains = gameController.getGame().getActiveBrains();
        BrainNote brainNote = new BrainNote("");

        for (Token brain : cellBrains) {
            if (brain instanceof BrainNote) {
                brainNote = (BrainNote) brain;
                Label brainNoteLabel = new Label("Brain Note:", skin);
                TextField nameField = new TextField(brainNote.getNote(), skin);
                brainNoteLabel.setFontScale(2f);
                this.add(brainNoteLabel).pad(20).row();
                this.add(nameField).expandX();
            } else {
                BrainFact tempBrain = (BrainFact) brain;
                Label brainFactLabel = new Label("Brain Note:", skin);
                Label factInfo = new Label(String.valueOf(tempBrain.getTimestamp()), skin);
                brainFactLabel.setFontScale(2f);
                this.add(brainFactLabel).pad(20).row();
                this.add(factInfo).expandX();
            }
        }

        // Create a close button
        TextButton closeButton = new TextButton("Close", skin);
        this.add(closeButton).colspan(2).padTop(10).center().row();
        this.pack();
        this.setSize(500, 200);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actor.getParent().remove(); // Closes the window
            }
        });

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.actionHandler(Actions.BRAINNOTE, new Object[] {});
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