package chengweiou.universe.andromeda.service.loginrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.LoginRecordDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.blackhole.exception.FailException;

@Component
public class LoginRecordDio {
    @Autowired
    private LoginRecordDao dao;

    public void save(LoginRecord e) throws FailException {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(LoginRecord e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(LoginRecord e) {
        e.updateAt();
        return dao.update(e);
    }

    public LoginRecord findLastByPerson(LoginRecord e) {
        LoginRecord result = dao.findLastByPerson(e);
        return result != null ? result : LoginRecord.NULL;
    }

    public long count(SearchCondition searchCondition, LoginRecord sample) {
        return dao.count(searchCondition, sample);
    }
    public List<LoginRecord> find(SearchCondition searchCondition, LoginRecord sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }
}
