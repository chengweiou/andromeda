package chengweiou.universe.andromeda.controller.me;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
@RequestMapping("me")
public class LoginRecordControllerMe {
    @Autowired
    private LoginRecordDio dio;

    @GetMapping("/loginRecord/count")
    public Rest<Long> count(SearchCondition searchCondition, LoginRecord sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        sample.setPerson(loginAccount.getPerson());
        long count = dio.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/loginRecord")
    public Rest<List<LoginRecord>> find(SearchCondition searchCondition, LoginRecord sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        sample.setPerson(loginAccount.getPerson());
        List<LoginRecord> list = dio.find(searchCondition, sample);
        return Rest.ok(list);
    }

}
