package chengweiou.universe.andromeda.service.loginrecord;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.LoginRecordDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.exception.FailException;

@Component
public class LoginRecordDio {
    @Autowired
    private LoginRecordDao dao;

    public void save(LoginRecord e) throws FailException {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        LoginRecord.Dto dto = e.toDto();
        long count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(LoginRecord e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(LoginRecord e) {
        e.updateAt();
        return dao.update(e.toDto());
    }

    public LoginRecord findLastByPerson(LoginRecord e) {
        LoginRecord.Dto result = dao.findLastByPerson(e.toDto());
        if (result == null) return LoginRecord.NULL;
        return result.toBean();
    }

    public long count(SearchCondition searchCondition, LoginRecord sample) {
        LoginRecord.Dto dtoSample = sample!=null ? sample.toDto() : LoginRecord.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        return dao.count(searchCondition, dtoSample, where);
    }

    public List<LoginRecord> find(SearchCondition searchCondition, LoginRecord sample) {
        searchCondition.setDefaultSort("createAt");
        LoginRecord.Dto dtoSample = sample!=null ? sample.toDto() : LoginRecord.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        List<LoginRecord.Dto> dtoList = dao.find(searchCondition, dtoSample, where);
        List<LoginRecord> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

    private String baseFind(SearchCondition searchCondition, LoginRecord.Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
            (ip LIKE #{searchCondition.like.k} or platform LIKE #{searchCondition.like.k})
            """);
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId=#{sample.personId}");
            }
        }}.toString();
    }
}
