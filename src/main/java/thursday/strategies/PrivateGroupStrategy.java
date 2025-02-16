package thursday.strategies;

import org.example.IObserver;
import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class PrivateGroupStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        //INPUT: DAVID,DENNIS,JENS hej til alle fra D

        String[] parts = message.split(" ",2); // input deles i targetClients og <msgOut>
        String[] targetClients = parts[0].trim().split(",");
        String msgOut = parts[1].trim();

        for (IObserver c: client.getServer().getClients()) {
            for (String targetClient: targetClients) {
                if (targetClient.equals(c.toString())) {
                    c.notify("Private group message from " + client + ": " + msgOut);
                }
            }
        }
    }
}
