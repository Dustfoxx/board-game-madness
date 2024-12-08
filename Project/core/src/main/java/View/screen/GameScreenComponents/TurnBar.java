package View.screen.GameScreenComponents;

import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import java.util.HashSet;
import Controller.GameController;

public class TurnBar extends Table{
    private final GameController gameController;
    private String timeValue;
    private final Label timeTracker;
    private final Table pastTurn;
    private final Set<Table> revealedTurns ;
    private final Set<Integer> revealedTurnsInt;
    private final Skin skin;

    public TurnBar(GameController gameController){
        this.setDebug(true);
        this.gameController = gameController;
        skin= new Skin(Gdx.files.internal("comicui/comic-ui.json"));

        Table turnClock = new Table();
        this.add(turnClock).expandX().fillX().top().row();
        this.timeValue=String.valueOf(gameController.getGame().getCurrentTime());
        //not sure if this is necessary in our game,
        //cause it seems the game turn is not gonna more than 9 now.
        if (timeValue.length()==1){
            timeValue="0"+timeValue;
        }
        timeTracker = new Label(timeValue+": 00", skin,"half-tone");
        timeTracker.setAlignment(Align.center);
        turnClock.add(timeTracker).expandX().fillX().pad(10);

        pastTurn = new Table();
        this.add(pastTurn).expand().fill().top();
        revealedTurns =new HashSet<>();
        revealedTurnsInt=new HashSet<>();
    }
    public void updateTurnbar(){
        String updatedTime=String.valueOf(gameController.getGame().getCurrentTime());
        if (updatedTime.length() == 1) {
            updatedTime = "0" + updatedTime;
        }
        timeTracker.setText(updatedTime + ": 00");

        int currentTurn=gameController.getGame().getCurrentTime();
        if (currentTurn > 5) {
            if ((currentTurn - 1) % 2 == 1&&!revealedTurnsInt.contains(currentTurn-1)) {
                revealTurn(currentTurn - 1);
            }
        }
    }
    private void revealTurn(int turn){
        String turnString = (turn > 9) ? String.valueOf(turn) : "0" + turn;
        Table turnRow = new Table();
        turnRow.setDebug(true);
        turnRow.align(Align.center);

        // Recruits/Mindslip
        Label actionLabel = new Label("Mindslip", skin, "big");
        actionLabel.setAlignment(Align.center);
        turnRow.add(actionLabel).expandX().pad(5);

        Label turnLabel = new Label(turnString + ":00", skin, "big");
        turnLabel.setAlignment(Align.center);
        turnRow.add(turnLabel).expandX().pad(5);

        if (!revealedTurns.contains(turnRow)) {
            revealedTurnsInt.add(turn);
            pastTurn.add(turnRow).expandX();
            pastTurn.row();
            revealedTurns.add(turnRow);
        }
    }

}
