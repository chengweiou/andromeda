package chengweiou.universe.andromeda.controller.me;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordService;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("me")
public class LoginRecordControllerMe {
    @Autowired
    private LoginRecordService service;

    @GetMapping("/loginRecord/count")
    public Rest<Long> count(SearchCondition searchCondition, LoginRecord sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        sample.setAccount(Builder.set("person", loginAccount.getPerson()).to(new Account()));
        long count = service.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/loginRecord")
    public Rest<List<LoginRecord>> find(SearchCondition searchCondition, LoginRecord sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().notEmpty();
        sample.setAccount(Builder.set("person", loginAccount.getPerson()).to(new Account()));
        List<LoginRecord> list = service.find(searchCondition, sample);
        return Rest.ok(list);
    }

}
