package Model;

import java.util.Objects;

/**
 * The Player class is an abstract class representing a player in the game.
 * It contains basic information about the player, such as their ID and name.
 * RougeAgents and Recruitors inherit from this class to define additional
 * behaviors.
 */
public abstract class Player {
    private int id; // The unique identifier for the player
    private String name; // The name of the player
    private boolean isVisible;

    /**
     * Constructor to initialize a player with a unique ID.
     * The player's name is set to a default format "Player X", where X is the ID.
     * 
     * @param id The unique identifier for the player.
     */
    public Player(int id) {
        this.id = id;
        this.name = "Player " + id;
        this.isVisible = true;
    }

    /**
     * Constructor to initialize a player with a unique ID and a specific name.
     * 
     * @param id   The unique identifier for the player.
     * @param name The name of the player.
     */
    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.isVisible = true;
    }

    /**
     * Gets the ID of the player.
     * 
     * @return The player's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the name for the player.
     * 
     * @param name The new name to set for the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the player.
     * 
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the visibility of the player
     * 
     * @param visibility true if visible, false if not
     */
    public void setVisibility(boolean visibility) {
        this.isVisible = visibility;
    }

    /**
     * Gets the visibilityvalue of a step
     * 
     * @return the visibility of the player
     */
    public boolean getVisibility() {
        return this.isVisible;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Player player = (Player) o;
        return id == player.id && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
