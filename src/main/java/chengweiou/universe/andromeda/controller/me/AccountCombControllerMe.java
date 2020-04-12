package chengweiou.universe.andromeda.controller.me;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.AccountComb;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.exception.ParamException;
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
    public Rest<AccountComb> findByPerson(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        List<Account> list = service.find(new SearchCondition(), Builder.set("person", loginAccount.getPerson()).to(new Account()));
        AccountComb data = AccountComb.from(list);
        return Rest.ok(data);
    }

    @PutMapping("/accountComb")
    public Rest<Boolean> updateByPerson(AccountComb e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        e.setPerson(loginAccount.getPerson());
        List<Account> list = AccountComb.toUpdate(e);
        list.forEach(each -> service.updateByPersonAndType(each));
        return Rest.ok(true);
    }

}