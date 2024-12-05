package View.screen;

import io.github.MindMGMT.MindMGMT;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen implements Screen {
    private final Stage stage;
    private final MindMGMT application;

    public MainMenuScreen(final MindMGMT application) {
        this.application = application;
        stage = new Stage(new ScreenViewport());
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        Skin skin = application.assets.get("metalui/metal-ui.json", Skin.class);

        TextButton startButton = new TextButton("Game Start", skin);
        TextButton quitButton = new TextButton("Quit " + width + " " + height, skin);

        TextButton startButton = new TextButton("Start Game", application.skin);
        startButton.setPosition(
            (stage.getWidth() - startButton.getWidth()) / 2,
            (stage.getHeight() - startButton.getHeight()) / 2);
        startButton.setPosition(width / 2, height / (2 - 0.15f), Align.center);
        quitButton.setPosition(width / 2, height / (2 + 0.15f), Align.center);

        startButton.setSize(width * 0.1f, height * 0.05f);
        quitButton.setSize(width * 0.1f, height * 0.05f);

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // not sure should this in view or controller
                application.setScreen(new SetupScreen(application));
                dispose();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(startButton);
        stage.addActor(quitButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
