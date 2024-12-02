package View.screen;

import Controller.ActionController;
import Controller.GameController;
import Model.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.Gdx;
import io.github.MindMGMT.MindMGMT;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    // private final GameController gameController;
    private final MindMGMT application;
    private final GameController gameController;
    private final ActionController actionController;

    private final Stage stage;
    private final Skin skin;
    private final SpriteBatch batch;
    private final Texture boardTexture;
    private final Image boardImage;

    public GameScreen(MindMGMT application) {
        this.application = application;
        Game game = initializeGame();

        this.gameController = new GameController(game);
        this.actionController = new ActionController();

        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.boardTexture = new Texture("basic-board.png");
        this.boardImage = new Image(boardTexture);

        Gdx.input.setInputProcessor(stage);
        setupUI();
    }

    private Game initializeGame() {
        List<Player> players = new ArrayList<Player>();
        Feature[] recruiterFeatures = new Feature[] { Feature.FOUNTAIN, Feature.BILLBOARD, Feature.BUS };

        for (int i = 0; i < application.nrOfPlayers; i++) {
            players.add(i == 0 ? new Recruiter(i, "recruiter", recruiterFeatures) : new RougeAgent(i));
        }

        int rows = 6;
        int columns = 7;

        AbstractCell[][] cells = new AbstractCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = new NormalCell(new Feature[] { Feature.BOOKSTORE, Feature.BILLBOARD });
            }
        }

        Board board = new Board(cells);
        return new Game(players, board, players.get(0));
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

        for (int i = 1; i <= application.nrOfPlayers; i++) {
            TextButton playerButton = new TextButton("Player " + i, skin);
            playerBar.add(playerButton).expandX();
        }

        // get the player list from the game controller
        // there is no getgame method in gamecontroller yet, and not sure if it's needed
        // for (Player player : gameController.getGame().getPlayers()) {
        // String playerName = player.getName();
        // TextButton playerButton = new TextButton(playerName, skin);
        // playerBar.add(playerButton).expandX();

        // //highlight the current player
        // if (player.equals(gameController.getCurrentPlayer())) {
        // playerButton.setColor(0, 1, 0, 1);
        // }
        // }
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

        String[] actions = { "Ask", "Move", "Reveal" };
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
