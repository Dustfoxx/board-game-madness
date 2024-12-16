package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Recruiter class represents a player collecting recruits.
 * The Recruiter tracks the number of unreviealed recruits, the path they have
 * walked, and their features of interest.
 */
public class Recruiter extends Player {

    public enum RecruiterType {
        HORIZONTAL,
        DIAGONAL,
        USED
    }

    private int amountRecruited; // The number of new unrevealed recruits
    private RecruiterType chosenRecruiter;
    private List<int[]> walkedPath; // A list of coordinates of the cells the recruiter has walked through ordered
                                    // from first step to last step.
    private Feature[] featuresOfInterest; // An array of the 3 features that the recruiter is interested in

    /**
     * Constructor to initialize a Recruiter with a unique ID, name, and features of
     * interest.
     * 
     * @param id                 The unique identifier for the recruiter.
     * @param name               The name of the recruiter.
     * @param featuresOfInterest The array of the 3 features that the recruiter is
     *                           interested in.
     */
    public Recruiter(int id, String name, Feature[] featuresOfInterest) {
        super(id, name); // Call the parent constructor to set ID and name
        if (featuresOfInterest.length == 3) {
            this.amountRecruited = 0; // Initialize the number of unrevealed recruits to 0
            this.walkedPath = new ArrayList<>(); // Initialize an empty list for the walked path
            this.featuresOfInterest = featuresOfInterest; // Set the features of interest
        } else {
            throw new IllegalArgumentException(
                    "The featuresOfInterest array is not of size 3 but of size " + featuresOfInterest.length);
        }
    }

    /**
     * Gets the total amount of unrevealed recruites by the recruiter.
     * 
     * @return The number of unrevealed recruits.
     */
    public int getAmountRecruited() {
        return amountRecruited;
    }

    /**
     * Adds a specified number of new recruits to the total.
     * 
     * @param newAmountRecruited The number of new recruits to add.
     */
    public void addAmountRecruited(int newAmountRecruited) {
        this.amountRecruited += newAmountRecruited;
    }

    /**
     * Resets the amount of unrevealed recruits to 0.
     */
    public void resetAmountRecruited() {
        this.amountRecruited = 0;
    }

    /**
     * Gets the list of coordinates representing the cells the recruiter has walked
     * through.
     * The path is ordered from the first step to the last step.
     * 
     * @return The list of coordinates (row, col) in the walked path.
     */
    public List<int[]> getWalkedPath() {
        return walkedPath;
    }

    /**
     * Adds the coordinates of the new step to the walked path.
     * If the cell has already been visited, an exception is thrown.
     * 
     * @param row The row coordinate of the new step.
     * @param col The column coordinate of the new step.
     * @throws IllegalArgumentException If the cell has already been visited.
     */
    public void addToWalkedPath(int row, int col) {
        int[] newStep = new int[] { row, col };
        // Check if the cell has already been visited
        for (int[] oldStep : walkedPath) {
            if (oldStep[0] == newStep[0] && oldStep[1] == newStep[1]) {
                throw new IllegalArgumentException(
                        "The new step (" + row + ", " + col + ") already exists in the walked path.");
            }
        }
        walkedPath.add(newStep); // Add the new step to the walked path
    }

    /**
     * Gets the array of features the recruiter is interested in.
     * 
     * @return The array of features of interest.
     */
    public Feature[] getFeaturesOfInterest() {
        return featuresOfInterest;
    }

    /**
     * Sets the chosen recruiter for this game
     * 
     * @param newType the new recruitertype. Set used to disable MINDSLIP
     */
    public void setRecruiterType(RecruiterType newType) {
        this.chosenRecruiter = newType;
    }

    /**
     * Gets the recruitertype of this recruiter
     * 
     * @return the chosenRecruiter of this instance
     */
    public RecruiterType getRecruiterType() {
        return this.chosenRecruiter;
    }
}
