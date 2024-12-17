package View.screen.CommonComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ErrorWindow extends Window {
    private final Label errorLabel;

    public String message;

    public ErrorWindow(String title, Skin skin) {
        super(title, skin);
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);
        this.message = "No error";

        this.errorLabel = new Label(message, skin);
        this.add(errorLabel);
        this.row();

        TextButton okButton = new TextButton("OK", skin);
        okButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });
        this.add(okButton).padTop(30);
        this.setPosition(
            Gdx.graphics.getWidth() / 2f - this.getPrefWidth() / 2,
            Gdx.graphics.getHeight() / 2f - this.getPrefHeight() / 2
        );
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) { this.message = message; }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.errorLabel.setText(this.message);
        super.draw(batch, parentAlpha);
    }
}
