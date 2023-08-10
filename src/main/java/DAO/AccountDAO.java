package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    //for database queries
   


   // retrieve all accounts
   public List<Account> getAllAccounts() throws SQLException{

    //return db connection
    Connection conn = ConnectionUtil.getConnection();

    //new accountlist
    List<Account> accounts = new ArrayList();

    //sql

    String sql="SELECT * FROM account";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    ResultSet rs = preparedStatement.executeQuery();
    while(rs.next()){
        Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        accounts.add(account);
    }





    return accounts;
   }




    
}
