package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
private AccountDAO dao;





//check if account username exists.
public boolean checkTaken(String username) throws SQLException{

dao = new AccountDAO();
List<Account> accounts = dao.getAllAccounts();



for(Account e : accounts)
{

    if(username==e.username){
        //if taken, return true
        return true;
    }



}

return false;
}






    
}
