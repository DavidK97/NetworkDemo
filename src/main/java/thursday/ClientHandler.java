package thursday;

import org.example.IObservable;
import org.example.IObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ClientHandler implements Runnable, IObserver { //Opretter klasse som implementerer Runnable og IObserver interface.
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private IObservable server;
    private String name = null;
    private ArrayList<String> inappropriateWords = new ArrayList<>(Arrays.asList("lort", "fuck", "fag"));
    private int inappropriateWordCounter;

    public ClientHandler(Socket socket, IObservable server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        inappropriateWordCounter = 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                banClient();
                //input splittes i command og restMessage fx. #MESSAGE  hej alle
                String [] parts = msg.split(" ", 2);
                String command = parts[0];
                String restMessage = parts.length > 1 ? parts[1]: msg.trim();
                String checkedMessage = replaceInappropriateWords(restMessage);
                MessageStrategyFactory.getStrategy(command).execute(checkedMessage, this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addInappropriateWord (String word) {
        inappropriateWords.add(word);
    }
    public void removeInappropriateWord (String word) {
        inappropriateWords.remove(word);
    }

    public String replaceInappropriateWords (String message) {
        boolean containsInappropriateWord = false;
        for (String word : inappropriateWords) {
            if(message.toLowerCase().contains(word.toLowerCase())) {
                containsInappropriateWord = true;
                message = message.replace(word, "***"); //Alt laves til lowercase, for bedre sammenligning
            }
        }
        if (containsInappropriateWord) {
            inappropriateWordCounter ++;
            this.notify("You have used a banned word. " + inappropriateWordCounter + "/3 chances used");
        }
        return message;
    }

    public void banClient() {
        if (inappropriateWordCounter == 2) {
            try {
                notify("You have been banned for inappropriate word use");
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void notify(String msg) {
        System.out.println(msg);
        out.println(msg);
    }



//Getters and Setters
    public ChatServerDemo getServer() {
        return (ChatServerDemo) this.server;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setServer(IObservable server) {
        this.server = server;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public ArrayList<String> getInappropriateWords() {
        return inappropriateWords;
    }
}