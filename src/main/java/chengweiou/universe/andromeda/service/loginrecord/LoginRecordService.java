package chengweiou.universe.andromeda.service.loginrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.blackhole.exception.FailException;

@Service
public class LoginRecordService {
    @Autowired
    private LoginRecordDio dio;

}
