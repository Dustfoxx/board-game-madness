package View.buildingBlocks;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import Model.AbstractCell;
import Model.Feature;
import Model.NormalCell;
import Model.Player;

public class VisualCell extends Actor {
    private TextureRegion feature1 = fetchFeature(null);
    private TextureRegion feature2 = fetchFeature(null);
    private List<TextureRegion> players;

    private AbstractCell cellInfo;

    private Dictionary<Feature, Integer> features = new Hashtable<>();

    private Texture featuresImg = new Texture("feature_img.png");
    private Texture playersImg = new Texture("players_tmp.png");

    public VisualCell(AbstractCell cellInfo) {
        initDict();
        this.cellInfo = cellInfo;
        if (cellInfo instanceof NormalCell) {
            NormalCell convertedCell = (NormalCell) cellInfo;
            Feature[] features = convertedCell.getFeatures();
            feature1 = fetchFeature(features[0]);
            feature2 = fetchFeature(features[1]);
        }
        players = new ArrayList<TextureRegion>();
        updatePlayers();
        setBounds(0, 0, 100, 100);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        float featureYPos = getY() + getHeight() / 2;
        float feature2Pos = getX() + getWidth() / 2;

        batch.draw(feature1, getX(), featureYPos, getOriginX(), getOriginY(),
                getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
        batch.draw(feature2, feature2Pos, featureYPos, getOriginX(), getOriginY(),
                getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());

        updatePlayers();

        int xVal = 0;

        for (TextureRegion player : players) {
            float xPos = getX() + getWidth() / 4 * xVal;
            batch.draw(player, xPos, getY(), getOriginX(), getOriginY(),
                    getWidth() / 4, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
        }
    }

    private void updatePlayers() {
        for (Player player : cellInfo.getPlayers()) {
            players.add(fetchPlayer(player.getId()));
        }
    }

    private void initDict() {
        features.put(Feature.BOOKSTORE, 0);
        features.put(Feature.BUS, 1);
        features.put(Feature.BILLBOARD, 2);
        features.put(Feature.PARROT, 3);
        features.put(Feature.FOUNTAIN, 4);
        features.put(Feature.TREE, 5);
        features.put(Feature.POOL, 6);
        features.put(Feature.UMBRELLA, 7);
        features.put(Feature.IDOL, 8);
        features.put(Feature.TORCHES, 9);
        features.put(Feature.GARDEN, 10);
        features.put(Feature.COFFEE, 11);
        features.put(Feature.MONKS, 12);
        features.put(Feature.DOGS, 13);
        features.put(Feature.COURIER, 14);
        features.put(Feature.GRAFFITI, 15);
    }

    public TextureRegion fetchFeature(Feature feature) {
        int sideSize = 873 / 4;
        if (feature != null) {
            return new TextureRegion(featuresImg,
                    sideSize * (features.get(feature) % 4),
                    sideSize * (int) (Math.floor(features.get(feature) / 4)),
                    sideSize, sideSize);
        }
        return new TextureRegion(new Texture("feature_img.png"),
                0,
                0,
                sideSize, sideSize);
    }

    private TextureRegion fetchPlayer(int playerNr) {
        int sideSize = 400 / 4;
        return new TextureRegion(playersImg,
                sideSize * (playerNr - 1),
                0,
                sideSize, sideSize);
    }
}
