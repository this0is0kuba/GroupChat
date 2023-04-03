package pl.edu.agh.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    String userName;

    public Client(int portNumber, String userName) {
        try {
            this.socket = new Socket("localhost", portNumber);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            closeEverything();
        }
    }

    public void startClient() {
        this.listenToMessage();
        this.sendMessage();
    }

    public void sendMessage() {
        try {
            bw.write(userName);
            bw.newLine();
            bw.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bw.write(userName + ": " + messageToSend);
                bw.newLine();
                bw.flush();
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            closeEverything();
        }
    }

    public void listenToMessage() {

        Thread listen = new Thread(() -> {

            String messageFromGroupChat;

            while(socket.isConnected()) {

                try {
                    messageFromGroupChat = br.readLine();
                    System.out.println(messageFromGroupChat);
                }
                catch(IOException ioe) {
                    ioe.printStackTrace();
                    closeEverything();
                    break;
                }
            }
        });

        listen.start();
    }
    public void closeEverything() {
        try {
            br.close();
            bw.close();
            socket.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your userName:");
        String userName = scanner.nextLine();

        Client client = new Client(8900, userName);
        client.startClient();
    }
}