package io.github.MindMGMT;

import Model.Csv;
import View.loader.CsvLoader;
import View.screen.MainMenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class MindMGMT extends Game {
    private Stage stage;
    private ProgressBar progressBar;
    private boolean hasLoaded = false;

    public Skin skin;
    public int nrOfPlayers;
    public AssetManager assets;
    boolean moving = false;

    @Override
    public void create() {
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);
        assets = new AssetManager();
        loadAssets();
        progressBar = new ProgressBar(0, 1, 0.1f, false, new ProgressBar.ProgressBarStyle());
        progressBar.setX(viewport.getCamera().viewportWidth / 2);
        progressBar.setY(viewport.getCamera().viewportHeight / 2);
        progressBar.setWidth(viewport.getCamera().viewportWidth / 2);
        progressBar.setHeight(viewport.getCamera().viewportHeight / 2);
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

    public boolean joinGame(String name, String code) {
        return false;
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

            this.hasLoaded = true;
            this.skin = assets.get("comicui/comic-ui.json", Skin.class);
            this.skin.getFont("button")
                    .getData()
                    .setScale(0.8f);

            this.skin.getFont("font")
                    .getData()
                    .setScale(2f);
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
