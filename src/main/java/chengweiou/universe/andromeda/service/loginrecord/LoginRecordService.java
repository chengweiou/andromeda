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

    public void save(LoginRecord e) throws FailException {
        dio.save(e);
    }

    public void delete(LoginRecord e) throws FailException {
        dio.delete(e);
    }

    public long update(LoginRecord e) {
        return dio.update(e);
    }

    public LoginRecord findLastByPerson(LoginRecord e) {
        return dio.findLastByPerson(e);
    }

    public long count(SearchCondition searchCondition, LoginRecord sample) {
        return dio.count(searchCondition, sample);
    }
    public List<LoginRecord> find(SearchCondition searchCondition, LoginRecord sample) {
        return dio.find(searchCondition, sample);
    }

}
