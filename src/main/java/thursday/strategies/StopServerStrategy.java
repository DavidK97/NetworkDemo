package thursday.strategies;

import org.example.IObserver;
import thursday.ClientHandler;
import thursday.IMessageStrategy;

import java.io.IOException;

public class StopServerStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        client.getServer().broadcast("The server is shutting down");
        for (IObserver ch: client.getServer().getClients()) {
            try {
                //Caster IObserver ch til et ClientHandler objekt, for at få adgang til dets metoder
                ClientHandler clientHandler = (ClientHandler) ch;

                //Lukker I/O streams og client-forbindelse til serveren
                clientHandler.getOut().close();
                clientHandler.getIn().close();
                clientHandler.getClientSocket().close();
                System.exit(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
