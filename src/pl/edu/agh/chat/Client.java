package pl.edu.agh.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    String userName;
    ClientGUI clientGUI;

    public Client(int portNumber, String userName) {
        try {
            this.socket = new Socket("localhost", portNumber);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;

            this.clientGUI = new ClientGUI(this.userName);
            this.clientGUI.setWindow();
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

            while (socket.isConnected()) {
                String messageToSend = returnMessageFromGUI();
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
                    transferMessageFromGroupToGUI(messageFromGroupChat);
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

    public void transferMessageFromGroupToGUI(String messageFromGroupChat) {
        String[] lines = clientGUI.textArea.getText().split("\n");

        if (lines.length == 17) {
            clientGUI.textArea.setText("");

            for (int i = 1; i < 17; i++)
                clientGUI.textArea.append(lines[i] + "\n");
        }

        clientGUI.textArea.append(messageFromGroupChat + "\n");
    }

    public String returnMessageFromGUI() {
        while(clientGUI.currentMessage == null) {
            try {
                Thread.sleep(300);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        String messageFromGUI = clientGUI.currentMessage;
        clientGUI.currentMessage = null;

        return messageFromGUI;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your userName:");
        String userName = scanner.nextLine();

        Client client = new Client(8900, userName);
        client.startClient();
    }
}