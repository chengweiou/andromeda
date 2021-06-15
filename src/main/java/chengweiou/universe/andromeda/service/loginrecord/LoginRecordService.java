package chengweiou.universe.andromeda.service.loginrecord;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.blackhole.exception.FailException;

public interface LoginRecordService {
    void save(LoginRecord e) throws FailException;
    void delete(LoginRecord e) throws FailException;

    long update(LoginRecord e);

    LoginRecord findLast(Account account);

    long count(SearchCondition searchCondition, LoginRecord sample);
    List<LoginRecord> find(SearchCondition searchCondition, LoginRecord sample);

}
