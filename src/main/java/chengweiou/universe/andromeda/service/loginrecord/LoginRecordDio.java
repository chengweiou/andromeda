package chengweiou.universe.andromeda.service.loginrecord;

import chengweiou.universe.andromeda.dao.LoginRecordDao;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginRecordDio {
    @Autowired
    private LoginRecordDao dao;

    public long save(LoginRecord e) {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        return dao.save(e);
    }

    public long delete(LoginRecord e) {
        return dao.delete(e);
    }

    public long update(LoginRecord e) {
        e.updateAt();
        return dao.update(e);
    }

    public LoginRecord findLast(Account account) {
        return dao.findLastByAccount(account);
    }

    public long count(SearchCondition searchCondition) {
        return dao.count(searchCondition);
    }
    public List<LoginRecord> find(SearchCondition searchCondition) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition);
    }

    public long count(SearchCondition searchCondition, Person person) {
        return dao.countByPerson(searchCondition, person);
    }
    public List<LoginRecord> find(SearchCondition searchCondition, Person person) {
        searchCondition.setDefaultSort("updateAt");
        return dao.findByPerson(searchCondition, person);
    }

}
