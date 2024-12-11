package View.screen.GameScreenComponents;

import Controller.GameController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class RevealButton extends TextButton {
    public RevealButton(GameController gameController, Skin skin) {
        super("Reveal", skin);
        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //gameController.reveal();
            }
        });
    }

}
