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

    public int update(Account e) {
        return dao.update(e);
    }

    public int updateByPerson(Account e) {
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

    public int count(SearchCondition searchCondition) {
        return dao.count(searchCondition);
    }

    public List<Account> find(SearchCondition searchCondition) {
        searchCondition.setDefaultSort("createAt");
        return dao.find(searchCondition);
    }

    public int countByUsername(Account e) {
        return dao.countByUsername(e);
    }
}
