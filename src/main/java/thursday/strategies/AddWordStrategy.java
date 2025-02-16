package thursday.strategies;

import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class AddWordStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        client.addInappropriateWord(message);
    }
}
