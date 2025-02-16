package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatServerDemo implements IObservable{
    private static volatile IObservable server = getInstance(); //volatile så værdien ikke bliver cached, men så alle Threads læser direkte fra main memory
    private ChatServerDemo () {}
    public String [] bannedWords = {"fuck, cunt"};

    private synchronized static IObservable getInstance () { //synchronized så kun én Thread kan komme ind i metoden ad gangen
        if (server == null) {
            server = new ChatServerDemo();
        }
        return server;
    }

    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        ChatServerDemo chatServerDemo = new ChatServerDemo();
        chatServerDemo.startServer(12345);
    }


    public void startServer (int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept(); //Returnere ved accept en sockets
                ClientHandler clientHandler = new ClientHandler(clientSocket, this); //tlf til client og reference til server
                clients.add(clientHandler);
                new Thread(clientHandler).start(); //En thread tager et runnable objekt som parameter, derfor at clientHanlder skal implemnet det
            }
        } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    @Override
    public void addObserver(IObserver iObserver) {
    }

    @Override
    public void removeObserver(IObserver iObserver) {
        clients.remove(iObserver);
    }

    @Override
    public void broadcast(String msg) { //Når en besked sendes af én klient, så bliver den broadcastede ud til alle klienter på serveren
        for (ClientHandler ch : clients) {
            ch.notify(msg);
        }
    }

    public String[] getBannedWords() {
        return bannedWords;
    }

    public void setBannedWords(String[] bannedWords) {
        this.bannedWords = bannedWords;
    }


    private static class ClientHandler implements Runnable, IObserver {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private IObservable server;
        private String name = null;

        public ClientHandler (Socket socket, IObservable server) throws IOException {
            this.clientSocket = socket;
            this.server = server;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public String getName() {
            return name;
        }

        @Override
        public void notify (String msg) {
            System.out.println(msg);
            out.println(msg);
        }

        @Override
        public void run () {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);

                        //#JOIN <nickname> a client joins the chat with a nickname
                        if (msg.startsWith("#JOIN ")) {
                            this.name = msg.split(" ")[1]; //Sætter navn som det der gemmes på index 1
                            server.broadcast("New person joined the chat. Welcome to " + name);

                            //#MESSAGE <message> a clients sends a msg to all other clients
                        } else if (msg.startsWith("#MESSAGE ")) {
                            String msgOut = msg.split(" ")[1];
                            if (name == null) {
                                server.broadcast("Broadcasting message: no_name: " + msgOut);
                            } else {
                                server.broadcast("Broadcasting message: " + name + ": " + msgOut);
                            }

                            //#LEAVE client leaves the chat (I/O streams lukkes)
                        } else if (msg.startsWith("#LEAVE")) {
                            if (name != null) {
                                out.println(name + "has left the chat ");
                            } else {
                                out.println("no_name has left the chat ");
                            }
                            server.removeObserver(this);
                            out.close();
                            in.close();
                            clientSocket.close();
                            break;

                            //#PRIVATE <nickname> <message> a client sends a private message to another client
                        } else if (msg.startsWith("#PRIVATE ")) {
                            String [] parts = msg.split(" ", 3); //Splitter input i 3: command vs targetClient vs msgOut
                            String targetClient = parts[1];
                            String msgOut = parts[2];

                           for (ClientHandler ch: clients) {
                               if (targetClient.equals(ch.getName())) {
                                   ch.notify(msgOut);
                               }
                           }

                           //#GETLIST returns a list of all active clients
                        } else if (msg.startsWith("#GETLIST")) {
                            out.println("Active users: ");
                            for (ClientHandler ch: clients) {
                                out.println(ch.getName());
                            }

                            //#PRIVATESUBLIST <client1, client2, client3> / <message> sends a private msg to a list of clients
                        } else if (msg.startsWith("#PRIVATESUBLIST ")) {
                            String[] parts = msg.split(" / "); // input deles i command og targetClients / <message>
                            //Substring returnerer sig selv (minus sin længde i dette tilfælde, derfor ikke cmd'en)
                            String[] targetClients = parts[0].substring("#PRIVATESUBLIST ".length()).split(",");
                            String msgOut = parts[1].trim();

                            for (ClientHandler ch: clients) {
                                for (String s: targetClients) {
                                    if (s.trim().equals(ch.getName())){
                                        ch.notify(msgOut);
                                    }
                                }
                            }


                            //#HELP Shows a list of available commands
                        } else if (msg.startsWith("#COMMANDLIST")) {
                            out.print("Available commands: \n" +
                                     "#JOIN <nickname> \n" +
                                     "#MESSAGE <message> \n" +
                                     "#LEAVE - \n" +
                                     "#PRIVATE <nickname> <message> \n" +
                                     "#GETLIST \n" +
                                     "#PRIVATESUBLIST \"nickname1,nickname2,nickname3\" <message> - \n" +
                                     "#STOPSERVER");

                            //#STOPSERVER shuts down the server and closes all connections
                        } else if (msg.startsWith("#STOPSERVER")) {
                            out.println("The server is shutting down: ");
                            for (ClientHandler ch: clients) {
                                ch.out.close();
                                ch.in.close();
                                ch.clientSocket.close();
                            }
                            //TODO close server
                          //  server.close();
                        }
                    }
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
        }
    }
}
