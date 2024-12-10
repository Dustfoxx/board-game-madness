package View.buildingBlocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MindMGMTStage extends Stage {
    private Image background;
    public MindMGMTStage(Viewport viewport, AssetManager manager) {
        super(viewport);
        background = new Image(manager.get("watercolor-sunset.png", Texture.class));
        background.setPosition(0, 0);
        background.setSize(viewport.getCamera().viewportWidth, viewport.getCamera().viewportHeight);
        this.addActor(background);
    }

    @Override
    public void draw() {
        super.draw();
        background.setSize(getViewport().getCamera().viewportWidth, getViewport().getCamera().viewportHeight);
    }
}
