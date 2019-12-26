/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.context.DBContext;
import com.entity.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Leanh
 */
public class UserDAO {

    //return a list of available users in the system
    public List<Client> selectAll() throws Exception {
        String query = " select * from users ";
        Connection con = new DBContext().getConnection();
        PreparedStatement ps = con.prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        List<Client> clients = new ArrayList<>();
        while (rs.next()) {
            String userName = rs.getString("username");
            String displayName = rs.getString("displayname");
            clients.add(new Client(userName, displayName));
        }
        rs.close();
        con.close();
        return clients;
    }

    public void addUser(Client user) throws Exception {
        /*insert code for inserting a new User to table User*/
        List<Client> clients = selectAll();
        Optional<Client> returnUser = clients.stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(user.getUsername()))
            .findFirst();

        if (!returnUser.isPresent()) {
            String insert = "insert into users(username, displayname) values(?, ?)";
            Connection con = new DBContext().getConnection();
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getDisplayName());
            ps.executeUpdate();
            ps.close();
            con.close();
        }

    }

    public void updateUserProfilePicture(String fromUser, String imagePath) throws Exception {
        String query = " update users set picture = '"  +imagePath 
            + "' where username = '" + fromUser +"'";
        Connection con = new DBContext().getConnection();
        PreparedStatement ps = con.prepareStatement(query);

        ps.executeUpdate();
        ps.close();
        con.close();
    }

}
