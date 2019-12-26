/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import java.util.LinkedList;

/**
 *
 * @author leanh
 * @param <T>
 *
 */
public class ChatAppBlockingQueue<T> {

    private LinkedList<T> ll = new LinkedList<>();

    public ChatAppBlockingQueue() {
    }


    public synchronized void offer(T e) {
        ll.add(e); //To change body of generated methods, choose Tools | Templates.
        notifyAll();
    }

    public synchronized T poll() throws InterruptedException {

        while (ll.isEmpty()) {
                wait();
        }
        return ll.poll();
    }

}
