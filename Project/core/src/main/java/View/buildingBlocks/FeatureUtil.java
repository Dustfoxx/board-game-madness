package View.buildingBlocks;

import java.util.Dictionary;
import java.util.Hashtable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Model.Feature;

/**
 * Utility class for managing and retrieving features in the game.
 * <p>
 * This class provides methods to initialize a feature dictionary
 * and to fetch specific {@link Feature} textures from a larger feature texture file.
 */

public class FeatureUtil {

    public static Dictionary<Feature, Integer> initializeFeatureDict() {
        
        Dictionary<Feature, Integer> features = new Hashtable<>();
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
        return features;
    }

     public static TextureRegion fetchFeature(Texture featuresImg, Dictionary<Feature, Integer> featureDict, Feature feature) {
        int sideSize = 873 / 4;
        if (feature != null && featureDict.get(feature) != null) {
            int index = featureDict.get(feature);
            int x = sideSize * (index % 4);
            int y = sideSize * (index / 4);
            return new TextureRegion(featuresImg, x, y, sideSize, sideSize);
        }
        return new TextureRegion(featuresImg, 0, 0, sideSize, sideSize);
    }

}
