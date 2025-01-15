package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Controller.GameController;
import Controller.GameController.Actions;

import Model.Feature;
import Model.MutableBoolean;
import Model.NormalCell;
import View.buildingBlocks.VisualCell;
import io.github.MindMGMT.MindMGMT;

public class AskWindow extends Window {

    Feature selectedFeature;
    ImageButton selectedFeatureButton;
    Table buttonTable;
    GameController gameController;

    public AskWindow(GameController gameController, MindMGMT application) {
        super("Ask Win", application.skin);
        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);
        this.buttonTable = new Table(); // Create a table for the feature buttons
        this.add(buttonTable).colspan(2).center().row();
        this.gameController = gameController;

        // Create a button for closing the window
        Button closeButton = new Button(application.skin, "close");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AskWindow.this.remove(); // Closes the window
            }
        });

        // Add the close button to the window
        this.getTitleTable().add(closeButton).padLeft(10).padTop(2).right();

        // Add the message to the window
        Label messageLabel = new Label("Choose a feature", application.skin);
        this.add(messageLabel).pad(20).row();

        // Get the cell of the current player
        NormalCell cell = (NormalCell) gameController.getGame().getCurrentPlayerCell();

        // Create a button for the first feature
        Feature[] features = cell.getFeatures();

        for (Feature feature : features) {
            // Get the feature image
            VisualCell visualCell = new VisualCell(cell, new MutableBoolean(), application, gameController);
            TextureRegion featureTexture = visualCell.fetchFeature(feature);
            // Create an ImageButton based on the feature image
            TextureRegionDrawable drawable = new TextureRegionDrawable(featureTexture);
            drawable.setMinWidth(100);
            drawable.setMinHeight(100);
            ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
            buttonStyle.up = drawable;
            buttonStyle.down = drawable.tint(Color.BROWN); // Tint the button when it's pressed
            ImageButton featureButton = new ImageButton(buttonStyle);

            // Update the selected feature when the button is pressed
            featureButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (featureButton.isChecked()) {
                        selectedFeatureButton = featureButton;
                        selectedFeature = feature;
                    } else {
                        selectedFeatureButton = null;
                    }

                }
            });
            buttonTable.add(featureButton).padRight(20).padBottom(5);
        }

        TextButton confirmButton = new TextButton("Confirm", application.skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.actionHandler(Actions.ASK, new Object[] { selectedFeature });
                actor.getParent().remove(); // Closes the window
            }
        });
        this.add(confirmButton).colspan(2).padTop(10).center().row();
        this.pack();
        this.setSize(500, 300);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Reset size for all ImageButtons
        for (Actor actor : this.buttonTable.getChildren()) {
            ((ImageButton) actor).setSize(100, 100);
        }

        // Increase the size of the selected button
        if (selectedFeatureButton != null) {
            selectedFeatureButton.setSize(120, 120);
        }

        super.draw(batch, parentAlpha); // Important
    }
}
