package chengweiou.universe.andromeda.service.loginrecord;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.LoginRecord;

import java.util.List;

public interface LoginRecordService {
    int save(LoginRecord e);
    int delete(LoginRecord e);

    int count(SearchCondition searchCondition);
    List<LoginRecord> find(SearchCondition searchCondition);

    int count(SearchCondition searchCondition, Person person);
    List<LoginRecord> find(SearchCondition searchCondition, Person person);
}
