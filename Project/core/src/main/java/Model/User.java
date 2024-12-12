package Model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private List<Integer> playerPieces; // List of player controlled pieces
    private int userId; // Unique id from the mulitplayer part
    private String userName; // Name chosen by the player

    public User(int userId, String userName) {
        this.playerPieces = new ArrayList<Integer>();
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void addPlayerPiece(Player player) {
        this.playerPieces.add(player.getId());
    }

    public boolean ownsPlayerPiece(Player player) {
        return playerPieces.contains(player.getId());
    }

}
