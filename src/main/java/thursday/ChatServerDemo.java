package thursday;

import org.example.IObservable;
import org.example.IObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerDemo implements IObservable {
    private static volatile IObservable server = getInstance(); //volatile så værdien ikke bliver cached, men så alle Threads læser direkte fra main memory
    private ChatServerDemo() {}
    public static List<IObserver> clients = new ArrayList<>();


    private synchronized static IObservable getInstance () { //synchronized så kun én Thread kan komme ind i metoden ad gangen
        if (server == null) {
            server = new ChatServerDemo();
        }
        return server;
    }


    public void startServer (int port) {
        try {
            ExecutorService executor = Executors.newCachedThreadPool(); //Nedsætter overhead
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept(); //Returnerer ved accept en socket
                //ClientHandler clientHandler = new ClientHandler(client, this); //tlf til client og reference til server
                Runnable runnable = new ClientHandler(client, this);
                executor.submit(runnable);
                clients.add((IObserver) runnable);
                //new Thread(clientHandler).start(); //En thread tager et runnable objekt som parameter, derfor at clientHanlder skal implemnet det
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(IObserver iObserver) {
        clients.add(iObserver);
    }

    @Override
    public void removeObserver(IObserver iObserver) {
        clients.remove(iObserver);
    }

    @Override
    public void broadcast(String msg) { //Når en besked sendes af én klient, så bliver den broadcastede ud til alle klienter på serveren
        for (IObserver ch : clients) {
            ch.notify(msg);
        }
    }

    public static List<IObserver> getClients() {
        return clients;
    }

    public static IObservable getServer() {
        return server;
    }

    public static void setServer(IObservable server) {
        ChatServerDemo.server = server;
    }

    public static void main(String[] args) {
        ChatServerDemo chatServerDemo = new ChatServerDemo();
        chatServerDemo.startServer(12345);
    }
}
