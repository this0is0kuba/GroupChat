package pl.edu.agh.chat;

import javax.swing.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ActionListener, WindowListener, KeyListener {
    JFrame frame;
    JTextField textField;
    JButton submitButton;
    JTextArea textArea;

    public ClientGUI() {
        frame = new JFrame("chat");
        textField = new JTextField(16);
        submitButton = new JButton("submit");
        textArea = new JTextArea(18, 20);
    }

    private void setObjectsInWindow() {
        textArea.setEditable(false);
    }

    private void addAllListener() {
        submitButton.addActionListener(this);
        textField.addKeyListener(this);
        frame.addWindowListener(this);
    }
    public void setWindow() {
        setObjectsInWindow();
        addAllListener();

        JPanel panel = new JPanel();

        panel.add(textField);
        panel.add(submitButton);
        panel.add(textArea);

        frame.add(panel);
        frame.setSize(300,400);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            this.actionSendMessage();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            this.actionSendMessage();
    }

    public void actionSendMessage() {
        if (textField.getText().isEmpty() || textField.getText().length() > 27)
            return;

        String[] lines = textArea.getText().split("\n");

        if (lines.length == 17) {
            textArea.setText("");

            for (int i = 1; i < 17; i++)
                textArea.append(lines[i] + "\n");
        }


        textArea.append(textField.getText() + "\n");
        textField.setText("");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        frame.dispose();
    }

    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.setWindow();
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
