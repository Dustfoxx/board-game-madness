package View.screen;

import View.buildingBlocks.MindMGMTStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.MindMGMT.MindMGMT;

public class JoinScreen implements Screen {
    private final MindMGMT application;
    private final MindMGMTStage stage;

    private String name;
    private String code;


    public JoinScreen(MindMGMT application) {
        this.application = application;
        this.stage = new MindMGMTStage(new ScreenViewport(), application.assets);

        this.name = "";
        this.code = "";

        setupUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);

        Label nameLabel = new Label("Enter your name here:", application.skin);
        TextField nameField = new TextField(name, application.skin);
        Label codeLabel = new Label("Enter your code here:", application.skin);
        TextField codeField = new TextField(code, application.skin);

        TextButton backButton = new TextButton("Back", application.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                application.setScreen(new MainMenuScreen(application));
                dispose();
            }
        });

        TextButton joinButton = new TextButton("Join", application.skin);
        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                application.setScreen(new SetupScreen(application));
            }
        });


        root.add(nameLabel).colspan(2);
        root.row();
        root.add(nameField).width(stage.getWidth() * 0.25f).colspan(2);
        root.row();
        root.add(codeLabel).colspan(2).padTop(80);
        root.row();
        root.add(codeField).width(stage.getWidth() * 0.25f).colspan(2).padBottom(80);
        root.row();
        root.add(backButton);
        root.add(joinButton);
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
