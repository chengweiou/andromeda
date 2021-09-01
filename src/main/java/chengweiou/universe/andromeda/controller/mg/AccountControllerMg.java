package chengweiou.universe.andromeda.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
@RequestMapping("mg")
public class AccountControllerMg {
    @Autowired
    private AccountService service;

    @PostMapping("/account")
    public Rest<Long> save(Account e) throws ParamException, FailException, ProjException {
        Valid.check("account.username|phone|email", e.getUsername(), e.getPhone(), e.getEmail()).are().notAllNull();
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
    public Rest<Boolean> update(Account e) throws ParamException, ProjException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.username | phone | email | wechat | weibo | google | facebook | password | active | person | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getPassword(), e.getPerson(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/account/person/{person.id}")
    public Rest<Boolean> updateByPerson(Account e) throws ParamException, ProjException {
        Valid.check("account.person.id", e.getPerson().getId()).is().notOf("0");
        Valid.check("account.username | phone | email | wechat | weibo | google | facebook | password | active | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getPassword(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        boolean success = service.updateByPerson(e) == 1 ;
        return Rest.ok(success);
    }

    @PutMapping("/account/{id}/person")
    public Rest<Boolean> updatePerson(Account e) throws ParamException, ProjException {
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

    @GetMapping("/account/person/{person.id}")
    public Rest<Account> findByPerson(Account e) throws ParamException {
        Valid.check("account.person.id", e.getPerson().getId()).is().notEmpty();
        Account data = service.findByPerson(e);
        return Rest.ok(data);
    }

    @GetMapping("/account/count")
    public Rest<Long> count(SearchCondition searchCondition, Account sample) {
        long count = service.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/account")
    public Rest<List<Account>> find(SearchCondition searchCondition, Account sample) {
        List<Account> list = service.find(searchCondition, sample);
        return Rest.ok(list);
    }
}
