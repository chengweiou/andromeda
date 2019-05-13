package chengweiou.universe.andromeda.controller.rest.api;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.model.SearchCondition;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.AccountService;
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
        int count = service.save(e);
        if (count != 1) throw new FailException();
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/account/{id}")
    public Rest<Boolean> delete(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        boolean success = service.delete(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/account/{id}")
    public Rest<Boolean> update(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.name", e.getUsername(), e.getPersonId(), e.getExtra()).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/account/{id}")
    public Rest<Account> findById(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        Account data = service.findById(e);
        return Rest.ok(data);
    }

    @GetMapping("/me")
    public Rest<Account> me(Auth auth, @RequestAttribute("personId")String personId) {
        // 还不确定
        return Rest.ok(personId);
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
