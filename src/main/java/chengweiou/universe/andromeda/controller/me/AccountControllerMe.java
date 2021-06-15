package chengweiou.universe.andromeda.controller.me;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
@RequestMapping("me")
public class AccountControllerMe {
    @Autowired
    private AccountService service;

    @PutMapping("/account")
    public Rest<Boolean> update(Account e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, UnauthException, ProjException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        Valid.check("account.username | phone | email | wechat | weibo | google | facebook | active | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        
        Account indb = service.findByPerson(loginAccount);
        if (!indb.getPerson().getId().equals(loginAccount.getPerson().getId())) throw new UnauthException();
        e.setId(indb.getId());
        e.setPerson(null);
        e.setPassword(null);
        e.setActive(null);
        e.setExtra(null);
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/account/password")
    public Rest<Boolean> updatePassword(Account e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, ProjException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        Account indb = service.findByPerson(loginAccount);
        boolean success = SecurityUtil.check(e.getOldPassword(), indb.getPassword());
        if (!success) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        success = service.update(Builder.set("id", indb.getId()).set("password", e.getPassword()).to(new Account())) == 1;
        return Rest.ok(true);
    }

    @GetMapping("/account")
    public Rest<Account> find(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Account indb = service.findByPerson(loginAccount);
        return Rest.ok(indb);
    }
}
