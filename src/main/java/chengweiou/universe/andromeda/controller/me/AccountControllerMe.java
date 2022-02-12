package chengweiou.universe.andromeda.controller.me;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountDio;
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
    @Autowired
    private AccountDio dio;

    @PutMapping("/account")
    public Rest<Boolean> update(Account e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, UnauthException, ProjException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("account.username | phone | email | wechat | weibo | google | facebook | active | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getActive(), e.getExtra()
            ).are().notAllNull();

        Account indb = dio.findByKey(loginAccount);
        if (indb.getPerson().getId().longValue() != loginAccount.getPerson().getId()) throw new UnauthException();
        e.setId(indb.getId());
        e.setPerson(null);
        e.setPassword(null);
        e.setActive(null);
        e.setExtra(null);
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/account/password")
    public Rest<Boolean> changePassword(Account e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, ProjException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        e.setPerson(loginAccount.getPerson());
        long count = service.changePassword(e);

        return Rest.ok(count == 1);
    }

    @GetMapping("/account")
    public Rest<Account> find(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Account indb = dio.findByKey(loginAccount);
        return Rest.ok(indb);
    }
}
