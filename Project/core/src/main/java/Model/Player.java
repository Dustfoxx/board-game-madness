package Model;

public abstract class Player {
    private int id;
    private String name;

    public Player(int id) {
        this.id = id;
        this.name = "Player " + id;
    }

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
