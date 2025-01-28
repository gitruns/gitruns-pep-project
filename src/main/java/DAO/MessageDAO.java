package DAO;

import Util.ConnectionUtil;
import static Util.CloseDBResources.closeQuietly;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Message;

interface IMessageDAO {
    Message insertMessage(Message message);

    Message selectMessageByID(int message_id);
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
}
