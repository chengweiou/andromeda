package chengweiou.universe.andromeda.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.model.Rest;

@RestController
@RequestMapping("mg")
public class LoginRecordControllerMg {
    @Autowired
    private LoginRecordDio dio;

    @GetMapping("/loginRecord/count")
    public Rest<Long> count(SearchCondition searchCondition, LoginRecord sample) {
        long count = dio.count(searchCondition, sample);
        return Rest.ok(count);
    }

    @GetMapping("/loginRecord")
    public Rest<List<LoginRecord>> find(SearchCondition searchCondition, LoginRecord sample) {
        List<LoginRecord> list = dio.find(searchCondition, sample);
        return Rest.ok(list);
    }

}
