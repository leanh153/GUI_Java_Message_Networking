/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.context.DBContext;
import com.entity.Message;
import com.entity.MessageType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

/**
 *
 * @author Leanh
 */
public class MessageDAO {

    //add a new message detail
    public void addMessageDetail(Message msg) throws Exception {
        /*insert code for inserting a new Message to table Message*/
        String insert = "insert into MessageDetail values(?,?,?,?,?)";
        Connection con = new DBContext().getConnection();

        PreparedStatement ps = con.prepareStatement(insert);
        ps.setString(1, msg.getFrom());
        ps.setString(2, msg.getTo());
        ps.setDate(3, new Date(msg.getDateCreated().getTime()));
        ps.setString(4, msg.getContent());

        switch (msg.getType()) {
            case LOG_IN:
                ps.setString(5, "Login");
                break;
            case LOG_OUT:
                ps.setString(5, "Logout");
                break;
            case NORMAL:
                ps.setString(5, "Message");
                break;
            case PROFILE:
                ps.setString(5, "Profile");
                break;
            case HISTORY:
                ps.setString(5, "History");
                break;
            default:
                break;
        }

        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public List<Message> filtByDates(java.util.Date fromDate, java.util.Date toDate, String from)
        throws SQLException, Exception {

        String query = " select * from MessageDetail where FromUser = '" + from
            + "' and DateCreated >= '" + new Date(fromDate.getTime()) + "' "
            + "and DateCreated <= '" + new Date(toDate.getTime()) + "'";

        Connection con = new DBContext().getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Message> lists = new ArrayList<>();
        while (rs.next()) {
            String fromUser = rs.getString("FromUser");
            String toUser = rs.getString("ToUser");
            Date createdDate = rs.getDate("DateCreated");
            String content = rs.getString("Content");
            String messageType = rs.getString("MessageType");

            MessageType type = null;

            if (messageType.equalsIgnoreCase("Login")) {
                type = MessageType.LOG_IN;
            } else if (messageType.equalsIgnoreCase("Logout")) {
                type = MessageType.LOG_OUT;
            } else if (messageType.equalsIgnoreCase("Message")) {
                type = MessageType.NORMAL;
            } else if (messageType.equalsIgnoreCase("Profile")) {
                type = MessageType.PROFILE;
            } else if (messageType.equalsIgnoreCase("History")) {
                type = MessageType.HISTORY;
            }

            lists.add(new Message(fromUser, toUser, new java.util.Date(createdDate
                .getTime()), content, type));

        }
        rs.close();
        con.close();

        return lists;
    }

}
