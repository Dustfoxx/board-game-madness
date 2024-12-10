package View.screen.GameScreenComponents;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Controller.ActionController.revealResult;

public class RevealWindow extends Dialog {

    public RevealWindow(revealResult result, Skin skin) {
        super(getTitle(result), skin);
        text(getMessage(result));
        button("OK", true);
    }


    private static String getTitle(revealResult result) {
        switch (result) {
            case Success:
                return "Success";
            case Temple:
            case NO_FOOTSTEP:
                return "Failure";
            default:
                return "Unknown";
        }
    }

    private static String getMessage(revealResult result) {
        switch (result) {
            case Success:
                return "Reveal successful!";
            case Temple:
                return "This cell is not a NormalCell.";
            case NO_FOOTSTEP:
                return "No footstep found in this cell.";
            default:
                return "An unknown error occurred.";
        }
    }
}
