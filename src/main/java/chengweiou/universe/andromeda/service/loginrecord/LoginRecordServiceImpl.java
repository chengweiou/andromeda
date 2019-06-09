package chengweiou.universe.andromeda.service.loginrecord;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.blackhole.exception.FailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordDio dio;

    @Override
    public void save(LoginRecord e) throws FailException {
        int count = dio.save(e);
        if (count != 1) throw new FailException();
    }

    @Override
    public void delete(LoginRecord e) throws FailException {
        int count = dio.delete(e);
        if (count != 1) throw new FailException();
    }

    @Override
    public int update(LoginRecord e) {
        return dio.update(e);
    }

    @Override
    public LoginRecord findLast(Account account) {
        return dio.findLast(account);
    }

    @Override
    public int count(SearchCondition searchCondition) {
        return dio.count(searchCondition);
    }
    @Override
    public List<LoginRecord> find(SearchCondition searchCondition) {
        return dio.find(searchCondition);
    }

    @Override
    public int count(SearchCondition searchCondition, Person person) {
        return dio.count(searchCondition, person);
    }
    @Override
    public List<LoginRecord> find(SearchCondition searchCondition, Person person) {
        return dio.find(searchCondition, person);
    }

}
