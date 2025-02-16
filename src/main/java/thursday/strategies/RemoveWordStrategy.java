package thursday.strategies;

import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class RemoveWordStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        client.removeInappropriateWord(message);

    }
}
