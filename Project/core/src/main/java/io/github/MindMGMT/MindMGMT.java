package io.github.MindMGMT;

import Model.Csv;
import View.loader.CsvLoader;
import View.screen.MainMenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MindMGMT extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private FitViewport viewport;
    private ProgressBar progressBar;
    private boolean hasLoaded = false;

    public Texture backgroundTexture;
    public Skin skin;
    public int nrOfPlayers;
    public AssetManager assets;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new FitViewport(800,450);
        assets = new AssetManager();
        loadAssets();
        progressBar = new ProgressBar(0, 1, 0.1f, false, new ProgressBar.ProgressBarStyle());
        progressBar.setX(viewport.getWorldWidth()/2);
        progressBar.setY(viewport.getWorldHeight()/2);
        progressBar.setWidth(viewport.getWorldWidth());
        progressBar.setHeight(viewport.getWorldHeight());
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
        super.render(); //important!

        if (hasLoaded) {
            return; // Only clear screen once all is loaded
        }

        if (assets.update()) {
            // We are done loading
            this.hasLoaded = true;
            this.skin = assets.get("comicui/comic-ui.json", Skin.class);
            this.backgroundTexture = assets.get("watercolor-sunset.png", Texture.class);
            this.setScreen(new MainMenuScreen(this));

        } else {
            batch.begin();
            progressBar.setValue(assets.getProgress());
            progressBar.draw(batch, 1f);
            batch.end();
        }
    }

    public void drawBackground() {
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight()
            );
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
