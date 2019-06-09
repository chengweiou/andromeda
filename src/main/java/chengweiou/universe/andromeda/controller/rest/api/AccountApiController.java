package chengweiou.universe.andromeda.controller.rest.api;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class AccountApiController {
    @Autowired
    private AccountService service;

    @PostMapping("/account")
    public Rest<Long> save(Account e) throws ParamException, FailException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        Valid.check("account.password", e.getPassword()).is().notEmpty();
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/account/{id}")
    public Rest<Boolean> delete(Account e) throws ParamException, FailException {
        Valid.check("account.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }

    @PutMapping("/account/{id}")
    public Rest<Boolean> update(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.username | account.person | account.extra", e.getUsername(), e.getPerson(), e.getExtra()).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/account/person/{person.id}")
    public Rest<Boolean> updateByPerson(Account e) throws ParamException {
        Valid.check("account.person.id", e.getPerson().getId()).is().notOf("0");
        Valid.check("account.active | account.extra", e.getActive(), e.getExtra()).are().notAllNull();
        boolean success = service.updateByPerson(e) > 0;
        return Rest.ok(success);
    }

    @PutMapping("/account/{id}/person")
    public Rest<Boolean> updatePerson(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.person.id", e.getPerson().getId()).is().notOf("0");
        // will update person and acitve=true together
        boolean success = service.update(Builder.set("id", e.getId()).set("person", e.getPerson()).set("active", true).to(new Account())) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/account/{id}")
    public Rest<Account> findById(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        Account data = service.findById(e);
        return Rest.ok(data);
    }

    @GetMapping("/account/count")
    public Rest<Integer> count(SearchCondition searchCondition) {
        Integer count = service.count(searchCondition);
        return Rest.ok(count);
    }

    @GetMapping("/account")
    public Rest<List<Account>> find(SearchCondition searchCondition) {
        List<Account> list = service.find(searchCondition);
        return Rest.ok(list);
    }
}
