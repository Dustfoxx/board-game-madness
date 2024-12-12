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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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


    Texture highlight = new Texture("highlight.png");
    TextureRegionDrawable highlightdrb = new TextureRegionDrawable(new TextureRegion(highlight));

    private AbstractCell cellInfo;

    private Dictionary<Feature, Integer> features = new Hashtable<>();

    private Texture featuresImg = new Texture("feature_img.png");
    private Texture tokensImg = new Texture("tokens_temple.png");
    private Texture playersImg = new Texture("players_tmp.png");
    private boolean highlighted = false;

    /**
     * Creates a single cell on the board. Initializes textures based on the
     * information found for the cell
     * 
     * @param cellInfo a single cell on the board. contains players and more
     */
    public VisualCell(AbstractCell cellInfo) {
        initDict();
        this.cellInfo = cellInfo;
        if (cellInfo instanceof NormalCell) {
            NormalCell convertedCell = (NormalCell) cellInfo;
            Feature[] features = convertedCell.getFeatures();
            feature1 = fetchFeature(features[0]);
            feature2 = fetchFeature(features[1]);
        }

        // These are currently magic numbers and pretty ugly. Find better way of doing
        // this
        this.temple = new TextureRegion(tokensImg, 0, 0, 250, 250);
        this.footstep = new TextureRegion(tokensImg, 250, 0, 250, 250);
        this.brains = new TextureRegion[2];
        this.brains[0] = new TextureRegion(tokensImg, 0, 250, 250, 250);
        this.brains[1] = new TextureRegion(tokensImg, 250, 250, 250, 250);
        this.players = new ArrayList<TextureRegion>();
        this.tokens = new ArrayList<TextureRegion>();
        updatePlayers();
        // Bounds needed to render at all. These should be updated based on parent if
        // possible
        setBounds(0, 0, 100, 100);
        this.setTouchable(Touchable.disabled);
    }

    /**
     * Draw function. Updates information and redraws based on the board
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();

        if (highlighted) {
            this.setTouchable(Touchable.enabled);
            highlightdrb.draw(batch, getX(), getY(), getWidth(), getHeight());  // Draw the background
        }
        else{
            this.setTouchable(Touchable.disabled);
        }

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

    /**
     * Draws the features for a cell on the current batch
     * 
     * @param batch the batch currently being composed
     */
    private void drawFeatures(Batch batch) {
        float featureYPos = getY() + getHeight() / 2;
        float feature2Pos = getX() + getWidth() / 2;

        batch.draw(feature1, getX(), featureYPos, getOriginX(), getOriginY(),
                getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
        batch.draw(feature2, feature2Pos, featureYPos, getOriginX(), getOriginY(),
                getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());
    }

    /**
     * Draws the tokens for the current cell. Values are based on how many tokens
     * are in the current cell
     * 
     * @param batch batch being composed
     */
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

    /**
     * Draws players inhabiting the current cell
     * 
     * @param batch batch being composed
     */
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

    /**
     * Updates the textures to be drawn from the current players in the cell
     */
    private void updatePlayers() {
        players.clear();
        for (Player player : cellInfo.getPlayers()) {
            players.add(fetchPlayer(player.getId()));
        }
    }

    /**
     * Updates the textures for tokens to be drawn based on the tokens in the cell
     */
    private void updateTokens() {
        tokens.clear();
        for (Token token : cellInfo.getTokens()) {
            if (token instanceof Footstep) {
                tokens.add(footstep);
            } else if (token instanceof BrainNote) {
                tokens.add(brains[0]);
            } else {
                tokens.add(brains[1]);
            }

        }
    }

    /**
     * Dictionary to fetch the proper feature from the texture based on feature name
     */
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

    /**
     * fetches a feature as a textureregion from the main file. Currently using
     * magic numbers to separate them. Should be replaced by atlas
     * 
     * @param feature Feature to be fetched
     * @return a textureregion containing the feature
     */
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

    /**
     * Fetches a player from the texturefile containing players. Uses magic numbers
     * based on pixelsize.
     * 
     * @param playerNr which player to fetch
     * @return a textureregion containing the corresponding player
     */
    private TextureRegion fetchPlayer(int playerNr) {
        int sideSize = 400 / 4;
        return new TextureRegion(playersImg,
                sideSize * (playerNr - 1),
                0,
                sideSize, sideSize);
    }



    void highlightCell(boolean highlight) {
        if (highlight) {
            this.highlighted = true;
        } else {
            this.highlighted = false;
        }
    }
}
