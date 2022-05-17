package chengweiou.universe.andromeda.service.codesendrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.CodeSendRecordDao;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord.Dto;
import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;

@Component
public class CodeSendRecordDio extends BaseDio<CodeSendRecord, CodeSendRecord.Dto> {
    @Autowired
    private CodeSendRecordDao dao;
    @Override
    protected CodeSendRecordDao getDao() { return dao; }
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
            if (sample != null) {
                if (sample.getType() != null) WHERE("type = #{sample.type}");
            }
        }}.toString();
    }

    public CodeSendRecord findLastByUsername(CodeSendRecord e) {
        CodeSendRecord.Dto result = dao.findLastByUsername(e.toDto());
        if (result == null) return CodeSendRecord.NULL;
        return result.toBean();
    }

    public CodeSendRecord findLastByUsername(CodeSendRecord e, int skip) {
        CodeSendRecord.Dto result = dao.findOffsetByUsername(e.toDto(), skip);
        if (result == null) return CodeSendRecord.NULL;
        return result.toBean();
    }
}
