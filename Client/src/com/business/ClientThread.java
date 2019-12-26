/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import com.entity.Message;
import com.entity.Server;
import com.entity.MessageType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Leanh
 */
public class ClientThread implements Runnable, Serializable {

    //for I/O
    private Socket socket;
    private Server server;
    private JTextArea txtContent;
    //use to read and write data to/from server
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ClientThread(Server server, JTextArea txtContent) throws IOException {
        /*insert code for opening a connection to server here*/
        this.txtContent = txtContent;
        this.server = server;
        socket = new Socket(server.getHost(), server.getPort());
        oos = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            /*insert code for receiving and output a message from server here*/
            ois = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
            while (true) {
                Message msg = (Message) ois.readObject();
                if (msg.getType() == MessageType.NORMAL) {
                    // output received message
                    txtContent.append((msg.getContent().length() > 0) ? "\n" + msg.getFrom()
                        + ": " + msg.getContent() : "");

                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //send message to client
    public void send(Object msg) throws Exception {
        /*insert code for sending a message to client here*/
        oos.writeObject(msg);
        oos.flush();
        Message m = (Message) msg;
        txtContent.append((m.getType() == MessageType.NORMAL && m.getContent().length() > 0)
            ? "\nMe: " + m.getContent() : "");

    }

}
