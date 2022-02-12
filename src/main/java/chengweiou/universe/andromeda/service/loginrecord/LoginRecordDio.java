package chengweiou.universe.andromeda.service.loginrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.LoginRecordDao;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord.Dto;
import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;

@Component
public class LoginRecordDio extends BaseDio<LoginRecord, LoginRecord.Dto> {
    @Autowired
    private LoginRecordDao dao;
    @Override
    protected LoginRecordDao getDao() { return dao; }
    @Override
    protected Class getTClass() { return LoginRecord.class; };
    @Override
    protected String getDefaultSort() { return "createAt"; };
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
