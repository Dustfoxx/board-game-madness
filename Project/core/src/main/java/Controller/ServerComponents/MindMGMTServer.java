package Controller.ServerComponents;

import Model.*;
import com.badlogic.gdx.net.HttpStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class MindMGMTServer {
    private HttpServer server;
    private Game gameState;
    private final ArrayList<String> users;
    private final Gson gson;

    public MindMGMTServer(int port, ArrayList<String> users) {
        this.gameState = null;
        this.users = users;
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Player.class, new GeneralAdapter<>())
            .registerTypeAdapter(Token.class, new GeneralAdapter<>())
            .registerTypeAdapter(AbstractCell.class, new GeneralAdapter<>())
            .create();

        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/poll", createPollHandler());
            server.createContext("/register", createRegisterPlayerHandler());
            server.createContext("/update", createUpdateHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpHandler createPollHandler() {
        return httpExchange -> {
            String response = "";
            if (gameState == null) {
                // We are in lobby
                response = users.stream().filter(Objects::nonNull).collect(Collectors.joining(","));
            } else {
                // We have an ongoing game
                response = gson.toJson(gameState);
            }

            httpExchange.sendResponseHeaders(HttpStatus.SC_OK, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        };
    }

    private HttpHandler createRegisterPlayerHandler() {
        return httpExchange -> {
            System.out.println(httpExchange.getRequestURI());
            String response;
            int status = HttpStatus.SC_OK;
            String method = httpExchange.getRequestMethod();
            if (!method.equals("POST")) {
                response = "Error: Method not supported!";
                status = HttpStatus.SC_BAD_REQUEST;
            } else {
                if (users.size() >= 5) {
                    response = "Error: Lobby is full!";
                    status = HttpStatus.SC_BAD_REQUEST;
                } else {
                    InputStream stream = httpExchange.getRequestBody();
                    String body = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)) // TODO: Resource leak: '<unassigned Closeable value>' is never closed
                            .lines()
                            .collect(Collectors.joining("\n"));
                    stream.close();
                    users.add(body);
                    response = "ok";
                }
            }

            httpExchange.sendResponseHeaders(status, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        };
    }

    private HttpHandler createUpdateHandler() {
        return httpExchange -> {
            System.out.println(httpExchange.getRequestURI());
            int status = HttpStatus.SC_OK;
            String response = "";
            if (gameState == null) {
                // We are in lobby
                status = HttpStatus.SC_BAD_REQUEST;
                response = "Cannot update host when in lobby!";
            } else if (!httpExchange.getRequestMethod().equals("POST")) {
                status = HttpStatus.SC_BAD_REQUEST;
                response = "Only POST requests are supported!";
            } else {
                InputStream stream = httpExchange.getRequestBody();
                String body = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
                stream.close();
                response = "ok";
                Game newGameState = gson.fromJson(body, Game.class);
                gameState.updateDeeply(newGameState);
            }

            httpExchange.sendResponseHeaders(status, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        };
    }

    public void setGameState(Game gameState) {
        // This works due to pass by reference!!!
        this.gameState = gameState;
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
