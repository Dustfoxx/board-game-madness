package View.screen.GameScreenComponents;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

import java.util.Dictionary;

import View.buildingBlocks.FeatureUtil;
import Controller.GameController;
import Model.Feature;
import Model.Player;
import Model.Recruiter;

public class FeatureSelection extends Table{

    private final GameController gameController;
    private final Dictionary<Feature, Integer> featureDict;
    private final Texture featuresImg;

    public FeatureSelection(GameController gameController, Skin skin) {
        this.gameController = gameController;

        // Initialize feature dictionary and texture
        this.featureDict=FeatureUtil.initializeFeatureDict();
        this.featuresImg = new Texture("feature_img.png");

        Label featureSelectedLabel = new Label("Features Selected", skin, "half-tone");
        featureSelectedLabel.setAlignment(Align.center);
        this.add(featureSelectedLabel).expandX().fillX().pad(10);
        this.row();

        Table featureTable = new Table();
        this.add(featureTable).expandX().top().padTop(10);

        Feature[] recruiterFeatures =gameController.getGame().getRecruiter().getFeaturesOfInterest();

        for (Feature feature : recruiterFeatures) {
            if (feature != null) {
                Label featureLabel = new Label(feature.name(), skin,"half-tone");
                featureLabel.setAlignment(Align.center);
                featureTable.add(featureLabel).expandX().center().pad(5);

                TextureRegion featureRegion = FeatureUtil.fetchFeature(featuresImg, featureDict, feature);
                Image featureImage = new Image(new TextureRegionDrawable(featureRegion));
                featureTable.add(featureImage).expandX().center().pad(5);

                featureTable.row();
            }
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Player currentPlayer = gameController.getGame().getCurrentPlayer();
        if (currentPlayer instanceof Recruiter) {
            // Only renders the table when the current player is the Recruiter
            super.draw(batch, parentAlpha); // Important
        }
    }

}