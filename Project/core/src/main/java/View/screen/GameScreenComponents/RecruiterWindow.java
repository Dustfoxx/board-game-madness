package View.screen.GameScreenComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Controller.GameController;
import Controller.GameController.Actions;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Model.Recruiter;
import Model.Recruiter.RecruiterType;

public class RecruiterWindow extends Window {

    RecruiterType selectedRecruiter;
    RecruiterType[] types = new RecruiterType[] { RecruiterType.HORIZONTAL, RecruiterType.DIAGONAL };
    ImageButton selectedrecruiterButton;
    Table buttonTable;
    Recruiter recruiter;

    public RecruiterWindow(Skin skin, Recruiter recruiter, GameController gameController) {
        super("Ask Recruiter", skin);
        // Create the window
        this.setMovable(false);
        this.setResizable(false);
        this.setModal(true);
        this.buttonTable = new Table(); // Create a table for the recruiter buttons
        this.add(buttonTable).colspan(2).center().row();
        this.recruiter = recruiter;
        this.selectedRecruiter = RecruiterType.HORIZONTAL;

        // Add the message to the window
        Label messageLabel = new Label("Which recruiter do you want to play?", skin);
        this.add(messageLabel).pad(20).row();

        for (RecruiterType type : types) {
            TextureRegion recruiterTexture = fetchRecruiter(type);
            // Create an ImageButton based on the recruiter image
            TextureRegionDrawable drawable = new TextureRegionDrawable(recruiterTexture);
            ImageButton recruiterButton = new ImageButton(drawable, drawable.tint(Color.BROWN));

            // Update the selected recruiter when the button is pressed
            recruiterButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectedrecruiterButton = recruiterButton;
                    selectedRecruiter = type;
                }
            });
            buttonTable.add(recruiterButton).padRight(20).padBottom(5).width(240).height(400);
        }

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.actionHandler(Actions.RECRUITERCHOICE, new Object[] { selectedRecruiter });
                actor.getParent().remove(); // Closes the window
            }
        });
        this.add(confirmButton).colspan(2).padTop(10).center().row();
        this.pack();
        this.setSize(1000, 600);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Reset size for all ImageButtons
        for (Actor actor : this.buttonTable.getChildren()) {
            ((Button) actor).setSize(240, 400);
        }

        // Increase the size of the selected button
        if (selectedrecruiterButton != null) {
            selectedrecruiterButton.setSize(260, 440);
        }

        super.draw(batch, parentAlpha); // Important
    }

    /**
     * Fetches recruiter card image
     * 
     * @param recruiter which recruiter it is
     * @return a textureregion containing the proper recruiter
     */
    private TextureRegion fetchRecruiter(RecruiterType recruiter) {
        int xSize = 677 / 2;
        int ySize = 481;
        switch (recruiter) {
            case HORIZONTAL:
                return new TextureRegion(new Texture("recruiters.png"),
                        0,
                        0,
                        xSize, ySize);
            default:
                return new TextureRegion(new Texture("recruiters.png"),
                        xSize,
                        0,
                        xSize, ySize);
        }
    }

}