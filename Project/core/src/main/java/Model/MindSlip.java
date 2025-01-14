package Model;

/**
 * The MindSlip class represents a specific type of Token in the game.
 * It overrides the toString method to provide a string representation
 * of the MindSlip object.
 * 
 * TODO: This is not used in the current version but could be useful in a future
 * version when it might be possible to pick up mindlip tokes from the board
 */

public class MindSlip extends Token {

    @Override
    public String toString() {
        return "Mindslip{}";
    }
}
