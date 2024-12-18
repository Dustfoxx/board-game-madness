package Controller.ServerComponents;

import Model.Game;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MindMGMTServer {
    private HttpServer server;
    private Game gameState;
    private final ArrayList<String> users;

    public MindMGMTServer(int port, ArrayList<String> users) {
        this.gameState = null;
        this.users = users;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/poll", createPollHandler());
            server.createContext("/register", createRegisterPlayerHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpHandler createPollHandler() {
        return httpExchange -> {
            String response = "";
            System.out.println(httpExchange.getRequestURI());
            if (gameState == null) {
                // We are in lobby
                response = users.stream().filter(Objects::nonNull).collect(Collectors.joining(","));
            } else {
                // We have an ongoing game
                response = "ingame";
            }

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        };
    }

    private HttpHandler createRegisterPlayerHandler() {
        return httpExchange -> {
            System.out.println(httpExchange.getRequestURI());
            String response;
            int status = 200;
            String method = httpExchange.getRequestMethod();
            if (!method.equals("POST")) {
                response = "Error: Method not supported!";
                status = 400;
            } else {
                if (users.size() >= 5) {
                    response = "Error: Lobby is full!";
                    status = 400;
                } else {
                    InputStream stream = httpExchange.getRequestBody();
                    String body = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    System.out.println(body);
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

    public void setGameState(Game gameState) {
        this.gameState = gameState;
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
