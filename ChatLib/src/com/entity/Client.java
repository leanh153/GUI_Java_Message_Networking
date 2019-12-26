/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author Leanh
 */
public class Client implements Serializable {

    private String username, displayName, password;
    private Socket socket;

    public Client() {
    }

    public Client(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public Client(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    public Client(String username, String displayName, String password, Socket socket) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    

     @Override
    public String toString() {
        return username; //To change body of generated methods, choose Tools | Templates.
    }
}
