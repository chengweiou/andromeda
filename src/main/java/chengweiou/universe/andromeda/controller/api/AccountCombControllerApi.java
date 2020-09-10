package chengweiou.universe.andromeda.controller.api;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.AccountComb;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("mg")
public class AccountCombControllerApi {
    @Autowired
    private AccountService service;

    @GetMapping("/accountComb/person/{person.id}")
    public Rest<AccountComb> findByPerson(AccountComb e) throws ParamException {
        Valid.check("account.person.id", e.getPerson().getId()).is().notEmpty();
        List<Account> list = service.find(new SearchCondition(), Builder.set("person",e.getPerson()).to(new Account()));
        AccountComb data = AccountComb.from(list);
        return Rest.ok(data);
    }

    @PutMapping("/accountComb")
    public Rest<Boolean> updateByPerson(AccountComb e) throws ParamException {
        List<Account> list = AccountComb.toUpdate(e);
        list.forEach(each -> service.updateByPersonAndType(each));
        return Rest.ok(true);
    }

}