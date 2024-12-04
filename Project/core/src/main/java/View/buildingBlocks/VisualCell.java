package View.buildingBlocks;

import java.util.Dictionary;
import java.util.Hashtable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import Model.AbstractCell;
import Model.Feature;
import Model.NormalCell;
import Model.Player;

public class VisualCell {
    private Table cell = new Table();

    private Dictionary<Feature, Integer> features = new Hashtable<>();

    private Texture featuresImg = new Texture("feature_img.png");
    private Texture playersImg = new Texture("players_tmp.png");

    public VisualCell(AbstractCell cellInfo) {
        initDict();
        if (cellInfo instanceof NormalCell) {
            NormalCell convertedCell = (NormalCell) cellInfo;
            Feature[] features = convertedCell.getFeatures();
            cell.add(new Image(fetchFeature(features[0]))).uniform();
            cell.add(new Image(fetchFeature(features[1]))).uniform();
        }
        cell.row();
        for (Player player : cellInfo.getPlayers()) {
            cell.add(new Image(fetchPlayer(player.getId()))).uniform();
        }
    }

    public Table getVisualCell() {
        return this.cell;
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

    private TextureRegion fetchFeature(Feature feature) {
        int sideSize = 873 / 4;
        return new TextureRegion(featuresImg,
                sideSize * (features.get(feature) % 4),
                sideSize * (int) (Math.floor(features.get(feature) / 4)),
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
