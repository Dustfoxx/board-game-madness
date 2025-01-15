package View.buildingBlocks;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Controller.GameController;
import Model.AbstractCell;
import Model.BrainFact;
import Model.BrainNote;
import Model.Feature;
import Model.Footstep;
import Model.MutableBoolean;
import Model.NormalCell;
import Model.Player;
import Model.RougeAgent;
import Model.Step;
import Model.Token;
import io.github.MindMGMT.MindMGMT;

public class VisualCell extends Actor {
    private TextureRegion feature1;
    private TextureRegion feature2;
    private final TextureRegion temple;
    private final TextureRegion footstep;
    private final TextureRegion[] brains;
    private final BitmapFont step;
    private final List<TextureRegion> players;
    private final List<TextureRegion> tokens;
    private final TextureRegionDrawable highlightdrb;
    private final AbstractCell cellInfo;
    private Dictionary<Feature, Integer> features;
    private String stepText;
    private GameController gameC;

    private final Texture featuresImg;
    private final Texture playersImg;
    private final MutableBoolean highlighted;

    /**
     * Creates a single cell on the board. Initializes textures based on the
     * information found for the cell
     *
     * @param cellInfo a single cell on the board. contains players and more
     */
    public VisualCell(AbstractCell cellInfo, MutableBoolean mutableBoolean, MindMGMT application,
            GameController gameController) {
        initDict();

        Texture highlight = application.assets.get("highlight.png", Texture.class);
        this.highlightdrb = new TextureRegionDrawable(highlight);
        this.featuresImg = application.assets.get("feature_img.png", Texture.class);
        Texture tokensImg = application.assets.get("tokens_temple.png", Texture.class);
        this.playersImg = application.assets.get("players_tmp.png", Texture.class);
        Texture stepImg = application.assets.get("tokens_3d.png", Texture.class);
        this.gameC = gameController;

        this.feature1 = fetchFeature(null);
        this.feature2 = fetchFeature(null);

        this.cellInfo = cellInfo;
        this.highlighted = mutableBoolean;
        if (cellInfo instanceof NormalCell) {
            NormalCell convertedCell = (NormalCell) cellInfo;
            Feature[] features = convertedCell.getFeatures();
            feature1 = fetchFeature(features[0]);
            feature2 = fetchFeature(features[1]);
        }

        // These are currently magic numbers and pretty ugly. Find better way of doing
        // this
        this.temple = new TextureRegion(tokensImg, 0, 0, 250, 250);
        this.footstep = new TextureRegion(stepImg, 170, 360, 70, 70);
        this.brains = new TextureRegion[2];
        this.brains[0] = new TextureRegion(tokensImg, 0, 250, 250, 250);
        this.brains[1] = new TextureRegion(tokensImg, 250, 250, 250, 250);
        this.step = new BitmapFont();
        this.players = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.stepText = "";
        updatePlayers();
        // Bounds needed to render at all. These should be updated based on parent if
        // possible
        setBounds(0, 0, 100, 100);
    }

    /**
     * Draw function. Updates information and redraws based on the board
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        if (highlighted.getBoolean()
                && (gameC.getGame().getCurrentPlayer() instanceof RougeAgent || gameC.getUserIsRecruiter())) {
            highlightdrb.draw(batch, getX(), getY(), getWidth(), getHeight()); // Draw the background
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
        drawNumbers(batch);
    }

    /**
     * Draws the features for a cell on the current batch
     *
     * @param batch the batch currently being composed
     */
    private void drawFeatures(Batch batch) {
        float featureSize = getWidth() / 2;

        float feature1XPos = getX();
        float feature1YPos = getY() + getHeight() - featureSize;

        float feature2XPos = getX() + getWidth() - featureSize;
        float feature2YPos = getY();

        batch.draw(feature1, feature1XPos, feature1YPos, featureSize, featureSize);
        batch.draw(feature2, feature2XPos, feature2YPos, featureSize, featureSize);
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
     * Draws numbers representing the recruiters steps on the current cell
     *
     * @param batch batch being composed
     */
    void drawNumbers(Batch batch) {
        this.step.getData().setScale(getScaleX() * 3);
        this.step.draw(batch, this.stepText, getX(), getY() + getHeight() / 2);
    }

    /**
     * Updates the textures to be drawn from the current players in the cell
     */
    private void updatePlayers() {
        players.clear();
        for (Player player : cellInfo.getPlayers()) {
            if (player.getVisibility()) {
                players.add(fetchPlayer(player.getId()));
            }
        }
    }

    /**
     * Updates the textures for tokens to be drawn based on the tokens in the cell
     */
    private void updateTokens() {
        tokens.clear();
        stepText = "";
        for (Token token : cellInfo.getTokens()) {
            if (token.getVisibility()) {
                if (token instanceof Footstep) {
                    tokens.add(footstep);
                } else if (token instanceof BrainNote) {
                    tokens.add(brains[0]);
                } else if (token instanceof BrainFact) {
                    tokens.add(brains[1]);
                } else {
                    this.stepText = String.valueOf(((Step) token).timestamp);
                }
            }
        }
    }

    /**
     * Initializes the feature dictionary by loading feature mappings.
     * This dictionary maps each {@link Feature} to its corresponding index in the
     * texture.
     * It uses the {@link FeatureUtil#initializeFeatureDict()} method to populate
     * the mapping.
     */
    private void initDict() {
        this.features = FeatureUtil.initializeFeatureDict();
    }

    /**
     * fetches a feature as a textureregion from the main file. Currently using
     * magic numbers to separate them. Should be replaced by atlas
     *
     * @param feature The {@link Feature} to be fetched from the texture.
     * @return A {@link TextureRegion} containing the image of the specified
     *         feature.
     * @throws IllegalArgumentException If the texture file is missing or the
     *                                  feature is invalid.
     */
    public TextureRegion fetchFeature(Feature feature) {
        return FeatureUtil.fetchFeature(featuresImg, features, feature);
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
        this.highlighted.setBoolean(highlight);
    }
}
