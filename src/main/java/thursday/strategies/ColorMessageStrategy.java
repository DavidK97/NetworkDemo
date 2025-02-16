package thursday.strategies;

import thursday.ClientHandler;
import thursday.ColorDecorator;
import thursday.IMessageStrategy;

public class ColorMessageStrategy implements IMessageStrategy {
    @Override
    public void execute(String message, ClientHandler client) {

        String coloredMessage = new ColorDecorator("\u001B[31m").decorate(message);  //rød farve
        client.getServer().broadcast(coloredMessage);

    }
}
