package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

import java.util.List;

public interface AccountService {
    void save(Account e) throws FailException, ProjException;
    void delete(Account e) throws FailException;

    long update(Account e);
    long updateByPerson(Account e);

    Account findById(Account e);

    Account login(Account e) throws ProjException;

    long count(SearchCondition searchCondition, Account sample);
    List<Account> find(SearchCondition searchCondition, Account sample);

    long countByUsername(Account e);
}
