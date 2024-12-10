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
import Model.BrainNote;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Player;
import Model.Token;

public class VisualCell extends Actor {
    private TextureRegion feature1 = fetchFeature(null);
    private TextureRegion feature2 = fetchFeature(null);
    private TextureRegion temple;
    private TextureRegion footstep;
    private TextureRegion[] brains;
    private List<TextureRegion> players;
    private List<TextureRegion> tokens;

    private AbstractCell cellInfo;

    private Dictionary<Feature, Integer> features = new Hashtable<>();

    private Texture featuresImg = new Texture("feature_img.png");
    private Texture tokensImg = new Texture("tokens_temple.png");
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

        this.temple = new TextureRegion(tokensImg, 0, 0, 250, 250);
        this.footstep = new TextureRegion(tokensImg, 250, 0, 250, 250);
        this.brains = new TextureRegion[2];
        this.brains[0] = new TextureRegion(tokensImg, 0, 250, 250, 250);
        this.brains[1] = new TextureRegion(tokensImg, 250, 250, 250, 250);
        this.players = new ArrayList<TextureRegion>();
        this.tokens = new ArrayList<TextureRegion>();
        updatePlayers();
        setBounds(0, 0, 100, 100);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (cellInfo instanceof NormalCell) {
            drawFeatures(batch);
        } else {
            batch.draw(temple, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
        drawPlayers(batch);
        drawTokens(batch);
    }

    private void drawFeatures(Batch batch) {
        float featureYPos = getY() + getHeight() / 2;
        float feature2Pos = getX() + getWidth() / 2;

        batch.draw(feature1, getX(), featureYPos, getOriginX(), getOriginY(),
                getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
        batch.draw(feature2, feature2Pos, featureYPos, getOriginX(), getOriginY(),
                getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
    }

    private void drawTokens(Batch batch) {
        updateTokens();

        int xVal = 0;
        int size = tokens.size();

        for (TextureRegion token : tokens) {
            float xPos = getX() + getWidth() / size * xVal;
            batch.draw(token, xPos, getY() + getHeight() / 2, getOriginX(), getOriginY(),
                    getWidth() / size, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
            xVal++;
        }
    }

    private void drawPlayers(Batch batch) {
        updatePlayers();

        int xVal = 0;

        for (TextureRegion player : players) {
            float xPos = getX() + getWidth() / 4 * xVal;
            batch.draw(player, xPos, getY(), getOriginX(), getOriginY(),
                    getWidth() / 4, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
            xVal++;
        }
    }

    private void updatePlayers() {
        players.clear();
        for (Player player : cellInfo.getPlayers()) {
            players.add(fetchPlayer(player.getId()));
        }
    }

    private void updateTokens() {
        tokens.clear();
        for (Token token : cellInfo.getTokens()) {
            if (token instanceof Footstep) {
                players.add(footstep);
            } else if (token instanceof BrainNote) {
                players.add(brains[0]);
            } else {
                players.add(brains[1]);
            }

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
