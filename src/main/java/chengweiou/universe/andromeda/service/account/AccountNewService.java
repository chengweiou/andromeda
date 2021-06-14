package chengweiou.universe.andromeda.service.account;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.AccountNew;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface AccountNewService {
    void save(AccountNew e) throws FailException, ProjException;
    void delete(AccountNew e) throws FailException;

    long update(AccountNew e) throws ProjException;
    long updateByPerson(AccountNew e) throws ProjException;

    AccountNew findById(AccountNew e);

    long countByLoginUsername(AccountNew e);
    AccountNew login(AccountNew e) throws ProjException;

    long countByUsernameOfOther(AccountNew e);
    long countByPhoneOfOther(AccountNew e);
    long countByEmailOfOther(AccountNew e);
    
    /**
     * check code needs token+code
     * @param twofa
     * @return
     * @throws ProjException
     */
    AccountNew findAfterCheckCode(Twofa twofa) throws ProjException;

    long count(SearchCondition searchCondition, AccountNew sample);
    List<AccountNew> find(SearchCondition searchCondition, AccountNew sample);

}
