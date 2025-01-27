package DAO;

import Util.ConnectionUtil;
import static Util.CloseDBResources.closeQuietly;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;

interface IAccountDAO {
    boolean register(Account account);

    Account getAccountByUsername(String username);
}

public class AccountDAO implements IAccountDAO {
    private Connection connection = ConnectionUtil.getConnection();

    @Override
    public boolean register(Account account) {
        PreparedStatement ps = null;
        try {
            ps = connection
                    .prepareStatement("insert into account (username, password) values (?, ?)");
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            if (ps.executeUpdate() == 1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(ps);
        }
        return false;
    }

    @Override
    public Account getAccountByUsername(String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection
                    .prepareStatement("select * from account where username=?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps);
        }
        return null;
    }
}
