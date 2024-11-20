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

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void addToken(Token newToken) {
        for (Token token : tokens) {
            if (token.getClass().equals(newToken.getClass())) {
                throw new IllegalArgumentException("There is already a " + newToken.getClass() + " in this cell");
            } else {
                tokens.add(token);
            }
        }
    }

    public void removeToken(Token token) {
        tokens.remove(token);
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
