package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
private static MessageDAO dao;



    public static Message insertDAO(Message message) throws SQLException {

        System.out.println("Trying to insert: " + message.toString());

        dao = new MessageDAO();

        Message result = dao.insert(message);

        return result;
    }







    
}
