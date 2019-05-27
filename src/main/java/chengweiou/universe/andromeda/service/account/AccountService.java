package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

import java.util.List;

public interface AccountService {
    int save(Account e) throws FailException;
    int delete(Account e);

    int update(Account e);

    int updateByPerson(Account e);

    Account findById(Account e);

    Account login(Account e) throws ProjException;

    int count(SearchCondition searchCondition);

    List<Account> find(SearchCondition searchCondition);
}
