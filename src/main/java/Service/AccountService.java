package Service;

import DAO.AccountDAO;
import Model.Account;

interface IAccountService {
    boolean register(Account account);

    Account getAccountByUsername(String username);

    boolean verify(Account account);
}

public class AccountService implements IAccountService {
    private AccountDAO dao;

    public AccountService() {
        this(new AccountDAO());
    }

    public AccountService(AccountDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean register(Account account) {
        if (account.getUsername().length() > 0 && account.getPassword().length() >= 4)
            return dao.register(account);
        return false;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return dao.getAccountByUsername(username);
    }

    @Override
    public boolean verify(Account account) {
        return account.getPassword().equals(dao.getAccountByUsername(account.getUsername()).getPassword());
    }
}
