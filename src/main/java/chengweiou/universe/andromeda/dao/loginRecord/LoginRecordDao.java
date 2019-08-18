package chengweiou.universe.andromeda.dao.loginRecord;


import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;

import java.util.List;

public interface LoginRecordDao extends BaseDao<LoginRecord> {
    long update(LoginRecord e);

    LoginRecord findLastByAccount(Account account);

    long countByPerson(SearchCondition searchCondition, Person person);
    List<LoginRecord> findByPerson(SearchCondition searchCondition, Person person);
}
