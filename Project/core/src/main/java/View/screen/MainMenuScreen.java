package View.screen;

import Model.User;
import View.buildingBlocks.MindMGMTStage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

import java.util.ArrayList;

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
        Table root = new Table();
        root.setFillParent(true);

        TextButton singlePlayerButton = new TextButton("Local game", application.skin);
        TextButton hostButton = new TextButton("Host Game", application.skin);
        TextButton joinButton = new TextButton("Join Game", application.skin);
        TextButton quitButton = new TextButton("Quit", application.skin);

        singlePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // not sure should this in view or controller
                ArrayList<User> users = new ArrayList<>();
                users.add(new User(0, ""));
                application.setScreen(new GameScreen(application, users, true));
                dispose();
            }
        });
        hostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // not sure should this in view or controller
                application.setScreen(new SetupScreen(application, true));
                dispose();
            }
        });
        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // not sure should this in view or controller
                application.setScreen(new SetupScreen(application, false));
                dispose();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Image titleImg = new Image(application.assets.get("mindmgmt.png", Texture.class));
        root.add(titleImg).padBottom(100).row();

        hostButton.setSize(singlePlayerButton.getWidth(), singlePlayerButton.getHeight());
        quitButton.setSize(singlePlayerButton.getWidth(), singlePlayerButton.getHeight());
        joinButton.setSize(singlePlayerButton.getWidth(), singlePlayerButton.getHeight());
        root.add(singlePlayerButton).padBottom(20).row();
        root.add(hostButton).padBottom(20).row();
        root.add(joinButton).padBottom(20).row();
        root.add(quitButton).padBottom(20).row();
        stage.addActor(root);
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
