package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {









    public Message insert(Message message) throws SQLException {

        System.out.println("Trying to insert using DAO: " + message.toString());

        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT into message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());
        preparedStatement.executeUpdate();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if (pkeyResultSet.next()) {
            int generated_account_id = (int) pkeyResultSet.getLong(1);
            System.out.println("Returning: " + generated_account_id + " " + message.getPosted_by() + " " + message.getMessage_text() + " " + message.getTime_posted_epoch());
            return new Message(generated_account_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }

        return null;

    }










    
}
