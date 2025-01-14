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

        Label pauseLabel = new Label("Game Paused", skin, "title", "white");
        pauseLabel.setFontScale(1.5f);
        this.add(pauseLabel).pad(10).row();

        TextButton resumeButton = new TextButton("Resume Game", skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: add game resume logic in game controller
                remove();
            }
        });
        // the size can be responsive
        this.add(resumeButton).pad(10).row();

        TextButton mainMenuButton = new TextButton("Back to Main Menu", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (application.server != null) {
                    application.server.stop();
                }
                application.setScreen(new MainMenuScreen(application));
                remove();
            }
        });
        // the size can be responsive
        this.add(mainMenuButton).pad(10).row();
    }

    public void updateSize() {
        this.setSize(stage.getWidth(), stage.getHeight());
    }
}
