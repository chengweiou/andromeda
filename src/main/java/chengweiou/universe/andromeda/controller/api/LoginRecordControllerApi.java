package chengweiou.universe.andromeda.controller.api;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordService;
import chengweiou.universe.blackhole.model.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class LoginRecordControllerApi {
    @Autowired
    private LoginRecordService service;

    @GetMapping("/loginRecord/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = service.count(searchCondition);
        return Rest.ok(count);
    }

    @GetMapping("/loginRecord")
    public Rest<List<LoginRecord>> find(SearchCondition searchCondition) {
        List<LoginRecord> list = service.find(searchCondition);
        return Rest.ok(list);
    }

    @GetMapping("/loginRecord/person/{id}/count")
    public Rest<Long> count(SearchCondition searchCondition, Person person) {
        long count = service.count(searchCondition, person);
        return Rest.ok(count);
    }

    @GetMapping("/loginRecord/person/{id}")
    public Rest<List<LoginRecord>> find(SearchCondition searchCondition, Person person) {
        List<LoginRecord> list = service.find(searchCondition, person);
        return Rest.ok(list);
    }
}
