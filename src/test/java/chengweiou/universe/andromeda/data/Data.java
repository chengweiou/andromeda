package chengweiou.universe.andromeda.data;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Data {
    @Autowired
    private AccountDio accountDio;
    public List<Account> accountList;

    @Autowired
    private LoginRecordDio loginRecordDio;
    public List<LoginRecord> loginRecordList;

    public void init() {
        accountList = accountDio.find(new SearchCondition()).stream().sorted(Comparator.comparingLong(Account::getId)).collect(Collectors.toList());
        accountList.forEach(e -> e.setPassword("123"));
        loginRecordList = loginRecordDio.find(new SearchCondition()).stream().sorted(Comparator.comparingLong(LoginRecord::getId)).collect(Collectors.toList());
    }
}
