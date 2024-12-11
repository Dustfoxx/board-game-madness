package View.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import View.buildingBlocks.MindMGMTStage;
import io.github.MindMGMT.MindMGMT;

public class PlayersScreen implements Screen {
    private final MindMGMTStage stage;
    private final MindMGMT application;

    public PlayersScreen(final MindMGMT application) {
        this.application = application;
        stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        setupUI();
    }

    private void startGame() {
        application.setScreen(new GameScreen(application));
        dispose();
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);

        float width = stage.getWidth();
        float height = stage.getHeight();

        TextButton playersButton = new TextButton("Start game", application.skin);
        root.add(playersButton).pad(20);
        playersButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
            }
        });

        root.row();

        TextButton backButton = new TextButton("Back", application.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                application.setScreen(new MainMenuScreen(application));
                dispose();
            }
        });
        root.add(backButton).colspan(2).width(width * 0.1f).height(height * 0.05f);
        stage.addActor(root);
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
