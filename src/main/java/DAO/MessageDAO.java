package DAO;

import Util.ConnectionUtil;
import static Util.CloseDBResources.closeQuietly;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

interface IMessageDAO {
    Message insertMessage(Message message);

    Message selectMessageByID(int message_id);

    List<Message> selectAllMessages();

    boolean deleteMessageByID(int id);

    boolean updateMessageByID(int id, Message message);

    List<Message> selectMessagesPostedByAccountID(int account_id);
}

public class MessageDAO implements IMessageDAO {
    private Connection connection = ConnectionUtil.getConnection();

    @Override
    public Message insertMessage(Message message) {
        PreparedStatement ps = null;
        try {
            ps = connection
                    .prepareStatement(
                            "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return selectMessageByID(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(ps);
        }

        return null;
    }

    @Override
    public Message selectMessageByID(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection
                    .prepareStatement("select * from message where message_id=?");
            ps.setInt(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps);
        }
        return null;
    }

    @Override
    public List<Message> selectAllMessages() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        try {
            ps = connection
                    .prepareStatement("select * from message");

            rs = ps.executeQuery();
            if (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps);
        }
        return messages;
    }

    @Override
    public boolean deleteMessageByID(int id) {
        PreparedStatement ps = null;
        try {
            ps = connection
                    .prepareStatement("delete from message where message_id=?");
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(ps);
        }
        return false;
    }

    @Override
    public boolean updateMessageByID(int id, Message message) {
        PreparedStatement ps = null;
        try {
            ps = connection
                    .prepareStatement("update message set message_text=? where message_id=?");
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, id);

            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(ps);
        }
        return false;
    }

    @Override
    public List<Message> selectMessagesPostedByAccountID(int account_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        try {
            ps = connection
                    .prepareStatement("select * from message where posted_by=?");
            ps.setInt(1, account_id);

            rs = ps.executeQuery();
            if (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps);
        }
        return messages;
    }
}
