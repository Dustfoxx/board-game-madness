package View.screen;

import View.buildingBlocks.MindMGMTStage;
import View.screen.CommonComponents.ErrorWindow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.MindMGMT.MindMGMT;

public class SetupScreen implements Screen {
    private final MindMGMT application;
    private final MindMGMTStage stage;
    private Net.HttpResponseListener responseListener;
    private final ErrorWindow errorWindow;
    private boolean joined = false;
    private final boolean isHost;
    private TextField nameField;

    public SetupScreen(MindMGMT application, boolean isHost) {
        this.application = application;
        this.isHost = isHost;
        this.stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        if (!isHost) {
            this.responseListener = getResponseListener();
        }
        this.errorWindow = new ErrorWindow("Error", application.skin);
        this.errorWindow.setPosition(
                Gdx.graphics.getWidth() / 2f - this.errorWindow.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - this.errorWindow.getHeight() / 2);

        setupUI();
        Gdx.input.setInputProcessor(stage);
    }

    private Net.HttpResponseListener getResponseListener() {
        return new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    joined = true;
                } else if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                    errorWindow.setMessage(httpResponse.getResultAsString());
                    errorWindow.setPosition(
                            Gdx.graphics.getWidth() / 2f - errorWindow.getWidth() / 2,
                            Gdx.graphics.getHeight() / 2f - errorWindow.getHeight() / 2);
                    stage.addActor(errorWindow);
                }
            }

            @Override
            public void failed(Throwable t) {
                errorWindow.setMessage(t.getMessage());
                errorWindow.setPosition(
                        Gdx.graphics.getWidth() / 2f - errorWindow.getWidth() / 2,
                        Gdx.graphics.getHeight() / 2f - errorWindow.getHeight() / 2);
                stage.addActor(errorWindow);
            }

            @Override
            public void cancelled() {

            }
        };
    }

    private void setupUI() {
        this.errorWindow.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 5f);

        Table root = new Table();
        root.setFillParent(true);

        Label nameLabel = new Label("Enter your name here:", application.skin);
        nameField = new TextField("", application.skin);

        TextButton backButton = new TextButton("Back", application.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                application.setScreen(new MainMenuScreen(application));
                dispose();
            }
        });

        root.add(nameLabel).colspan(2);
        root.row();
        root.add(this.nameField).width(stage.getWidth() * 0.25f).colspan(2).padBottom(30);
        root.row();
        root.add(backButton);

        TextField localNameRef = this.nameField; // No idea how availability for variables work with these internal
                                                 // structures so this is bad code but functioning code

        if (isHost) {
            TextButton startButton = new TextButton("Create Lobby", application.skin);
            startButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    String name = localNameRef.getText().isEmpty() ? "Host" : localNameRef.getText();
                    application.setScreen(new LobbyScreen(application, name, name)); // Kinda awkward. Might look into
                                                                                     // checking for host in another way
                    dispose();
                }
            });
            root.add(startButton);
        } else {
            TextButton joinButton = new TextButton("Join", application.skin);
            joinButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                    Net.HttpRequest httpRequest = requestBuilder
                            .newRequest()
                            .method(Net.HttpMethods.POST)
                            .url("http://localhost:8080/register")
                            .content(localNameRef.getText())
                            .build();
                    Gdx.net.sendHttpRequest(httpRequest, responseListener);
                }
            });
            root.add(joinButton);
        }

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

        if (joined) {
            String hostName = null; // to indicate that we are not the host
            application.setScreen(new LobbyScreen(application, hostName, this.nameField.getText()));
        }
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
