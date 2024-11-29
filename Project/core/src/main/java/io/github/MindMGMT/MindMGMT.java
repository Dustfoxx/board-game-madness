package io.github.MindMGMT;

import View.screen.MainMenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
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
    public int nrOfPayers;
    public AssetManager assets;
    private ProgressBar progressBar;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new FitViewport(640,480);
        assets = new AssetManager();
        loadAssets();
        progressBar = new ProgressBar(0, 1, 0.1f, false, new ProgressBar.ProgressBarStyle());
        progressBar.setX(viewport.getWorldWidth()/2);
        progressBar.setY(viewport.getWorldHeight()/2);
        progressBar.setWidth(viewport.getWorldWidth());
        progressBar.setHeight(viewport.getWorldHeight());
    }

    private void loadAssets() {
        assets.load("basic-ui.atlas", TextureAtlas.class);
        assets.load("metalui/metal-ui.json", Skin.class);
    }

    @Override
    public void render() {
        super.render(); //important!
        if (assets.update()) {
            // We are done loading
           this.setScreen(new MainMenuScreen(this));

        } else {
            ScreenUtils.clear(0, 0, 0, 1);
            batch.begin();
            progressBar.setValue(assets.getProgress());
            progressBar.draw(batch, 1f);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
