package chengweiou.universe.andromeda.controller.me;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.phonemsg.PhoneMsgService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
@RequestMapping("me")
public class AccountRecoverControllerMe {
    @Autowired
    private AccountRecoverDio dio;
    @Autowired
    private PhoneMsgService phoneMsgService;

    @PutMapping("/accountRecover")
    public Rest<Boolean> update(AccountRecover e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, UnauthException, ProjException, FailException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("accountRecover.phone | email | q1 | q2 | q3 | a1 | a2 | a3",
                e.getPhone(), e.getEmail(), e.getQ1(), e.getQ2(), e.getQ3(), e.getA1(), e.getA2(), e.getA3()
            ).are().notAllNull();
        e.setPerson(loginAccount.getPerson());
        e.setCode(null);
        e.setCodeExp(null);
        e.setCodeCount(null);
        boolean success = dio.updateByKey(e) == 1;
        // 发code才是重点，多人重复无所谓，你要能接收验证码，10个账号同一个验证邮箱的都行
        if (e.getPhone() != null) {
            // todo
            // phoneMsgService.sendValid(Builder.set("k", "v").to(new Twofa()));
        }
        if (e.getEmail() != null) {

        }
        return Rest.ok(success);
    }

    @GetMapping("/accountRecover")
    public Rest<Account> find(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        AccountRecover indb = dio.findByKey(Builder.set("person", loginAccount.getPerson()).to(new AccountRecover()));
        return Rest.ok(indb);
    }
}
