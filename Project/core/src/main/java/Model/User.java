package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public int getPlayerAmount() {
        return this.playerPieces.size();
    }

    public void addPlayerPiece(Player player) {
        this.playerPieces.add(player.getId());
    }

    public boolean ownsPlayerPiece(Player player) {
        return playerPieces.contains(player.getId());
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", userName='" + userName + "', playerPieces=" + playerPieces + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(playerPieces, user.playerPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, playerPieces);
    }

}
