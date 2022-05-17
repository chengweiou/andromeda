package chengweiou.universe.andromeda.service.loginrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.LoginRecordDao;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord.Dto;
import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.dao.DioCache;
import chengweiou.universe.blackhole.dao.DioDefaultSort;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;

@DioCache(false)
@Component
public class LoginRecordDio extends BaseDio<LoginRecord, LoginRecord.Dto> {
    @Autowired
    private LoginRecordDao dao;
    @Override
    protected LoginRecordDao getDao() { return dao; }
    @DioDefaultSort("createAt")
    private String defaultSort;
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
                (ip LIKE #{searchCondition.like.k} or platform LIKE #{searchCondition.like.k})
                """);
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId=#{sample.personId}");
            }
        }}.toString();
    }

    public LoginRecord findLastByPerson(LoginRecord e) {
        LoginRecord.Dto result = dao.findLastByPerson(e.toDto());
        if (result == null) return LoginRecord.NULL;
        return result.toBean();
    }
}
