package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;


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
    }


    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String echoMsg = "";

        public ClientHandler (Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run(){
            try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //bytes til char
            out.println("Connection established"); //Besked til klienten


                // Tjekker om der kommer noget ind fra klienten
                String inputLine;
                while ((inputLine = in.readLine()) !=  null) {
               // System.out.println("Message from client: " + inputLine); //Printer i konsollen


                //Slutter while-loop, hvis klient skriver EXIT
                if ("EXIT".equals(inputLine)) {
                    out.println("The server is shutting down");
                    in.close();
                    out.close();
                    clientSocket.close();

                    break;
                }

                //TODO: indexOutOfBounds
                //Læser requests fra klienten
                String [] request = inputLine.split(" ");
                String method = request[0];
                String path = request[1];
                String protocolVersion = request[2];
                if (request.length == 3 && method.equals("GET") && protocolVersion.equals("HTTP/1.1")) {
                    switch (path) {
                        case ("/hello"):
                                printResponse(out);
                                out.println("hello");
                                break;

                        case ("/time"):
                                printResponse(out);
                                out.println(LocalDateTime.now()); //TODO: udskriv i andet format
                                break;

                        case ("/echo"):
                            out.println((echoMsg));
                                break;
                        default:
                            out.println("HTTP/1.1 400 Bad Request");
                            break;
                    }
                } else if (request.length == 3 && method.equals("POST") && protocolVersion.equals("HTTP/1.1")){
                    switch (path) {
                        case ("/echo"):
                            StringBuilder requestBuilder = new StringBuilder();
                            String newLine;

                            //Læser og gemmer headers indtil en tom linje
                            while (in.ready() && (newLine = in.readLine()) != null && !newLine.isEmpty()) {
                                requestBuilder.append(newLine).append("\n");
                            }
                            echoMsg = in.readLine();

                            out.println("Message saved");
                            break;
                    }
                } else {
                    out.println("HTTP/1.1 400 Bad Request");
                }
            }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }

    public static void printResponse(PrintWriter out) {
        //TODO: udskift hardcoded response-data
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 200 OK  \n");
        response.append("Date: Sun, 26 Jan 2025 10:00:00 GMT \n");
        response.append("Server: Min server \n");
        response.append("Content-Type: text/html; charset=UTF-8 \n");
        response.append("Content-Length: 5");

        out.println(response);
    }


    public static void main(String[] args) {
        new SimpleDemo().start();
    }
}
