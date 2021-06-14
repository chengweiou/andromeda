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
import chengweiou.universe.andromeda.model.entity.AccountNew;
import chengweiou.universe.andromeda.service.account.AccountNewService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
@RequestMapping("mg")
public class AccountNewControllerMg {
    @Autowired
    private AccountNewService service;

    @PostMapping("/accountNew")
    public Rest<Long> save(AccountNew e) throws ParamException, FailException, ProjException {
        Valid.check("accountNew.username|phone|email", e.getUsername(), e.getPhone(), e.getEmail()).are().notAllNull();
        Valid.check("accountNew.password", e.getPassword()).is().notEmpty();
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/accountNew/{id}")
    public Rest<Boolean> delete(AccountNew e) throws ParamException, FailException {
        Valid.check("accountNew.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }

    @PutMapping("/accountNew/{id}")
    public Rest<Boolean> update(AccountNew e) throws ParamException, ProjException {
        Valid.check("accountNew.id", e.getId()).is().positive();
        Valid.check("accountNew.username | phone | email | wechat | weibo | google | facebook | password | active | person | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getPassword(), e.getPerson(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @PutMapping("/accountNew/person/{person.id}")
    public Rest<Boolean> updateByPerson(AccountNew e) throws ParamException, ProjException {
        Valid.check("accountNew.person.id", e.getPerson().getId()).is().notOf("0");
        Valid.check("accountNew.username | phone | email | wechat | weibo | google | facebook | password | active | extra",
                e.getUsername(), e.getPhone(), e.getEmail(), e.getWechat(), e.getWeibo(), e.getGoogle(), e.getFacebook(), e.getPassword(), e.getActive(), e.getExtra()
            ).are().notAllNull();
        boolean success = service.updateByPerson(e) == 1 ;
        return Rest.ok(success);
    }

    @PutMapping("/accountNew/{id}/person")
    public Rest<Boolean> updatePerson(AccountNew e) throws ParamException, ProjException {
        Valid.check("accountNew.id", e.getId()).is().positive();
        Valid.check("accountNew.person.id", e.getPerson().getId()).is().notOf("0");
        // will update person and acitve=true together
        boolean success = service.update(Builder.set("id", e.getId()).set("person", e.getPerson()).set("active", true).to(new AccountNew())) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/accountNew/{id}")
    public Rest<AccountNew> findById(AccountNew e) throws ParamException {
        Valid.check("accountNew.id", e.getId()).is().positive();
        AccountNew data = service.findById(e);
        return Rest.ok(data);
    }

    @GetMapping("/accountNew/count")
    public Rest<Long> count(SearchCondition searchCondition, AccountNew sample) {
        long count = service.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/accountNew")
    public Rest<List<AccountNew>> find(SearchCondition searchCondition, AccountNew sample) {
        List<AccountNew> list = service.find(searchCondition, sample);
        return Rest.ok(list);
    }
}
