package chengweiou.universe.andromeda.service.loginrecord;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.blackhole.exception.FailException;

import java.util.List;

public interface LoginRecordService {
    void save(LoginRecord e) throws FailException;
    void delete(LoginRecord e) throws FailException;

    long update(LoginRecord e);

    LoginRecord findLast(Account account);

    long count(SearchCondition searchCondition, LoginRecord sample);
    List<LoginRecord> find(SearchCondition searchCondition, LoginRecord sample);

}
