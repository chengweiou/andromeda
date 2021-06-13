package chengweiou.universe.andromeda.service.accountrecover;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.AccountRecover;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface AccountRecoverService {
    void save(AccountRecover e) throws FailException, ProjException;
    void delete(AccountRecover e) throws FailException;

    long update(AccountRecover e);

    AccountRecover findById(AccountRecover e);

    long countByPerson(AccountRecover e);
    AccountRecover findByPerson(AccountRecover e);

    long count(SearchCondition searchCondition, AccountRecover sample);
    List<AccountRecover> find(SearchCondition searchCondition, AccountRecover sample);

    /**
     * 
     * @param id, code
     * @return
     * @throws ProjException
     */
    AccountRecover findByActiveCode(AccountRecover e) throws ProjException;
}
