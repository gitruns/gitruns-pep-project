package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

interface IMessageService {
    Message insertMessage(Message message);

    List<Message> selectAllMessages();

    /*
     * Account getAccountByUsername(String username);
     * 
     * boolean verify(Account account);
     */
}

public class MessageService implements IMessageService {
    private MessageDAO dao;
    private AccountDAO daoa;

    public MessageService() {
        this(new MessageDAO(), new AccountDAO());
    }

    public MessageService(MessageDAO dao, AccountDAO daoa) {
        this.dao = dao;
        this.daoa = daoa;
    }

    @Override
    public Message insertMessage(Message message) {
        int msgLength = message.getMessage_text().length();
        if (msgLength < 1 || msgLength > 255)
            return null;

        return dao.insertMessage(message);
    }

    @Override
    public List<Message> selectAllMessages() {
        return dao.selectAllMessages();
    }

    // @Override
    // public Account getAccountByUsername(String username) {
    // return dao.getAccountByUsername(username);
    // }

    // @Override
    // public boolean verify(Account account) {
    // Account a = dao.getAccountByUsername(account.getUsername());
    // return a != null && account.getPassword().equals(a.getPassword());
    // }
}
