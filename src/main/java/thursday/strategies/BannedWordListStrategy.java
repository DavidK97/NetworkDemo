package thursday.strategies;

import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class BannedWordListStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        client.notify("List of banned word: ");
        for (String word : client.getInappropriateWords()) {
            client.notify(word);
        }
    }
}
