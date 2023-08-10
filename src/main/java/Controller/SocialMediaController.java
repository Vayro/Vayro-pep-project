package Controller;

import org.h2.util.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        
        Javalin app = Javalin.create().start(8080);


        app.get("example-endpoint", this::exampleHandler);



        //register

        app.post("/register",this::register);


        //login


        app.post("/login",this::login);
        




        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }



    private void register(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Account account = om.readValue(jsonString,Account.class);
        String username=account.username;
        String password=account.getPassword();






        //ctx.json(account);
    }


    private void login(Context ctx) {
        ctx.json("sample text");
    }




}