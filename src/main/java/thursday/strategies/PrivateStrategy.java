package thursday.strategies;

import org.example.IObserver;
import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class PrivateStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        //INPUT: DAVID hej fra david

        String[] parts = message.split(" ", 2);
        String targetClient = parts[0];
        String msgOut = parts[1];

        for (IObserver c : client.getServer().getClients()) {
            if (targetClient.equals(c.toString())) {
                c.notify("Message from " + client + ": "+ msgOut);
            }
        }
    }
}
