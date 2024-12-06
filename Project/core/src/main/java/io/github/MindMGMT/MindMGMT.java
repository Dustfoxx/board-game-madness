package io.github.MindMGMT;

import Model.Csv;
import View.loader.CsvLoader;
import View.screen.MainMenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MindMGMT extends Game {
    private Stage stage;
    private ProgressBar progressBar;
    private boolean hasLoaded = false;
    private ScreenViewport viewport;

    public Texture backgroundTexture;
    public Image backgroundImage;
    public Skin skin;
    public int nrOfPlayers;
    public AssetManager assets;

    @Override
    public void create() {
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        assets = new AssetManager();
        backgroundImage = new Image();
        loadAssets();
        progressBar = new ProgressBar(0, 1, 0.1f, false, new ProgressBar.ProgressBarStyle());
        progressBar.setX(viewport.getWorldWidth()/2);
        progressBar.setY(viewport.getWorldHeight()/2);
        progressBar.setWidth(viewport.getWorldWidth());
        progressBar.setHeight(viewport.getWorldHeight());
        stage.addActor(progressBar);
    }

    private void loadAssets() {
        assets.setLoader(Csv.class, new CsvLoader(new InternalFileHandleResolver()));
        assets.load("board-data.csv", Csv.class);
        assets.load("basic-ui.atlas", TextureAtlas.class);
        assets.load("metalui/metal-ui.json", Skin.class);
        assets.load("comicui/comic-ui.json", Skin.class);
        assets.load("watercolor-sunset.png", Texture.class);
        assets.load("basic-board.png", Texture.class);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        if (hasLoaded) {
            stage.act();
            stage.draw();
            super.render();
            return; // Only clear screen once all is loaded
        } else {
            super.render();
        }

        if (assets.update()) {
            // We are done loading
            backgroundImage = new Image(assets.get("watercolor-sunset.png", Texture.class));
            backgroundImage.setSize(stage.getWidth(), stage.getHeight());
            stage.clear();
            stage.addActor(backgroundImage);
            stage.act();
            stage.draw();

            this.hasLoaded = true;
            this.skin = assets.get("comicui/comic-ui.json", Skin.class);
            this.skin.getFont("button")
                .getData()
                .setScale(0.8f);
            this.setScreen(new MainMenuScreen(this));

        } else {
            progressBar.setValue(assets.getProgress());
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
