package Model;

import java.util.ArrayList;
import java.util.List;

public class Recruiter extends Player {
    private int amountRecruited; // Unrevield number of newly recruited people
    private List<int[]> walkedPath;
    private List<Feature> featuresOfInterest;

    public Recruiter(int id, String name, List<Feature> featuresOfInterest) {
        super(id, name);
        this.amountRecruited = 0;
        this.walkedPath = new ArrayList<>();
        this.featuresOfInterest = featuresOfInterest;
    }

    public int getAmountRecruited() {
        return amountRecruited;
    }

    public void addAmountRecruited(int newAmountRecruited) {
        this.amountRecruited += newAmountRecruited;
    }

    public void resetAmountRecruited() {
        this.amountRecruited = 0;
    }

    public List<int[]> getWalkedPath() {
        return walkedPath;
    }

    public void addToWalkedPath(int row, int col) {
        int[] newCell = new int[] { row, col };
        for (int[] cell : walkedPath) {
            if (cell[0] == newCell[0] && cell[1] == newCell[1]) {
                throw new IllegalArgumentException(
                        "The cell (" + row + ", " + col + ") already existis in the walked path.");
            }
        }
        walkedPath.add(new int[] { row, col });
    }

    public List<Feature> getFeaturesOfInterest() {
        return featuresOfInterest;
    }

}