package chengweiou.universe.andromeda.service.account;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface AccountService {
    void save(Account e) throws FailException, ProjException;
    void delete(Account e) throws FailException;

    long update(Account e);
    long updateByPerson(Account e);
    long updateByPersonAndType(Account e);

    Account findById(Account e);

    Account login(Account e) throws ProjException;

    /**
     * check code needs token+code
     * @param twofa
     * @return
     * @throws ProjException
     */
    Account findAfterCheckCode(Twofa twofa) throws ProjException;

    long count(SearchCondition searchCondition, Account sample);
    List<Account> find(SearchCondition searchCondition, Account sample);

    long countByUsername(Account e);
}
