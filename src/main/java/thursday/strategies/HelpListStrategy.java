package thursday.strategies;


import thursday.ClientHandler;
import thursday.IMessageStrategy;

public class HelpListStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {
        client.notify("List of available commands: ");
        client.notify(
                    "#JOIN <nickname> " +
                            "#MESSAGE <message> " +
                            "#LEAVE" +
                            "#PRIVATE <nickname> <message>" +
                            "#GETLIST" +
                            "#PRIVATEGROUP nickname1,nickname2,nickname3  <message> " +
                            "#STOPSERVER");
    }
}