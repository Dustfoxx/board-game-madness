package View.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.MindMGMT.MindMGMT;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SetupScreen implements Screen {
    private final Stage stage;
    private final MindMGMT application;

    public SetupScreen(final MindMGMT application) {
        this.application = application;
        stage = new Stage(new ScreenViewport());
        setupUI();
    }

    private void startGame(int nrOfPlayers) {
        application.nrOfPlayers = nrOfPlayers;
        application.setScreen(new GameScreen(application));
        dispose();
    }

    private void setupUI() {
        Table root = new Table();
        root.debug();
        root.setFillParent(true);
        root.setDebug(true);

        Label question = new Label("How many players?", application.skin, "narration");
        question.setFontScale(2);
        root.add(question).colspan(2).padBottom(40);
        root.row();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        Skin skin = application.assets.get("metalui/metal-ui.json", Skin.class);
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               application.setScreen(new MainMenuScreen(application));
               dispose();
            }
        });
        root.add(backButton).width(width * 0.1f).height(height * 0.05f);

        root.row().height(height * 0.05f);
        root.add(new Image()); // Creating empty cell
        root.row().height(height * 0.05f);
        root.add(new Image()); // Creating empty cell

        for (int i = 2; i <= 5; i++) {
            final int value = i;
            TextButton playersButton = new TextButton(i + " players", application.skin);
            root.add(playersButton).pad(20);
            TextButton playersButton = new TextButton(i + " players", skin);
            root.add(playersButton).width(width * 0.1f).height(height * 0.05f);
            playersButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    startGame(value);
                }
            });
            // Start a new row for last 2 buttons
            if (i == 3) {
                root.row();
            }
        }

        root.add(new Image()).width(width * 0.1f); // Creating empty cell
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

