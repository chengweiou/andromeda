package chengweiou.universe.andromeda.controller.me;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("me")
public class AccountControllerMe {
    @Autowired
    private AccountService service;

    @PutMapping("/account/{id}")
    public Rest<Boolean> update(Account e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, UnauthException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.username | account.password",
                e.getUsername(), e.getPassword()).are().notAllNull();
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        List<Account> list = service.find(new SearchCondition(), Builder.set("person", loginAccount.getPerson()).to(new Account()));
        long matchCount = list.stream().filter(each -> e.getId() == each.getId()).count();
        if (matchCount != 1) throw new UnauthException();
        e.setPerson(null);
        e.setActive(null);
        e.setExtra(null);
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/account/count")
    public Rest<Long> count(SearchCondition searchCondition, Account sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        sample.setPerson(loginAccount.getPerson());
        long count = service.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/account")
    public Rest<List<Account>> find(SearchCondition searchCondition, Account sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        sample.setPerson(loginAccount.getPerson());
        List<Account> list = service.find(searchCondition, sample);
        return Rest.ok(list);
    }
}
