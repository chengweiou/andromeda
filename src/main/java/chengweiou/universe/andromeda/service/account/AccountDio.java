package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.dao.AccountDao;
import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AccountDio {
    @Autowired
    private AccountDao dao;

    public void save(Account e) throws ProjException, FailException {
        long count = dao.countByUsername(e);
        if (count != 0) throw new ProjException("dup key: " + e.getUsername() + " exists", ProjectRestCode.EXISTS);
        e.fillNotRequire();
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        e.createAt();
        e.updateAt();
        count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(Account e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
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
