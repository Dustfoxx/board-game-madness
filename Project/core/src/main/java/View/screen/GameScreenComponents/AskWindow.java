package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Model.Feature;
import Model.NormalCell;
import View.buildingBlocks.VisualCell;


public class AskWindow extends Window {

    String selectedFeature;

    public AskWindow(Skin skin, NormalCell cell) {
        super("Ask Window", skin);
        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);

        // Create a button for closing the window
        Button closeButton = new Button(skin, "close");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });

        // Add the close button to the window
        this.getTitleTable().add(closeButton).padLeft(10).padTop(2).right();

        // Add the message to the window
        Label messageLabel = new Label("Which feature do you want to ask about?", skin);
        this.add(messageLabel).pad(20).row();

        // Create a table for the feature buttons
        Table buttonTable = new Table();

        // Create a button for the first feature
        Feature[] features = cell.getFeatures();
        ImageButton[] selectedButton = { null }; // To track the currently selected button

        for (Feature feature : features) {
            VisualCell visualCell = new VisualCell(cell);
            TextureRegion featureTexture = visualCell.fetchFeature(feature);
            TextureRegionDrawable drawable = new TextureRegionDrawable(featureTexture);
            drawable.setMinWidth(100);
            drawable.setMinHeight(100);
            ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
            buttonStyle.up = drawable;
            buttonStyle.down = drawable.tint(Color.BROWN);
            ImageButton featureButton = new ImageButton(buttonStyle);

            featureButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // Resize previously selected button back to normal
                    if (selectedButton[0] != null) {
                        selectedButton[0].setSize(100, 100); // Reset size
                        // selectedButton[0].addAction(Action.fadeIn());
                    }

                    // Set the newly selected button
                    selectedButton[0] = featureButton;
                    selectedFeature = feature.name();

                    // Increase the size of the selected button
                    featureButton.setSize(120, 120); // New size for the selected button

                    selectedFeature = feature.name();
                }
            });
            buttonTable.add(featureButton).padRight(20).padBottom(5);
        }

        // Add feature buttons to the window
        this.add(buttonTable).colspan(2).center().row();

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: Send the selected feature to the game controller
                System.out.println("Feature selected" + selectedFeature);
                remove();
            }
        });
        this.add(confirmButton).colspan(2).padTop(10).center().row();

        this.pack();
        this.setSize(500, 300);
    }

    @Override
    public boolean remove() {
        return super.remove();
    }
}