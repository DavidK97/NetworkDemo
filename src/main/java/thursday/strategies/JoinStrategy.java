package thursday.strategies;

import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class JoinStrategy implements IMessageStrategy {

    @Override
    public void execute(String message, ClientHandler client) {
        client.setName(message);
        client.getServer().broadcast("Welcome! " + message + " has joined the server");
    }
}
