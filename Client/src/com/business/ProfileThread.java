/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import com.entity.Message;
import com.entity.Server;
import com.entity.MessageType;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author leanh
 */
public class ProfileThread extends Thread {

    private Socket socket;
    private ObjectOutputStream oos;
    ObjectInputStream ois;
    private JLabel lableIcon;
    private volatile boolean running = true;
    private Server server;

    /**
     *
     * @param server to create new socket to connect server. every message use
     * one socket, when server side complete send back result then close this socket
     * and this thread occur one exception EOFExeption then i create new socket for
     * this thread to send new request
     * @param userIcon to display user icon
     * @throws IOException
     */
    public ProfileThread(Server server, JLabel userIcon)
        throws IOException {
        this.server = server;
        this.lableIcon = userIcon;
        socket = new Socket(server.getHost(), server.getPort());
        oos = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()));
    }

    

    /**
     *
     * @param running to close this thread by use volatile key word
     */
    public void setRunnable(boolean running) {

        this.running = running;
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ProfileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        try {
            ois = new ObjectInputStream(
                new DataInputStream(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ProfileThread.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ois " + ex);

        }
        while (running) {
            try {
                // Get message from server, exactly is ProfilePictureConsumerThread.class
                
                Message msg = (Message) ois.readObject();

                if (msg.getType() == MessageType.PROFILE) {
                    
                    FileHelper fHelper = new FileHelper((new File("img"))
                        .getAbsolutePath());
                    
                    String imageAbsolutePath = fHelper.getDirectory()
                        + File.separatorChar + msg.getFile().getName();

                    if (!fHelper.isExistFileInDir()) {
                        fHelper.saveStreamFile(socket.getInputStream(),
                            msg.getFile(), imageAbsolutePath);
                    } else {
                        fHelper.removeExistFile();
                        fHelper.saveStreamFile(socket.getInputStream(),
                            msg.getFile(), imageAbsolutePath);
                    }

                    lableIcon.setIcon(new ImageIcon(new ImageIcon(imageAbsolutePath)
                        .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ProfileThread.class.getName()).log(Level.SEVERE, null, ex);
                /**
                 This create new socket to send new message, cause sent socket
                is closed by server side when it completely sending back result
                */
                try {
                    socket = new Socket(server.getHost(), server.getPort());
                    oos = new ObjectOutputStream(
                        new DataOutputStream(socket.getOutputStream()));
                    ois = ois = new ObjectInputStream(
                        new DataInputStream(socket.getInputStream()));
                } catch (IOException ex1) {
                    Logger.getLogger(ProfileThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
    }

    /**
     *
     * @param msg Message object to be sent to server
     * @throws IOException
     */
    public void send(Object msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
        /** Stream picture that will be become user icon. This image will be saved
            in server side and its absolute path will be save to database. Then
            stream that image back to client and save to the app image(img) 
            directory then set that image to label on the top right side corner**/
        new FileHelper().streamFileToOuput(socket.getOutputStream(), ((Message) msg).getFile());
    }

}
