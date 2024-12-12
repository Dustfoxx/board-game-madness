package View.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
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

    private void setupUI() {
        Table root = new Table();
        root.setDebug(true);
        root.setFillParent(true);

        for (int i = 0; i < application.nrOfPlayers; i++) {
            Label nameLabel;
            if (i == 0) {
                nameLabel = new Label("Recruiter", application.skin);
            } else {
                nameLabel = new Label("Agent " + i, application.skin);
            }
            nameLabel.setFontScale(2);
            TextField nameField = new TextField("", application.skin);
            nameField.setTextFieldListener(new TextFieldListener() {
                @Override
                public void keyTyped(TextField nameField, char c) {
                }
            });
            root.add(nameLabel).colspan(2).width(500).left();
            root.row();
            root.add(nameField).colspan(2).width(500).fillX();
            root.row();
        }

        TextButton startButton = new TextButton("Start game", application.skin);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ArrayList<String> names = new ArrayList<String>();
                SnapshotArray<Actor> children = actor.getParent().getChildren();
                for (Actor child : children) {
                    if (child.getClass().equals(TextField.class)) {
                        names.add(((TextField) child).getText());
                    }
                }
                application.setScreen(new GameScreen(application, names));
                dispose();
            }
        });

        TextButton backButton = new TextButton("Back", application.skin);
        backButton.setSize(startButton.getWidth(), startButton.getHeight());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                application.setScreen(new SetupScreen(application));
                dispose();
            }
        });

        root.add(backButton);
        root.add(startButton);
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
