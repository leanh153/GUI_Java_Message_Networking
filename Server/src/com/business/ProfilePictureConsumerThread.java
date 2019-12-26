/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import com.dao.UserDAO;
import com.entity.Message;
import com.entity.MessageType;
import com.entity.ProfileRequest;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leanh
 */
public class ProfilePictureConsumerThread extends Thread {

    private ObjectOutputStream oos;
    private ProfileRequest pRequest;
    private ChatAppBlockingQueue profileJobs;

    ProfilePictureConsumerThread() {
    }

    ProfilePictureConsumerThread(ChatAppBlockingQueue profileJobs) {
        this.profileJobs = profileJobs;
    }

    @Override
    public void run() {
        while (true) {
            try {

                pRequest = (ProfileRequest) profileJobs.poll();

                oos = new ObjectOutputStream(
                    new DataOutputStream(pRequest.getSocket().getOutputStream()));

                Message msg = pRequest.getMessage();

                FileHelper fHelper = new FileHelper((new File("img"
                    + File.separatorChar + msg.getFrom())).getAbsolutePath());

                String imageAbsolutePath = fHelper.getDirectory() + File.separatorChar
                    + msg.getFile().getName();

                if (!fHelper.isExistFileInDir()) {
                    fHelper.saveStreamFile(pRequest.getSocket().getInputStream(),
                        msg.getFile(), imageAbsolutePath);
                } else {
                    fHelper.removeExistFile();
                    fHelper.saveStreamFile(pRequest.getSocket().getInputStream(),
                        msg.getFile(), imageAbsolutePath);
                }

                UserDAO userDao = new UserDAO();

                userDao.updateUserProfilePicture(msg.getFrom(), imageAbsolutePath);

                Message msgBack = new Message(new Date(), new File(imageAbsolutePath),
                    MessageType.PROFILE);
                send(msgBack);

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ProfilePictureConsumerThread.class.getName())
                    .log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ProfilePictureConsumerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void send(Message msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
        new FileHelper().streamFileToOuput(pRequest.getSocket().getOutputStream(), msg.getFile());
        // close this socket then Any thread currently blocked in an I/O 
        // operation upon this socket will throw a SocketException then inside the exception
        // block, i create a new socket.
        pRequest.getSocket().close();
    }

}
