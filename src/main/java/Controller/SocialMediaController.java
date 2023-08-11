package Controller;

import java.sql.SQLException;

import org.h2.util.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import Model.Account;
import Service.AccountService;
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

            ctx.status(400);
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
                ctx.status(400);
                System.out.println("login failed");

            }

        } else {
            ctx.status(400);
            System.out.println("login failed");
        }

    }

}
