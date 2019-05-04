package com.universe.andromeda.service;


import chengweiou.universe.blackhole.model.SearchCondition;
import com.universe.andromeda.model.entity.Account;

import java.util.List;

public interface AccountService {
    int save(Account e);
    int delete(Account e);

    int update(Account e);

    Account findById(Account e);

    int count(SearchCondition searchCondition);

    List<Account> find(SearchCondition searchCondition);

    Account findByUsername(Account e);
}
