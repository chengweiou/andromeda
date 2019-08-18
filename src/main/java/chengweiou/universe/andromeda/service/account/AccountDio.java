package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.dao.AccountDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AccountDio {
    @Autowired
    private AccountDao dao;

    public long save(Account e) {
        e.fillNotRequire();
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        e.createAt();
        e.updateAt();
        return dao.save(e);
    }

    public long delete(Account e) {
        return dao.delete(e);
    }

    public long update(Account e) {
        return dao.update(e);
    }

    public long updateByPerson(Account e) {
        return dao.updateByPerson(e);
    }

    public Account findById(Account e) {
        Account result = dao.findById(e);
        return result != null ? result : Account.NULL;
    }

    public Account findByUsername(Account e) {
        Account result = dao.findByUsername(e);
        return result != null ? result : Account.NULL;
    }

    public long count(SearchCondition searchCondition) {
        return dao.count(searchCondition);
    }

    public List<Account> find(SearchCondition searchCondition) {
        searchCondition.setDefaultSort("createAt");
        return dao.find(searchCondition);
    }

    public long countByUsername(Account e) {
        return dao.countByUsername(e);
    }
}
