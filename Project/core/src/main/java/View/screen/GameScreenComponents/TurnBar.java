package View.screen.GameScreenComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import Controller.GameController;


public class TurnBar extends Table{
    private final GameController gameController;
    private final String timeValue;
    private final Label timeTracker;

    public TurnBar(GameController gameController){
        this.setDebug(true);
        this.gameController = gameController;
        Skin skin= new Skin(Gdx.files.internal("comicui/comic-ui.json"));

        this.timeValue=String.valueOf(gameController.getGame().getCurrentTime());
        timeTracker = new Label("0"+timeValue+": 00", skin,"half-tone");
        timeTracker.setAlignment(Align.center);

        this.add(timeTracker).expandX().fillX().pad(10);
    }
    public void updateTurnbar(){
        String updatedTime=String.valueOf(gameController.getGame().getCurrentTime());
        timeTracker.setText("0"+updatedTime+": 00");
    }

}
