package View.screen;

import Controller.ServerComponents.MindMGMTServer;
import Model.User;
import View.buildingBlocks.MindMGMTStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.MindMGMT.MindMGMT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LobbyScreen implements Screen {
    private final MindMGMT application;
    private final MindMGMTStage stage;
    private final ArrayList<String> players;
    private final Label[] labels;

    private final int pollingFrequency;
    private Net.HttpResponseListener pollListener;
    private int frameCount;
    private final boolean isHost;

    private boolean gameStarted;

    public LobbyScreen(MindMGMT application, String hostName)  {
        this.application = application;
        this.stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        this.players = new ArrayList<>();
        this.players.add(hostName);
        this.players.add("Dummy player");
        this.labels = new Label[5];

        this.frameCount = 0;
        this.pollingFrequency = 30;
        this.isHost = hostName != null;
        this.gameStarted = false;

        if (this.isHost) {
            this.application.server = new MindMGMTServer(8080, players);
            this.application.server.start();
        } else {
            this.pollListener = getPollListener();
        }

        setupUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.debug();
        root.add(new Label("Lobby", application.skin, "narration")).padTop(10).colspan(2);
        for (int i = 0; i < labels.length; i++) {
            if (i % 2 == 0) {
                root.row();
            }
            labels[i] = new Label((players.size() <= i ? "" : players.get(i)), application.skin);
            root.add(labels[i]).colspan(i == labels.length - 1 ? 2 : 1);
        }
        root.row();



        TextButton backButton = new TextButton("Back", application.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (application.server != null) {
                    application.server.stop();
                }
                application.setScreen(new SetupScreen(application));
                dispose();
            }
        });

        if (isHost) {
            TextButton startButton = new TextButton("Start", application.skin);
            startButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<User> users = new ArrayList<>();
                    for (int i = 0; i < players.size(); i++) {
                        users.add(new User(i, players.get(i)));
                    }
                    application.setScreen(new GameScreen(application, users));
                    dispose();
                }
            });

            root.add(backButton);
            root.add(startButton);
        } else {
            root.add(backButton).colspan(2);
        }
        stage.addActor(root);
    }

    private Net.HttpResponseListener getPollListener() {
        return new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String msg = httpResponse.getResultAsString();

                if (msg.equals("ingame")) {
                    gameStarted = true;
                } else {
                    String[] incomingPlayers = msg.split(",");

                    players.clear();
                    players.addAll(Arrays.asList(incomingPlayers));
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        };
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (gameStarted) {
            ArrayList<User> users = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                users.add(new User(i, players.get(i)));
            }
            application.setScreen(new GameScreen(application, users));
            dispose();
        } else if (!isHost && frameCount >= pollingFrequency) {
            frameCount = 0;
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
            Net.HttpRequest httpRequest = requestBuilder
                .newRequest()
                .method(Net.HttpMethods.GET)
                .url("http://localhost:8080/poll")
                .build();
            Gdx.net.sendHttpRequest(httpRequest, pollListener);
        }
        ScreenUtils.clear(Color.BLACK);
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < players.size(); i++) {
            labels[i].setText(players.get(i));
        }

        stage.act();
        stage.draw();
        frameCount++;
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
