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

    public TurnBar(GameController gameController){
        this.setDebug(true);
        this.gameController = gameController;
        Skin skin= new Skin(Gdx.files.internal("comicui/comic-ui.json"));
        this.timeValue=String.valueOf(gameController.getGame().getCurrentTime());
        Label timeTracker = new Label("0"+timeValue+": 00", skin,"half-tone");
        timeTracker.setAlignment(Align.center);
        this.add(timeTracker).expandX().fillX().pad(10);
    }

}
