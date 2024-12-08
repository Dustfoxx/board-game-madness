package View.screen.GameScreenComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import Controller.GameController;


public class TurnBar extends Table{
    private final GameController gameController;
    private String timeValue;
    private final Label timeTracker;

    public TurnBar(GameController gameController){
        this.setDebug(true);
        this.gameController = gameController;
        Skin skin= new Skin(Gdx.files.internal("comicui/comic-ui.json"));

        Table turnClock = new Table();
        this.add(turnClock).expandX().fillX();
        this.timeValue=String.valueOf(gameController.getGame().getCurrentTime());
        //not sure if this is necessary in our game,
        //cause it seems the game turn is not gonna more than 9 now.
        if (timeValue.length()==1){
            timeValue="0"+timeValue;
        }
        timeTracker = new Label(timeValue+": 00", skin,"half-tone");
        timeTracker.setAlignment(Align.center);
        turnClock.add(timeTracker);

        Table pastTurn= new Table();
        this.add(pastTurn).expandX().fillX();

    }
    public void updateTurnbar(){
        String updatedTime=String.valueOf(gameController.getGame().getCurrentTime());
        timeTracker.setText("0"+updatedTime+": 00");
    }

}
