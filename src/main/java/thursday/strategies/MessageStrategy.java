package thursday.strategies;

import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class MessageStrategy implements IMessageStrategy {

    @Override
    public void execute(String message, ClientHandler client) {
        if (client.getName() == null) {
            client.getServer().broadcast("Broadcasting message: no_name: " + message);
        } else {
            client.getServer().broadcast("Broadcasting message: " + client.getName() + ": " + message);
        }
    }
}