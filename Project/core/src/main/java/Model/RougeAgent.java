package Model;

/**
 * The RougeAgent class represents a player trying to find the recruiter.
 */
public class RougeAgent extends Player {

    /**
     * Constructor to initialize a RougeAgent with a unique ID.
     * 
     * @param id The unique identifier for the rogue agent.
     */
    public RougeAgent(int id) {
        super(id); // Call the parent constructor to set the ID and name
    }

    /**
     * Constructor to initialize a RougeAgent with a unique ID and name.
     * 
     * @param id   The unique identifier for the rogue agent.
     * @param name The name of the rogue agent.
     */
    public RougeAgent(int id, String name) {
        super(id, name); // Call the parent constructor to set the ID and name
    }

    public boolean isRecruiter() {
        return false;
    }
}
