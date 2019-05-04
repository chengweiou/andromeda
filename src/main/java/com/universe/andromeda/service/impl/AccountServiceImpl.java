package com.universe.andromeda.service.impl;


import chengweiou.universe.blackhole.model.SearchCondition;
import com.universe.andromeda.dao.AccountDao;
import com.universe.andromeda.model.entity.Account;
import com.universe.andromeda.service.AccountService;
import com.universe.andromeda.init.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao dao;
    @Autowired
    private JwtUtil jwtUtil;

    public int save(Account e) {
        e.fillNotRequire();
        e.setPassword(jwtUtil.sign(e));
        e.createAt();
        e.updateAt();
        return dao.save(e);
    }

    public int delete(Account e) {
        return dao.delete(e);
    }

    @Override
    public int update(Account e) {
        return dao.update(e);
    }

    @Override
    public Account findById(Account e) {
        Account result = dao.findById(e);
        return result != null ? result : Account.NULL;
    }

    @Override
    public int count(SearchCondition searchCondition) {
        return dao.count(searchCondition);
    }
    @Override
    public List<Account> find(SearchCondition searchCondition) {
        searchCondition.setDefaultSort("createAt");
        return dao.find(searchCondition);
    }

    @Override
    public Account findByUsername(Account e) {
        Account result = dao.findByUsername(e);
        return result != null ? result : Account.NULL;
    }
}
