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

    public static List<Message> getAllMessage() throws SQLException {
        dao = new MessageDAO();
        List<Message> result = dao.getAllMessage();

        return result;
    }

    public static Message getMessageByID(Integer inputID) throws SQLException {

        dao = new MessageDAO();
        Message result = dao.getMessageByID(inputID);

        return result;
    }

    public static Message deleteMessageByID(Integer inputID) throws SQLException {

        dao = new MessageDAO();
        // check if message exists
        Message result = dao.getMessageByID(inputID);

        // attempt to delete message
        if (dao.deleteMessageByID(inputID) > 0) {

            System.out.println("succesfully deleted");

        }
        return result;
    }

    public static Message updateMessageByID(Message m) throws SQLException {
        dao = new MessageDAO();
        // check if message exists

        if (dao.updateMessageByID(m) > 0) {
            System.out.println("message patched: " + getMessageByID(m.message_id).toString());
            return getMessageByID(m.message_id);
        }

        return null;
    }

    public static List<Message> retrieveAllbyAccountID(int accountID) throws SQLException {

        dao = new MessageDAO();
        List<Message> resultList = dao.retrieveAllbyAccountID(accountID);

        return resultList;
    }

}
