package Controller;

import Model.Account;
import Model.Message;
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
    private Service.AccountService accountService = new Service.AccountService();
    private Service.MessageService msgService = new Service.MessageService();

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
        app.post("register", this::register);
        app.post("login", this::login);
        app.post("messages", this::messages);
        app.get("messages", this::messages);

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

    private void register(Context ctx) {
        if (ctx.body().isEmpty()) {
            ctx.status(400);
            return;
        }

        Account account = ctx.bodyAsClass(Account.class);
        if (accountService.register(account)) {
            ctx.status(200);
            ctx.json(accountService.getAccountByUsername(account.getUsername()));
        } else {
            ctx.status(400);
        }
    }

    private void login(Context ctx) {
        if (ctx.body().isEmpty()) {
            ctx.status(400);
            return;
        }

        Account account = ctx.bodyAsClass(Account.class);
        if (accountService.verify(account)) {
            ctx.status(200);
            ctx.json(accountService.getAccountByUsername(account.getUsername()));
        } else {
            ctx.status(401);
        }
    }

    private void messages(Context ctx) {
        if (ctx.body().isEmpty()) {
            ctx.status(400);
            return;
        }

        Message msg = msgService.insertMessage(ctx.bodyAsClass(Message.class));
        if (msg != null) {
            ctx.status(200);
            ctx.json(msg);
        } else {
            ctx.status(400);
        }
    }

    private void getMessages(Context ctx) {
        ctx.json(msgService.selectAllMessages());
    }
}