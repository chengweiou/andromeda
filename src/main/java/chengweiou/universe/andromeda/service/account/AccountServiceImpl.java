package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.dao.AccountDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao dao;

    public int save(Account e) {
        e.fillNotRequire();
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        e.createAt();
        e.updateAt();
        return dao.save(e);
    }

    public int delete(Account e) {
        return dao.delete(e);
    }

    @Override
    public int update(Account e) {
        return dao.update(e);
    }

    @Override
    public int updateByPerson(Account e) {
        return dao.updateByPerson(e);
    }

    @Override
    public Account findById(Account e) {
        Account result = dao.findById(e);
        return result != null ? result : Account.NULL;
    }

    @Override
    public int count(SearchCondition searchCondition) {
        return dao.count(searchCondition);
    }
    @Override
    public List<Account> find(SearchCondition searchCondition) {
        searchCondition.setDefaultSort("createAt");
        return dao.find(searchCondition);
    }

    @Override
    public Account findByUsername(Account e) {
        Account result = dao.findByUsername(e);
        return result != null ? result : Account.NULL;
    }
}
