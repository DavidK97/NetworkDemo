package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;


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
               // System.out.println("Message from client: " + inputLine); //Printer i konsollen

                //Breaks the while loop if the client enters "EXIT"
                if ("EXIT".equals(inputLine)) {
                    out.println("The server is shutting down");
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }

                //TODO: Tjekke om input fra klient overholder protokollen for et HTTP request, hvis ikke så returner "HTTP/1.1 400 Bad Request"
                //TODO: Noget der bryder et request ned i "method", "path" og "protocol version" (.split på "/" og prop i Array)
                //TODO: Hvis et GET request: så noget der håndterer /hello, /time, /echo (if-statements, eventuelt en switch?)
                //TODO: Hvis et POST request: så noget der gemmer input fra bruger og returnere det på næste GET /echo request (Gemmer det i et array?)

                String [] request = inputLine.split(" ");
                if (request.length == 3 && request[0].equals("GET") && request[2].equals("HTTP/1.1")) {
                    switch (request[1]) {
                        case ("/hello"):
                                printResponse(out);
                                out.println("hello");
                                break;

                        case ("/time"):
                                printResponse(out);
                                out.println(LocalDateTime.now()); //Andet format
                                break;

                        case ("/echo"):
                                break;
                        default:
                            out.println("HTTP/1.1 400 Bad Request");
                            break;
                    }
                } else if (request[0].equals("POST") && request[2].equals("HTTP/1.1")){
                    switch (request[1]) {
                        case ("/echo"):
                            /*
                            StringBuilder requestBuilder = new StringBuilder();
                            String newLine;
                            while (in.ready() && (newLine = in.readLine()) != null && !newLine.isEmpty()) {
                                requestBuilder.append(newLine).append("\n");
                            }
                            
                             */
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
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 200 OK  \n");
        response.append("Date: Sun, 26 Jan 2025 10:00:00 GMT \n");
        response.append("Server: Min server \n");
        response.append("Content-Type: text/html; \n");
        response.append("charset=UTF-8 \n");
        response.append("Content-Length: 5");

        out.println(response);
    }

/*
    public String sendMessage(String msg) {
        out.println(msg);
        out.flush();
        StringBuilder sb = new StringBuilder();
        in.lines().forEach(sb::append);
        String resp = sb.toString();
        return resp;
    }

 */

    public static void main(String[] args) {
        new SimpleDemo().start();
    }
}
