package thursday.strategies;

import thursday.ClientHandler;
import thursday.IMessageStrategy;

import java.io.IOException;

public class LeaveStrategy implements IMessageStrategy {
    @Override
    public void execute (String message, ClientHandler client) {
        if (client.getName() != null) {
            client.getServer().broadcast(client.getName() + " has left the chat");
        } else {
            client.getServer().broadcast("no_name has left the chat ");
        }
        leaveServer(client);
    }

    //Close input- and output-streams and serverSocket
        private void leaveServer (ClientHandler client){
        try {
            client.getServer().removeObserver(client);
            client.getOut().close();
            client.getIn().close();
           // client.getClientSocket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
