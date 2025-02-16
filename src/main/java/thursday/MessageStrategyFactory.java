package thursday;

import thursday.strategies.*;

import java.util.HashMap;
import java.util.Map;

public class MessageStrategyFactory {
    private static Map <String, IMessageStrategy> strategies = new HashMap();
    static {
        strategies.put("#JOIN", new JoinStrategy()); //VIRKER
        strategies.put("#MESSAGE", new MessageStrategy()); //VIRKER
        strategies.put("#LEAVE", new LeaveStrategy()); //VIRKER
        strategies.put("#GETLIST", new GetlistStrategy()); //VIRKER
        strategies.put("#PRIVATE", new PrivateStrategy()); //VIRKER
        strategies.put("#PRIVATEGROUP", new PrivateGroupStrategy()); //VIRKER
        strategies.put("#STOPSERVER", new StopServerStrategy()); //VIRKER
        strategies.put("#HELPLIST", new HelpListStrategy()); //VIRKER TODO formatering på output er underlig
        strategies.put("#COLOREDMESSAGE", new ColorMessageStrategy()); //VIRKER TODO så man selv kan vælge farve
        strategies.put("#ADDWORD", new AddWordStrategy()); //VIRKER
        strategies.put("#REMOVEWORD", new RemoveWordStrategy()); //VIRKER
        strategies.put("#BANNEDWORDLIST", new BannedWordListStrategy()); //VIRKER
    }


    public static IMessageStrategy getStrategy(String strategy) {
        return strategies.getOrDefault(strategy, new IMessageStrategy(){ //Default er til hvis de giver en invalid cmd. Anonymt objekt??
            @Override
            public void execute(String message, ClientHandler client) {
                client.notify("Not a valid command: " + message);
            }
        });
    }
}
