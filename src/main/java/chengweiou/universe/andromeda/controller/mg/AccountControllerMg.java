package chengweiou.universe.andromeda.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountDio;
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
    @Autowired
    private AccountDio dio;

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
    public Rest<Boolean> update(Account e) throws ParamException, ProjException, FailException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.username | phone | email | wechat | weibo | google | facebook | password | active | person | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getPassword(), e.getPerson(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/account/person/{personId}")
    public Rest<Boolean> updateByPerson(Account e, @PathVariable Long personId) throws ParamException, ProjException, FailException {
        Valid.check("personId", personId).is().positive();
        Valid.check("account.username | phone | email | wechat | weibo | google | facebook | password | active | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getPassword(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        e.setPerson(Builder.set("id", personId).to(new Person()));
        boolean success = service.updateByKey(e) == 1 ;
        return Rest.ok(success);
    }

    @PutMapping("/account/{id}/person")
    public Rest<Boolean> updatePerson(Account e) throws ParamException, ProjException, FailException {
        Valid.check("account.id", e.getId()).is().positive();
        Valid.check("account.person.id", e.getPerson().getId()).is().positive();
        // will update person and acitve=true together
        boolean success = dio.update(Builder.set("id", e.getId()).set("person", e.getPerson()).set("active", true).to(new Account())) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/account/{id}")
    public Rest<Account> findById(Account e) throws ParamException {
        Valid.check("account.id", e.getId()).is().positive();
        Account data = dio.findById(e);
        return Rest.ok(data);
    }

    @GetMapping("/account/person/{personId}")
    public Rest<Account> findByPerson(Account e, @PathVariable Long personId) throws ParamException {
        Valid.check("personId", personId).is().positive();
        e.setPerson(Builder.set("id", personId).to(new Person()));
        Account data = dio.findByKey(e);
        return Rest.ok(data);
    }

    @GetMapping("/account/count")
    public Rest<Long> count(SearchCondition searchCondition, Account sample) {
        long count = dio.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/account")
    public Rest<List<Account>> find(SearchCondition searchCondition, Account sample) {
        List<Account> list = dio.find(searchCondition, sample);
        return Rest.ok(list);
    }
}
