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
            System.out.println("Returning: " + generated_account_id + " " + message.getPosted_by() + " "
                    + message.getMessage_text() + " " + message.getTime_posted_epoch());
            return new Message(generated_account_id, message.getPosted_by(), message.getMessage_text(),
                    message.getTime_posted_epoch());
        }

        return null;

    }

    public List<Message> getAllMessage() throws SQLException {
        // retrieve all messages from database
        Connection conn = ConnectionUtil.getConnection();
        List<Message> resultList = new ArrayList<Message>();

        // sql

        String sql = "SELECT * FROM message";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Message m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            resultList.add(m);
        }

        return resultList;
    }

    public Message getMessageByID(Integer inputID) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        Message result;
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, inputID);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next() == false) {
            System.out.println("no results");
            return null;
        } else {

            do {

                result = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                System.out.println(result.toString());

                return result;

            } while (rs.next());
        }

    }

    public int deleteMessageByID(Integer inputID) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, inputID);
        return preparedStatement.executeUpdate();

    }

    public int updateMessageByID(Message m) throws SQLException {

        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text=? WHERE message_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(2, m.getMessage_id());
        preparedStatement.setString(1, m.getMessage_text());
        return preparedStatement.executeUpdate();

    }

    public List<Message> retrieveAllbyAccountID(int accountID) throws SQLException {

        Connection conn = ConnectionUtil.getConnection();

        // new messageList
        List<Message> resultList = new ArrayList();

        // sql

        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, accountID);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Message m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            resultList.add(m);
        }

        return resultList;

    }

}
