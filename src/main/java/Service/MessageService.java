package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

interface IMessageService {
    Message insertMessage(Message message);

    List<Message> selectAllMessages();

    Message selectMessageByID(int id);

    Message deleteMessageByID(int id);

    Message updateMessageByID(int id, Message message);

    List<Message> selectMessageByAccountID(int account_id);
}

public class MessageService implements IMessageService {
    private MessageDAO dao;

    public MessageService() {
        this(new MessageDAO());
    }

    public MessageService(MessageDAO dao) {
        this.dao = dao;
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
    public Message updateMessageByID(int id, Message message) {
        int msgLength = message.getMessage_text().length();
        if (msgLength < 1 || msgLength > 255)
            return null;

        if (dao.updateMessageByID(id, message))
            return dao.selectMessageByID(id);
        return null;
    }

    @Override
    public List<Message> selectMessageByAccountID(int account_id) {
        return dao.selectMessagesPostedByAccountID(account_id);
    }
}
