/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Leanh
 */
public class Message implements Serializable {
    private int id;
    private String from;
    private String to;
    private Date dateCreated;
    private String content;
    private File file;
    private List<Message> lstMessage; 
    private Date fromDate; // date filter in history 
    private Date toDate;    // date filter in history 
    private MessageType type;

    public Message(Date dateCreated, Date fromDate, Date toDate, MessageType type) {
        this.dateCreated = dateCreated;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.type = type;
    }

    public Message() {
    }

    public Message(String from, Date dateCreated, Date fromDate, Date toDate, 
        MessageType type) {
        this.from = from;
        this.dateCreated = dateCreated;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.type = type;
    }

    /**
     *
     * @param lstMessage that user has sent
     * @param type MessageType.HISTORY to indicate message type
     */
    public Message(List<Message> lstMessage, MessageType type) {
        this.lstMessage = lstMessage;
        this.type = type;
    }

    public Message(String from, String to, Date dateCreated, String content, 
        MessageType type) {
        this.from = from;
        this.to = to;
        this.dateCreated = dateCreated;
        this.content = content;
        this.type = type;
    }

    public Message(int id, String from, String to, Date dateCreated, 
        String content, MessageType type) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.dateCreated = dateCreated;
        this.content = content;
        this.type = type;
    }

    public Message(String from, String to, Date dateCreated, File file, 
        MessageType type) {
        this.from = from;
        this.to = to;
        this.dateCreated = dateCreated;
        this.file = file;
        this.type = type;
    }

    public Message(String from, Date dateCreated, File file, MessageType type) {
        this.from = from;
        this.dateCreated = dateCreated;
        this.file = file;
        this.type = type;
    }

    public Message(Date dateCreated, File file, MessageType type) {
        this.dateCreated = dateCreated;
        this.file = file;
        this.type = type;
    }

   

 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<Message> getLstMessage() {
        return lstMessage;
    }

    public void setLstMessage(List<Message> lstMessage) {
        this.lstMessage = lstMessage;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return this.content;
    }

}
