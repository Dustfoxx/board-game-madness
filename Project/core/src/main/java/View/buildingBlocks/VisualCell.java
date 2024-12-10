    package View.buildingBlocks;

    import java.util.Dictionary;
    import java.util.Hashtable;

    import com.badlogic.gdx.graphics.Colors;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.Pixmap.Format;
    import com.badlogic.gdx.graphics.g2d.NinePatch;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.scenes.scene2d.Actor;
    import com.badlogic.gdx.scenes.scene2d.ui.Image;
    import com.badlogic.gdx.scenes.scene2d.ui.Table;
    import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
    import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
    import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

    import Model.AbstractCell;
    import Model.Feature;
    import Model.NormalCell;
    import Model.Player;

    public class VisualCell extends Table {

        AbstractCell abstractCell;

        private Dictionary<Feature, Integer> features = new Hashtable<>();

        private Texture featuresImg = new Texture("feature_img.png");
        private Texture playersImg = new Texture("players_tmp.png");

        public VisualCell(AbstractCell cellInfo) {
            abstractCell = cellInfo;
            initDict();
            if (cellInfo instanceof NormalCell) {
                NormalCell convertedCell = (NormalCell) cellInfo;
                Feature[] features = convertedCell.getFeatures();
                this.add(new Image(fetchFeature(features[0]))).uniform();
                this.add(new Image(fetchFeature(features[1]))).uniform();

                
            }
            this.row();

            for (Player player : cellInfo.getPlayers()) {
                this.add(new Image(fetchPlayer(player.getId()))).uniform();
            }
        }

        public Table getVisualCell() {
            return this;
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


        public void s() {

            if (abstractCell instanceof NormalCell) {
                NormalCell convertedCell = (NormalCell) abstractCell;
                Feature[] features = convertedCell.getFeatures();
                this.add(new Image(fetchFeature(features[0]))).uniform();
                this.add(new Image(fetchFeature(features[1]))).uniform();
            }
            this.row();

            for (Player player : abstractCell.getPlayers()) {
                this.add(new Image(fetchPlayer(player.getId()))).uniform();
            }
        }

        public void UpdateCell() {
            // Clear existing contents in the table before updating
            clear();
    
            // Rebuild the visuals based on the updated AbstractCell
            updateVisuals();
    
            // Invalidate and relayout the table to ensure correct rendering
            invalidate();
            layout();
        }
    
        // Method to update visuals (called both in constructor and UpdateCell)
        private void updateVisuals() {
            
            if (abstractCell instanceof NormalCell) {
                NormalCell convertedCell = (NormalCell) abstractCell;
                Feature[] features = convertedCell.getFeatures();
    
                // Add features to the cell (ensure to handle them properly)
                this.add(new Image(fetchFeature(features[0]))).uniform();
                this.add(new Image(fetchFeature(features[1]))).uniform();
            }
    
            // Add the player icons to the cell
            this.row(); // Move to next row after features

            for (Player player : abstractCell.getPlayers()) {
                System.out.println(player);
                this.add(new Image(fetchPlayer(player.getId()))).uniform();
            }
        }
        
    }
