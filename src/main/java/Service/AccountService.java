package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private static AccountDAO dao;

    // check if account username exists.
    public static boolean checkTaken(String username) throws SQLException {

        dao = new AccountDAO();
        List<Account> accounts = dao.getAllAccounts();

        for (Account e : accounts) {

            if (username.equals(e.getUsername())) {
                // if taken, return true
                System.out.println("username taken, returning 'true'");
                return true;
            }

        }
        System.out.println("username not taken, returning 'false'");
        return false;
    }

    public static Account insertDAO(Account account) throws SQLException {

        System.out.println("Trying to insert: " + account.toString());

        dao = new AccountDAO();

        Account result = dao.insert(account);

        return result;
    }

}
