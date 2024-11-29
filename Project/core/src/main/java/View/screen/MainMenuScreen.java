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
    private final MindMGMT game;

    public MainMenuScreen(final MindMGMT game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        TextButton startButton = new TextButton("Game Start", game.assets.get("metalui/metal-ui.json", Skin.class));
        startButton.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2 ,Align.center);
        startButton.setSize(100,50);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //not sure should this in view or controller
                game.setScreen(new SetupScreen(game));
                dispose();
            }
        });

        stage.addActor(startButton);
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

