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

public class AskButton extends TextButton {

    Stage stage;
    Skin skin;
    String selectedFeature;
    GameController gameController;

    public AskButton(String name, Skin skin, Stage stage, String selectedFeature, GameController gameController) {

        super(name, skin);
        this.stage = stage; 
        this.skin = skin;

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
        // TODO: get the feature from the model
        TextButton featureButton1 = new TextButton("Feature 1", skin);
        featureButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // get the selected feature
                selectedFeature = "Feature 1";
            }
        });
        buttonTable.add(featureButton1).padRight(20).padBottom(10);

        // Create a button for the second feature
        // TODO: get the feature from the model
        TextButton featureButton2 = new TextButton("Feature 2", skin);
        featureButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedFeature = "Feature 2";
            }
        });
        buttonTable.add(featureButton2).padBottom(10);

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
        askWindow.setSize(300, 200);
        askWindow.setPosition(stage.getWidth() / 2 - askWindow.getWidth() / 2,
                stage.getHeight() / 2 - askWindow.getHeight() / 2);

        return askWindow;
    }
}
