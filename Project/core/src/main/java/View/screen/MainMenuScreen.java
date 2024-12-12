package View.screen;

import View.buildingBlocks.MindMGMTStage;
import io.github.MindMGMT.MindMGMT;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MainMenuScreen implements Screen {
    private final MindMGMTStage stage;
    private final MindMGMT application;

    public MainMenuScreen(final MindMGMT application) {
        this.application = application;
        stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        Gdx.input.setInputProcessor(stage);
        setupUI();
    }
    private void setupUI() {

        TextButton hostButton = new TextButton("Host Game", application.skin);
        TextButton joinButton = new TextButton("Join Game", application.skin);
        TextButton quitButton = new TextButton("Quit", application.skin);

        hostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // not sure should this in view or controller
                application.setScreen(new SetupScreen(application));
                dispose();
            }
        });
        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // not sure should this in view or controller
                application.setScreen(new JoinScreen(application));
                dispose();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        float width = stage.getCamera().viewportWidth;
        float height = stage.getCamera().viewportHeight;

        quitButton.setSize(hostButton.getWidth(), hostButton.getHeight());
        joinButton.setSize(hostButton.getWidth(), hostButton.getHeight());
        hostButton.setPosition(
            (width - hostButton.getWidth()) / 2,
            (height - hostButton.getHeight()) / (2 - 0.38f));
        joinButton.setPosition(
            (width - hostButton.getWidth()) / 2,
            (height - hostButton.getHeight()) / (2 - 0.15f));
        quitButton.setPosition(
            (width - quitButton.getWidth()) / 2,
            (height - quitButton.getHeight()) / (2 + 0.15f));

        stage.addActor(hostButton);
        stage.addActor(joinButton);
        stage.addActor(quitButton);
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
