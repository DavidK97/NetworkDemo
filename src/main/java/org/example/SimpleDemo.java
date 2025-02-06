package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.out;

public class SimpleDemo {
    private ServerSocket server;


    public void start () {
        try {
            server = new ServerSocket(8080);


        while (true) {
                Socket socket = server.accept();
                Runnable clientHandler = new ClientHandler(socket);
                new Thread (clientHandler).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
/*
        } finally { //Bliver altid udført
            try {
                stop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

           */
    }
    /*
    private void stop () throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        server.close();
    }

     */

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler (Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run(){
            try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); //Sekvens af 0'er og 1-taller der sendes
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("Connection established");
            String inputLine;
            while ((inputLine = in.readLine()) !=  null) { //Tjekker om der kommer noget ind fra klienten
                System.out.println("Message from client: " + inputLine); //Printer i konsollen
                if ("Exit".equals(inputLine)) { //Breaks the while loop if the client enters "Exit"
                    out.println("The server is shutting down");
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
            }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }


    public static void main(String[] args) {
        new SimpleDemo().start();
    }
}
