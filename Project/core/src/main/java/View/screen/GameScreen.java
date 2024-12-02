package View.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Model.Player;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.MindMGMT.MindMGMT;
// import Model.Player;
// import Controller.GameController;

public class GameScreen implements Screen {

    // private final GameController gameController;
    private final MindMGMT game;
    private final Stage stage;
    private final Skin skin;
    private final SpriteBatch batch;
    private final Texture boardTexture;
    private final Image boardImage;
    private int currentTurn; //TODO: Remove


    public GameScreen(MindMGMT game) {
        // this.gameController = new GameController(game.nrOfPlayers);

        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.boardTexture = new Texture("basic-board.png");
        this.boardImage = new Image(boardTexture);
        this.currentTurn = 0; //TODO: Remove

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

        // Buttons for each player
        // TODO: Update to use real model and not fake data
        List<TextButton> playerButtons = new ArrayList<>();
        for (int i = 0; i < game.nrOfPayers; i++) {
            TextButton playerButton = new TextButton("Player " + (i + 1), skin);
            if (i == currentTurn) {
                playerButton.setColor(Color.GREEN);
            }
            playerButtons.add(playerButton);
            playerBar.add(playerButton).expandX();
        }

        // Button for simulating that it's the next players turn
        // TODO: Remove button below
        TextButton nextTurnButton = new TextButton("Next turn", skin);
        nextTurnButton.setColor(Color.MAGENTA);
        playerBar.add(nextTurnButton);
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerButtons.get(currentTurn).setColor(Color.WHITE);
                currentTurn = (currentTurn + 1) % game.nrOfPayers;
                playerButtons.get(currentTurn).setColor(Color.GREEN);
            }
        });

        //get the player list from the game controller
        //there is no getgame method in gamecontroller yet, and not sure if it's needed
        // for (Player player : gameController.getGame().getPlayers()) {
        //     String playerName = player.getName();
        //     TextButton playerButton = new TextButton(playerName, skin);
        //     playerBar.add(playerButton).expandX();

        //     //highlight the current player
        //     if (player.equals(gameController.getCurrentPlayer())) {
        //         playerButton.setColor(0, 1, 0, 1);
        //     }
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
