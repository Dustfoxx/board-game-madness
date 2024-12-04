package View.screen.GameScreenComponents;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import View.screen.MainMenuScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;


import io.github.MindMGMT.MindMGMT;

public class SettingWindow extends Window {
    private final Stage stage;

    public SettingWindow(Skin skin, Stage stage, MindMGMT application) {
        super("Settings", skin);

        this.stage = stage;

        this.setModal(true);
        this.setMovable(false);

        Label pauseLabel = new Label("Game Paused", skin);
        pauseLabel.setFontScale(1.5f);
        this.add(pauseLabel).pad(10).row();

        TextButton resumeButton = new TextButton("Resume Game", skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO: add game resume logic in game controller
                remove();
            }
        });
        //the size can be responsive
        this.add(resumeButton).width(200).height(50).pad(10).row();

        TextButton mainMenuButton = new TextButton("Back to Main Menu", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                application.setScreen(new MainMenuScreen(application));
            }
        });
        //the size can be responsive
        this.add(mainMenuButton).width(200).height(50).pad(10).row();
    }
    public void init(){
        updateSize();

    }
    public void updateSize(){
        this.setSize(stage.getWidth(), stage.getHeight());
    }
}
