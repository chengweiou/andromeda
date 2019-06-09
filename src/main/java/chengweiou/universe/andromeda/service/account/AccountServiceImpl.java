package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDio dio;

    public void save(Account e) throws FailException {
        int count = dio.save(e);
        if (count != 1) throw new FailException();
    }

    @Override
    public void delete(Account e) throws FailException {
        int count = dio.delete(e);
        if (count != 1) throw new FailException();
    }

    @Override
    public int update(Account e) {
        return dio.update(e);
    }

    @Override
    public int updateByPerson(Account e) {
        return dio.updateByPerson(e);
    }

    @Override
    public Account findById(Account e) {
        return dio.findById(e);
    }

    @Override
    public Account login(Account e) throws ProjException {
        Account indb = dio.findByUsername(e);
        if (!indb.getActive()) throw new ProjException(ProjectRestCode.ACCOUNT_INACTIVE);
        if (!SecurityUtil.check(e.getPassword(), indb.getPassword())) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        return indb;
    }

    @Override
    public int count(SearchCondition searchCondition) {
        return dio.count(searchCondition);
    }

    @Override
    public List<Account> find(SearchCondition searchCondition) {
        return dio.find(searchCondition);
    }

    @Override
    public boolean checkUsername(Account e) {
        return dio.countByUsername(e) == 0;
    }
}
