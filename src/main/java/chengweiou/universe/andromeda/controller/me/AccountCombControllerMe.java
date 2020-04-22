package chengweiou.universe.andromeda.controller.me;


import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.AccountComb;
import chengweiou.universe.andromeda.model.entity.AccountType;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("me")
public class AccountCombControllerMe {
    @Autowired
    private AccountService service;

    @GetMapping("/accountComb")
    public Rest<AccountComb> find(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        List<Account> list = service.find(new SearchCondition(), Builder.set("person", loginAccount.getPerson()).to(new Account()));
        AccountComb data = AccountComb.from(list);
        return Rest.ok(data);
    }

    @PutMapping("/accountComb")
    public Rest<Boolean> update(AccountComb e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        e.setPerson(loginAccount.getPerson());
        e.setPassword(null);
        List<Account> list = AccountComb.toUpdate(e);
        list.forEach(each -> service.updateByPersonAndType(each));
        return Rest.ok(true);
    }
    @PutMapping("/accountComb/password")
    public Rest<Boolean> updatePassword(AccountComb e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, ProjException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        e.setPerson(loginAccount.getPerson());
        List<Account> list = service.find(new SearchCondition(), Builder.set("person", loginAccount.getPerson()).to(new Account()));
        // tip: different account but same password
        Account indb = list.stream().filter(each ->
                each.getType()==AccountType.NORMAL || each.getType()==AccountType.PHONE || each.getType()==AccountType.EMAIL
                ).findFirst().get();
        boolean success = SecurityUtil.check(e.getOldPassword(), indb.getPassword());
        if (!success) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        list.forEach(each -> service.update(Builder.set("id", indb.getId()).set("password", e.getPassword()).to(new Account())));
        return Rest.ok(true);
    }
}