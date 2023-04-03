package pl.edu.agh.chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
public class ClientHandler extends Thread {
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    String userName;

    static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = br.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server: " + this.userName + " has entered the chat");
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            closeEverything();
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()) {

            try {
                messageFromClient = br.readLine();
                broadcastMessage(messageFromClient);
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
                closeEverything();
                break;
            }
        }
    }

    public void broadcastMessage(String message) {
        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if(clientHandler != this)
                    sendMessage(message, clientHandler);
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
                closeEverything();
                break;
            }
        }
    }

    public void sendMessage(String message, ClientHandler clientHandler) throws IOException{
            clientHandler.bw.write(message);
            clientHandler.bw.newLine();
            clientHandler.bw.flush();
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("Server: " + this.userName + "has left the chat");
    }

    public void closeEverything() {
        removeClientHandler();
        try {
            this.br.close();
            this.bw.close();
            this.socket.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
