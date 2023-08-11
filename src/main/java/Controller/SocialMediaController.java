package Controller;

import static org.mockito.ArgumentMatchers.intThat;

import java.sql.SQLException;
import java.util.List;

import org.h2.util.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {

        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);

        // register

        app.post("/register", this::register);

        // login

        app.post("/login", this::login);

        // create message
        app.post("/messages", this::createMessage);
        // retrieve all messages
        app.get("/messages", this::getAllMessage);
        // retrieve messages by ID
        app.get("/messages/{message_id}", this::getMessageByID);

        // retrieve messages by ID
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        // retrieve messages by ID
        app.patch("/messages/{message_id}", this::updateMessageByID);
        // retrieve all messages by account ID
        app.get("/accounts/{account_id}/messages", this::retrieveAllbyAccountID);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void register(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        System.out.println(om.readValue(jsonString, Account.class));
        Account account = om.readValue(jsonString, Account.class);
        System.out.println(jsonString);

        String username = account.getUsername();
        String password = account.getPassword();

        if (username.isBlank() || password.length() < 4) {

            ctx.status(400);
            System.out.println("Registration failed");

        } // check if username already exists
        else if (AccountService.checkTaken(username)) {
            ctx.status(400);
            System.out.println("Username Taken");
            // username taken
        } else {
            // insert account
            System.out.println("trying to insert");
            Account result = AccountService.insertDAO(account);
            ctx.status(200);
            ctx.json(result);

        }

    }

    private void login(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Account loginAttempt = om.readValue(jsonString, Account.class);
        String username = loginAttempt.getUsername();
        String password = loginAttempt.getPassword();

        if (username.isBlank() || password.length() < 4) {

            ctx.status(401);
            System.out.println("login failed");

        } // check if username already exists
        else if (AccountService.checkTaken(username)) {
            System.out.println("Username Exists");
            // compare passwords
            Account a = AccountService.attemptLogin(loginAttempt);

            if (a != null) {
                ctx.status(200);
                ctx.json(a);

            } else {
                ctx.status(401);
                System.out.println("login failed");

            }

        } else {
            ctx.status(401);
            System.out.println("login failed");
        }

    }

    private void createMessage(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Message m = om.readValue(jsonString, Message.class);
        String mText = m.getMessage_text();
        int userID = m.getPosted_by();

        if (mText.length() < 255 && mText.length() > 0) {
            Account account = AccountService.getAccountbyID(userID);

            if (account != null) {

                // post message

                Message posted = MessageService.insertDAO(m);

                if (posted != null) {

                    System.out.println("Posted Succesfully");

                    ctx.json(posted);
                    ctx.status(200);
                }

            } else {
                System.out.println("null account");
                ctx.status(400);

            }

        } else {

            System.out.println("Message length error");
            ctx.status(400);
        }

    }

    private void getAllMessage(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {

        List<Message> result = MessageService.getAllMessage();
        ctx.json(result);

        System.out.println(result.toString());
        ctx.status(200);
    }

    private void getMessageByID(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {

        System.out.println("ID: " + ctx.pathParam("message_id"));
        Integer inputID = Integer.parseInt(ctx.pathParam("message_id"));
        System.out.println(ctx.body() + " : " + inputID);

        Message result = MessageService.getMessageByID(inputID);
        if (result != null) {
            ctx.json(result);
        } else {

            ctx.json("");
        }

        ctx.status(200);
    }

    private void deleteMessageByID(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {
        System.out.println("ID: " + ctx.pathParam("message_id"));
        Integer inputID = Integer.parseInt(ctx.pathParam("message_id"));
        System.out.println(ctx.body() + " : " + inputID);

        Message result = MessageService.deleteMessageByID(inputID);
        if (result != null) {
            ctx.json(result);
        } else {

            ctx.json("");
        }

        ctx.status(200);

    }

    private void updateMessageByID(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {
        System.out.println("ID: " + ctx.pathParam("message_id"));
        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Message m = om.readValue(jsonString, Message.class);
        m.setMessage_id(Integer.parseInt(ctx.pathParam("message_id")));
        System.out.println("New Messege Text: " + m.getMessage_text());
        String mText = m.getMessage_text();

        if (mText.length() < 255 && mText.length() > 0) {

            Message result = MessageService.updateMessageByID(m);

            if (result != null) {
                ctx.json(result);
                ctx.status(200);
            } else {

                ctx.status(400);
            }

        } else {
            ctx.status(400);

        }

    }

    private void retrieveAllbyAccountID(Context ctx)
            throws SQLException, JsonMappingException, JsonProcessingException {

        int accountID = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> resultList = MessageService.retrieveAllbyAccountID(accountID);

        ctx.json(resultList);

        ctx.status(200);

    }

}
