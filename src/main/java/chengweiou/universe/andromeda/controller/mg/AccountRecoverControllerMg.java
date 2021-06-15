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
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
@RequestMapping("mg")
public class AccountRecoverControllerMg {
    @Autowired
    private AccountRecoverService service;

    @PostMapping("/accountRecover")
    public Rest<Long> save(AccountRecover e) throws ParamException, FailException, ProjException {
        Valid.check("accountRecover.person", e.getPerson()).isNotNull();
        Valid.check("accountRecover.person.id", e.getPerson().getId()).is().notEmpty();
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/accountRecover/{id}")
    public Rest<Boolean> delete(AccountRecover e) throws ParamException, FailException {
        Valid.check("accountRecover.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }

    @PutMapping("/accountRecover/{id}")
    public Rest<Boolean> update(AccountRecover e) throws ParamException, ProjException {
        Valid.check("accountRecover.id", e.getId()).is().positive();
        Valid.check("accountRecover.phone | email | q1 | q2 | q3 | a1 | a2 | a3 | code | codeExp | codeCount",
                e.getPhone(), e.getEmail(), e.getQ1(), e.getQ2(), e.getQ3(), e.getA1(), e.getA2(), e.getA3(), e.getCode(), e.getCodeExp(), e.getCodeExp()
            ).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/accountRecover/{id}")
    public Rest<AccountRecover> findById(AccountRecover e) throws ParamException {
        Valid.check("accountRecover.id", e.getId()).is().positive();
        AccountRecover data = service.findById(e);
        return Rest.ok(data);
    }

    @GetMapping("/accountRecover/count")
    public Rest<Long> count(SearchCondition searchCondition, AccountRecover sample) {
        long count = service.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/accountRecover")
    public Rest<List<AccountRecover>> find(SearchCondition searchCondition, AccountRecover sample) {
        List<AccountRecover> list = service.find(searchCondition, sample);
        return Rest.ok(list);
    }
}
