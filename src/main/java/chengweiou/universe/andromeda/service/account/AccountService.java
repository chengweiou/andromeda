package chengweiou.universe.andromeda.service.account;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface AccountService {
    void save(Account e) throws FailException, ProjException;
    void delete(Account e) throws FailException;

    long update(Account e) throws ProjException;
    long updateByPerson(Account e) throws ProjException;

    Account findById(Account e);
    Account findByPerson(Account e);

    long countByLoginUsername(Account e);
    Account findByLoginUsername(Account e);
    Account login(Account e) throws ProjException;

    long countByUsernameOfOther(Account e);
    long countByPhoneOfOther(Account e);
    long countByEmailOfOther(Account e);
    
    /**
     * check code needs token+code
     * @param twofa
     * @return
     * @throws ProjException
     */
    Account findAfterCheckCode(Twofa twofa) throws ProjException;

    long count(SearchCondition searchCondition, Account sample);
    List<Account> find(SearchCondition searchCondition, Account sample);

}
