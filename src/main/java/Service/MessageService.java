package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

interface IMessageService {
    Message insertMessage(Message message);

    List<Message> selectAllMessages();

    Message selectMessageByID(int id);

    Message deleteMessageByID(int id);

    Message patchMessageByID(int id, Message message);
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

    @Override
    public Message selectMessageByID(int id) {
        return dao.selectMessageByID(id);
    }

    @Override
    public Message deleteMessageByID(int id) {
        Message msg = dao.selectMessageByID(id);
        if (msg == null)
            return null;
        if (dao.deleteMessageByID(id))
            return msg;
        return null;
    }

    @Override
    public Message patchMessageByID(int id, Message message) {
        if (dao.patchMessageByID(id, message))
            return dao.selectMessageByID(id);
        return null;
    }
}
