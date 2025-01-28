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
        app.get("messages", this::getMessages);
        app.get("messages/{id}", this::getMessageById);
        app.delete("messages/{id}", this::deleteMessageById);
        app.patch("messages/{id}", this::patchMessageById);
        app.get("accounts/{account_id}/messages", this::getMessageByAccountID);

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
            ctx.status(400).json("");
            return;
        }

        Account account = ctx.bodyAsClass(Account.class);
        if (accountService.register(account)) {
            ctx.status(200).json("");
            ctx.json(accountService.getAccountByUsername(account.getUsername()));
        } else {
            ctx.status(400).json("");
        }
    }

    private void login(Context ctx) {
        if (ctx.body().isEmpty()) {
            ctx.status(400).json("");
            return;
        }

        Account account = ctx.bodyAsClass(Account.class);
        if (accountService.verify(account)) {
            ctx.status(200).json("");
            ctx.json(accountService.getAccountByUsername(account.getUsername()));
        } else {
            ctx.status(401).json("");
        }
    }

    private void messages(Context ctx) {
        if (ctx.body().isEmpty()) {
            ctx.status(400).json("");
            return;
        }

        Message msg = msgService.insertMessage(ctx.bodyAsClass(Message.class));
        if (msg != null) {
            ctx.status(200).json(msg);
        } else {
            ctx.status(400).json("");
        }
    }

    private void getMessages(Context ctx) {
        ctx.json(msgService.selectAllMessages());
    }

    private void getMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message msg = msgService.selectMessageByID(id);
        if (msg == null) {
            ctx.status(200).json("");
        } else {
            ctx.json(msg);
        }
    }

    private void deleteMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message msg = msgService.deleteMessageByID(id);
        if (msg == null) {
            ctx.status(200).json("");
        } else {
            ctx.json(msg);
        }
    }

    private void patchMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message msg = msgService.updateMessageByID(id, ctx.bodyAsClass(Message.class));
        if (msg == null) {
            ctx.status(400).json("");
        } else {
            ctx.json(msg);
        }
    }

    private void getMessageByAccountID(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(msgService.selectMessageByAccountID(id));
    }

}