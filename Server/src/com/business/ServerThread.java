/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import com.entity.ProfileRequest;
import com.dao.MessageDAO;
import com.dao.UserDAO;
import com.entity.Client;
import com.entity.HistoryRequest;
import com.entity.Message;
import com.entity.Server;
import com.ui.ServerBox;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leanh
 */
public class ServerThread implements Runnable {
    
    // Custom blocking queue 
    private ChatAppBlockingQueue historyJobs;
    private ChatAppBlockingQueue profileJobs;
    public static HashMap<String, ClientHandler> clients = new HashMap<>();

    private ServerSocket server;
    private Socket socket;

    public ServerThread(Server chatServer) throws IOException {
        server = new ServerSocket(chatServer.getPort());
        historyJobs = new ChatAppBlockingQueue<>();
        profileJobs = new ChatAppBlockingQueue<>();

    }

    @Override
    public void run() {
        /*insert code to handle a connection from each client here*/
        // this thread handle image profile picture
        new ProfilePictureConsumerThread(profileJobs).start();
        // these 2 thread handle history requests, cause this take more time
        new HistoryConsumerThread(historyJobs).start();
        new HistoryConsumerThread(historyJobs).start();

        while (true) {
            try {
                // Accept a connection from client
                socket = server.accept();
                ObjectInputStream ois = new ObjectInputStream(
                    new DataInputStream(socket.getInputStream()));
                // Read infomation of logined user
                Message msg = (Message) ois.readObject();

                switch (msg.getType()) {
                    case LOG_IN:
                        Client c = new Client(msg.getFrom(), msg.getFrom());
                        c.setSocket(socket);

                        UserDAO userHelper = new UserDAO();
                        userHelper.addUser(c);
                        MessageDAO md = new MessageDAO();
                        md.addMessageDetail(msg);
                        /**
                         * Setting message for client thread, For each
                         * connection from client, create a
                         * Thread-ClientHandlerto handle the connection
                         */
                        ServerBox.clients.addElement(c);
                        System.out.println("Welcome " + c.getUsername());
                        ClientHandler ch = new ClientHandler(socket, c, ois);
                        clients.put(c.getUsername(), ch);
                        break;

                    case PROFILE:
                        // add new profile requests to blocking queue
                        profileJobs.offer(new ProfileRequest(socket, ois, msg));
                        break;

                    case HISTORY:
                        // add new history Chat requests to blocking queue and
                        // HistoryConsumerThread handle these
                        historyJobs.offer(new HistoryRequest(socket, ois, msg));
                        break;

                    default:
                        break;
                }

            } catch (Exception ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
