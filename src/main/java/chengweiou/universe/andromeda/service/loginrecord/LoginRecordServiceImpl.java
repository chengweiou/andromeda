package chengweiou.universe.andromeda.service.loginrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.AccountNew;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.blackhole.exception.FailException;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordDio dio;

    @Override
    public void save(LoginRecord e) throws FailException {
        dio.save(e);
    }

    @Override
    public void delete(LoginRecord e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(LoginRecord e) {
        return dio.update(e);
    }

    @Override
    public LoginRecord findLast(AccountNew account) {
        return dio.findLast(account);
    }

    @Override
    public long count(SearchCondition searchCondition, LoginRecord sample) {
        return dio.count(searchCondition, sample);
    }
    @Override
    public List<LoginRecord> find(SearchCondition searchCondition, LoginRecord sample) {
        return dio.find(searchCondition, sample);
    }

}
