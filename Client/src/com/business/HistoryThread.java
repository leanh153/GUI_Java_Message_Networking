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
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author leanh
 */
public class HistoryThread extends Thread {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private DefaultTableModel tableModel;
    private volatile boolean running = true;
    private Server server;

    /**
     *
     * @param server information contain IP and post 
     * @param tableModel table's model to display history result
     * @throws IOException
     */
    public HistoryThread(Server server, DefaultTableModel tableModel)
        throws IOException {
        this.server = server;
        this.tableModel = tableModel;
        socket = new Socket(server.getHost(), server.getPort());
        oos = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()));
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     *
     * @param running runnable of this thread
     */
    public void setRunnable(boolean running) {
        this.running = running;
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(HistoryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        try {
            ois = new ObjectInputStream(
                new DataInputStream(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(HistoryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (running) {
            try {
                Message msg = (Message) ois.readObject();

                if (msg.getType() == MessageType.HISTORY) {
                    List<Message> lists = msg.getLstMessage();
                    if (tableModel.getRowCount() > 0) {
                        removeOldData();
                    }
                    lists.forEach(item -> {
                        tableModel.addRow(new Object[]{item.getDateCreated(),
                            item.getFrom(), item.getTo(), item.getContent(),
                            item.getType()});
                    });

                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ProfileThread.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    Logger.getLogger(ProfileThread.class.getName()).log(Level.SEVERE, null, ex);
                    /**
                     * This create new socket to send new message, cause sent
                     * socket is closed by server side when it completely
                     * sending back result, then this thread throw EOFExeption
                     * then this thread need a new socket
                     */
                    socket = new Socket(server.getHost(), server.getPort());
                    oos = new ObjectOutputStream(
                        new DataOutputStream(socket.getOutputStream()));
                    ois = new ObjectInputStream(
                        new DataInputStream(socket.getInputStream()));
                } catch (IOException ex1) {
                    Logger.getLogger(HistoryThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

    }

    /**
     *
     * @param msg
     * @throws IOException
     */
    public void send(Object msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
    }

    // remove row that exist in table 
    private void removeOldData() {
        int rowsToRemove = tableModel.getRowCount();
        for (int i = rowsToRemove - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

}
