package chengweiou.universe.andromeda.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordService;
import chengweiou.universe.blackhole.model.Rest;

@RestController
@RequestMapping("mg")
public class LoginRecordControllerApi {
    @Autowired
    private LoginRecordService service;

    @GetMapping("/loginRecord/count")
    public Rest<Long> count(SearchCondition searchCondition, LoginRecord sample) {
        long count = service.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/loginRecord")
    public Rest<List<LoginRecord>> find(SearchCondition searchCondition, LoginRecord sample) {
        List<LoginRecord> list = service.find(searchCondition, sample);
        return Rest.ok(list);
    }

}
