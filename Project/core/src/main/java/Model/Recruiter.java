package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Recruiter class represents a player collecting recruits. 
 * The Recruiter tracks the number of unreviealed recruits, the path they have walked, and their features of interest.
 */
public class Recruiter extends Player {

    private int amountRecruited;        // The number of new unrevealed recruits 
    private List<int[]> walkedPath;     // A list of coordinates of the cells the recruiter has walked through ordered from first step to last step. 
    private List<Feature> featuresOfInterest;  // A list of features that the recruiter is interested in

    /**
     * Constructor to initialize a Recruiter with a unique ID, name, and features of interest.
     * 
     * @param id The unique identifier for the recruiter.
     * @param name The name of the recruiter.
     * @param featuresOfInterest The list of features that the recruiter is interested in.
     */
    public Recruiter(int id, String name, List<Feature> featuresOfInterest) {
        super(id, name);  // Call the parent constructor to set ID and name
        this.amountRecruited = 0; // Initialize the number of unrevealed recruits to 0
        this.walkedPath = new ArrayList<>(); // Initialize an empty list for the walked path
        this.featuresOfInterest = featuresOfInterest;  // Set the features of interest
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
     * Gets the list of coordinates representing the cells the recruiter has walked through.
     * The path is ordered from the first step to the last step. 
     * 
     * @return The list of coordinates (row, col) in the walked path.
     */
    public List<int[]> getWalkedPath() {
        return walkedPath;
    }

    /**
     * Adds a new cell coordinates to the walked path in the end of the list.
     * If the cell has already been visited, an exception is thrown.
     * 
     * @param row The row coordinate of the cell.
     * @param col The column coordinate of the cell.
     * @throws IllegalArgumentException If the cell has already been visited.
     */
    public void addToWalkedPath(int row, int col) {
        int[] newCell = new int[] { row, col };
        // Check if the cell has already been visited
        for (int[] cell : walkedPath) {
            if (cell[0] == newCell[0] && cell[1] == newCell[1]) {
                throw new IllegalArgumentException(
                        "The cell (" + row + ", " + col + ") already exists in the walked path.");
            }
        }
        walkedPath.add(newCell);  // Add the new cell to the walked path
    }

    /**
     * Gets the list of features the recruiter is interested in.
     * 
     * @return The list of features of interest.
     */
    public List<Feature> getFeaturesOfInterest() {
        return featuresOfInterest;
    }
}
