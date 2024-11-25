package View.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.MindMGMT.Main;

public class GameScreen implements Screen {

    private final Main game;
    private final Stage stage;
    private final Skin skin;
    private final SpriteBatch batch;
    private final Texture boardTexture;
    private final Image boardImage;


    public GameScreen(Main game) {
         this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.boardTexture = new Texture("basic-board.png");
        this.boardImage = new Image(boardTexture);

        Gdx.input.setInputProcessor(stage);
        setupUI();
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        setupPlayerBar(root);
        setupMainSection(root);
        setupActionBar(root);
    }

    private void setupPlayerBar(Table root) {
        Table playerBar = new Table();
        root.add(playerBar).expandX().fillX().top().height(stage.getViewport().getWorldHeight() * 0.1f);

        for (int i = 1; i <= 5; i++) {
            TextButton playerButton = new TextButton("Player " + i, skin);
            playerBar.add(playerButton).expandX();
        }
    }

    private void setupMainSection(Table root) {
        Table mainSection = new Table();
        root.row();
        root.add(mainSection).expand().fill();

        Table iconBar = new Table();
        mainSection.add(iconBar).expandY().fillY().width(Value.percentWidth(0.15f, mainSection));

        Table boardSection = new Table();
        mainSection.add(boardSection).expandY().fillY().width(Value.percentWidth(0.5f, mainSection));
        boardSection.add(boardImage).expand().fill();

        Table mindslipBar = new Table();
        mainSection.add(mindslipBar).expandY().fillY().width(Value.percentWidth(0.15f, mainSection));

        Table turnBar = new Table();
        mainSection.add(turnBar).expandY().fillY().width(Value.percentWidth(0.2f, mainSection));
    }

    private void setupActionBar(Table root) {
        Table actionBar = new Table();
        root.row();
        root.add(actionBar).expandX().fillX().bottom().height(stage.getViewport().getWorldHeight() * 0.1f);

        String[] actions = {"Ask", "Move", "Reveal"};
        for (String action : actions) {
            TextButton actionButton = new TextButton(action, skin);
            actionBar.add(actionButton).expandX();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
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
        batch.dispose();
        skin.dispose();
        stage.dispose();
        boardTexture.dispose();
    }
}
