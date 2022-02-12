package chengweiou.universe.andromeda.data;


import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.andromeda.service.twofa.TwofaDio;

@Component
public class Data {
    // @Autowired
    // private AccountDio accountDio;
    // public List<Account> accountList;
    @Autowired
    private AccountDio accountDio;
    public List<Account> accountList;

    @Autowired
    private LoginRecordDio loginRecordDio;
    public List<LoginRecord> loginRecordList;

    @Autowired
    private TwofaDio twofaDio;
    public List<Twofa> twofaList;

    @Autowired
    private CodeSendRecordDio codeSendRecordDio;
    public List<CodeSendRecord> codeSendRecordList;

    @Autowired
    private AccountRecoverDio accountRecoverDio;
    public List<AccountRecover> accountRecoverList;

    public void init() {
        // accountList = accountDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Account::getId)).toList();
        // accountList.forEach(e -> e.setPassword("123"));
        accountList = accountDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Account::getId)).toList();
        accountList.forEach(e -> e.setPassword("123"));
        loginRecordList = loginRecordDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(LoginRecord::getId)).toList();
        twofaList = twofaDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Twofa::getId)).toList();
        codeSendRecordList = codeSendRecordDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(CodeSendRecord::getId)).toList();
        accountRecoverList =  accountRecoverDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(AccountRecover::getId)).toList();
    }
}
