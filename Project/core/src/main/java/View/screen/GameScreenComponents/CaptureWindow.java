package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Model.NormalCell;

public class CaptureWindow extends Window {

    String selectedFeature;
    ImageButton selectedFeatureButton;

    public CaptureWindow(NormalCell cell, boolean successfulCapture, Skin skin) {
        super("Capture Window", skin);

        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);

        // Create close button
        Button closeButton = new Button(skin, "close");
        this.getTitleTable().add(closeButton).padLeft(10).padTop(2).right();
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actor.getParent().remove(); // Closes the window
            }
        });

        // Add a message to the window
        Label messageLabel = new Label("The capture was" + successfulCapture, skin);
        this.add(messageLabel).pad(20).row();

        // Create a confirm button
        TextButton confirmButton = new TextButton("Confirm", skin);
        this.add(confirmButton).colspan(2).padTop(10).center().row();
        this.pack();
        this.setSize(500, 300);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actor.getParent().remove(); // Closes the window
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha); // Important
    }
}