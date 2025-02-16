package thursday.strategies;

import org.example.IObserver;
import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class GetlistStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        client.notify("Active users: ");
        for (IObserver c : client.getServer().getClients()) {
            //client.getServer().broadcast(String.valueOf(c));
            client.notify(c.toString());
        }
    }
}
