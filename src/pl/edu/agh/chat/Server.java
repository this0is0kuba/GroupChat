package pl.edu.agh.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;

    public ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(int portNumber) {
        try {
            this.serverSocket = new ServerSocket(portNumber);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void startServer() {
        try {

            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A New client has been connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            serverSocket.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8900);
        server.startServer();
    }
}
