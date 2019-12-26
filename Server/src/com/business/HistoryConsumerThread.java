/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import com.dao.MessageDAO;
import com.entity.HistoryRequest;
import com.entity.Message;
import com.entity.MessageType;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leanh
 */
public class HistoryConsumerThread extends Thread {

    private ObjectOutputStream oos;
    private ChatAppBlockingQueue historyJobs;
    private HistoryRequest hRequest;

    public HistoryConsumerThread() {

    }

    HistoryConsumerThread(ChatAppBlockingQueue historyJobs) {
        this.historyJobs = historyJobs;
    }

    @Override
    public void run() {

        while (true) {
            try {
                hRequest = (HistoryRequest) historyJobs.poll();

                oos = new ObjectOutputStream(
                    new DataOutputStream(hRequest.getSocket().getOutputStream()));

                Message msg = hRequest.getMessage();
                String from = msg.getFrom();
                Date fromDate = msg.getFromDate();
                Date toDate = msg.getToDate();

                MessageDAO mDAO = new MessageDAO();
                List<Message> lists = mDAO.filtByDates(fromDate, toDate, from);

                Message mBack = new Message(lists, MessageType.HISTORY);
                send(mBack);

            } catch (IOException ex) {
                Logger.getLogger(HistoryConsumerThread.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("History consumer: " + ex);
            } catch (Exception ex) {
                Logger.getLogger(HistoryConsumerThread.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("History consumer: " + ex);
            }
        }

    }

    private void send(Message message) throws IOException {
        oos.writeObject(message);
        oos.flush();
        hRequest.getSocket().close();
    }

    }
