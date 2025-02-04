package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleDemo {
    private ServerSocket server;
    private Socket clientHandler;
    private PrintWriter out;
    private BufferedReader in;

    public void start () {
        try {
            server = new ServerSocket(8080);
            clientHandler = server.accept();
            out = new PrintWriter(clientHandler.getOutputStream(), true); //Sekvens af 0'er og 1-taller der sendes
            in = new BufferedReader(new InputStreamReader(clientHandler.getInputStream()));
            out.println("Connection established");
            String inputLine;
            while ((inputLine = in.readLine()) !=  null) { //Tjekker om der kommer noget ind fra klienten
                System.out.println("Message from client: " + inputLine);
                if ("Exit".equals(inputLine)){ //Breaks the while loop if the client enters "Exit"
                    out.println("The server is shutting down");
                    stop();
                }
            }
            in.readLine();

        } catch (IOException ex) {
            //TODO: Fjern stacktrace inden produktion
            ex.printStackTrace();

        } finally { //Bliver altid udført
            try {
                stop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void stop () throws IOException {
        in.close();
        out.close();
        clientHandler.close();
        server.close();
    }

    public static void main(String[] args) {
        new SimpleDemo().start();
    }
}
