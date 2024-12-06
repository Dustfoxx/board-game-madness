package View.screen.GameScreenComponents;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Controller.GameController;
import Model.AbstractCell;
import Model.Feature;
import Model.NormalCell;
import Model.Player;

public class AskButton extends TextButton {

    Stage stage;
    Skin skin;
    String selectedFeature;
    GameController gameController;
    NormalCell cell;

    public AskButton(String name, Skin skin, Stage stage, String selectedFeature, GameController gameController,
            NormalCell cell) {

        super(name, skin);
        this.stage = stage;
        this.skin = skin;
        this.gameController = gameController;
        this.cell = cell;

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Window askActionWindow = createAskActionWindow("Ask Action", "Which feature do you want to ask about?");
                stage.addActor(askActionWindow);
            }
        });
    }

    // Create a new window
    private Window createAskActionWindow(String title, String message) {
        // Create the window
        Window askWindow = new Window(title, skin);
        askWindow.setMovable(false);
        askWindow.setResizable(false);
        askWindow.setModal(true);

        // Create a button for closing the window
        Button closeButton = new Button(skin, "close");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                askWindow.remove();
            }
        });

        // Add the close button to the window
        askWindow.getTitleTable().add(closeButton).padLeft(10).padTop(2).right();

        // Add the message to the window
        Label messageLabel = new Label(message, skin);
        askWindow.add(messageLabel).pad(20).row();

        // Create a table for the feature buttons
        Table buttonTable = new Table();

        // Create a button for the first feature

        Feature[] features = cell.getFeatures();

        for (Feature feature : features) {
            String featureName = feature.name();
            TextButton featureButton = new TextButton(featureName, skin);
            featureButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectedFeature = featureName;
                }
            });
            buttonTable.add(featureButton).padRight(20).padBottom(10);
        }

        // Add feature buttons to the window
        askWindow.add(buttonTable).colspan(2).center().row();

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO:send the selected feature to the game controller
                System.out.println("Feature selected" + selectedFeature);
                gameController.newTurn();
                askWindow.remove();
            }
        });
        askWindow.add(confirmButton).colspan(2).padTop(10).center().row();

        askWindow.pack();
        askWindow.setSize(500, 200);
        askWindow.setPosition(stage.getWidth() / 2 - askWindow.getWidth() / 2,
                stage.getHeight() / 2 - askWindow.getHeight() / 2);

        return askWindow;
    }
}
