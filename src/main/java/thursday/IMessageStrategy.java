package thursday;

public interface IMessageStrategy {
    void execute (String message, ClientHandler client);
}
