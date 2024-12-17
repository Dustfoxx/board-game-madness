package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Game.gameStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SerializatioTests {

    private Game game;
    private Feature[] featuresOfInterest;
    private Player player1;
    private Player player2;
    private Board board;
    private int rows;
    private int columns;
    private AbstractCell[][] cells;

    @BeforeEach
    void setUp() {
        // Mocking Player and Board objects
        featuresOfInterest = new Feature[3];
        player1 = new Recruiter(0, null, featuresOfInterest);
        player2 = new RougeAgent(1, null);
        rows = 3;
        columns = 3;
        cells = new AbstractCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Feature[] features = new Feature[2];
                features[0] = Feature.BILLBOARD;
                features[1] = Feature.BOOKSTORE;
                cells[row][column] = new NormalCell(features);
            }
        }
        board = new Board(cells);
        List<Player> players = Arrays.asList(player1, player2);

        // Initializing the Game object
        game = new Game(players, new ArrayList<User>(), board, player1);
        game.setGameState(gameStates.ONGOING);
    }

    @Test
    void testGameConstructorValidation() {

        Gson gson = new GsonBuilder()
        .registerTypeAdapter(Player.class, new GeneralAdapter<>(new Gson()))
        .registerTypeAdapter(Token.class, new GeneralAdapter<>(new Gson()))
        .registerTypeAdapter(AbstractCell.class, new GeneralAdapter<>(new Gson()))
        .create();


        // Serialize Java object to JSON
        String jsonGame = gson.toJson(game);
        System.out.println("Serialized JSON: " + jsonGame);

        // Deserialize JSON to Java object
        Game newGame = gson.fromJson(jsonGame, Game.class);
        System.out.println("Deserialized Object: " + newGame);

        assertTrue(game.equals(newGame));
    }
}