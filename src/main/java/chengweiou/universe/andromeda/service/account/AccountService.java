package chengweiou.universe.andromeda.service.account;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;

import java.util.List;

public interface AccountService {
    int save(Account e);
    int delete(Account e);

    int update(Account e);

    int updateByPerson(Account e);

    Account findById(Account e);

    Account findByUsername(Account e);

    int count(SearchCondition searchCondition);

    List<Account> find(SearchCondition searchCondition);
}
