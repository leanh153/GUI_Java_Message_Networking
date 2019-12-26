package com.business;

import com.dao.MessageDAO;
import com.entity.Client;
import com.entity.Message;
import com.entity.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class ClientHandler implements Runnable {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    private Client client;
    private JTextArea txtContent;
    private MessageDAO md;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public JTextArea getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(JTextArea txtContent) {
        this.txtContent = txtContent;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;

    }

    public ClientHandler(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

    }

    public ClientHandler(Socket socket, Client client, ObjectInputStream ois) {
        this.socket = socket;
        this.client = client;
        this.ois = ois;

    }

    public ClientHandler(Socket socket, Client client, JTextArea txtContent) {
        this.socket = socket;
        this.client = client;
        this.txtContent = txtContent;
    }

    @Override
    public void run() {

        try {
            md = new MessageDAO();
            oos = new ObjectOutputStream(socket.getOutputStream());
            // Receive data from client
            while (true) {
                Message msg = (Message) ois.readObject();
                if (msg.getType() == MessageType.NORMAL) {
                    // dispaly message content and save to database

                    txtContent.append((msg.getContent().length() > 0)
                        ? "\n" + client.getUsername() + ": " + msg.getContent() : "");

                    md.addMessageDetail(msg);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //send message to client
    public void send(Object msg) throws Exception {
        /*sending a message to client*/
        Message m = (Message) msg;
        oos.writeObject(msg);
        md.addMessageDetail(m);

        txtContent.append((m.getType() == MessageType.NORMAL && m.getContent().length() > 0)
            ? "\nMe: " + m.getContent() : "");
    }
}
