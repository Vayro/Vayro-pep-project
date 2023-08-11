package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // for database queries

    // retrieve all accounts
    public List<Account> getAllAccounts() throws SQLException {

        // return db connection
        Connection conn = ConnectionUtil.getConnection();

        // new accountlist
        List<Account> accounts = new ArrayList();

        // sql

        String sql = "SELECT * FROM account";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            accounts.add(account);
        }

        return accounts;
    }

    public Account insert(Account account) throws SQLException {

        System.out.println("Trying to insert using DAO: " + account.toString());

        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT into account(username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        preparedStatement.executeUpdate();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if (pkeyResultSet.next()) {
            int generated_account_id = (int) pkeyResultSet.getLong(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
        }

        return null;

    }

    public Account loginValidation(Account account) throws SQLException {

        String passedUsername = account.getUsername();
        String passedPassword = account.getPassword();

        System.out.println("Trying to insert using DAO: " + account.toString());
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account where username = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, account.getUsername());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {

            int retrievedID = rs.getInt(1);
            String retrievedUsername = rs.getString(2);
            String retrievedPassword = rs.getString(3);

            if (retrievedPassword == passedPassword) {

                System.out.println("passwords match");
                return new Account(retrievedID, retrievedUsername, retrievedPassword);
            } else {
                System.out.println("passwords do not match, returned null");
                ;
                return null;
            }

        }

        System.out.println("returned null");
        return null;

    }

}
