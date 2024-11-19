package Model;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Player> players;
    private List<Token> tokens;
    private List<Feature> features;

    public Cell(List<Feature> features) {
        this.players = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.features = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public void removeToken(Token token) {
        tokens.remove(token);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
