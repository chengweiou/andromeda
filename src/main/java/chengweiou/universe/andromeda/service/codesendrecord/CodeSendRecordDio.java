package chengweiou.universe.andromeda.service.codesendrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.CodeSendRecordDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.blackhole.exception.FailException;

@Component
public class CodeSendRecordDio {
    @Autowired
    private CodeSendRecordDao dao;

    public void save(CodeSendRecord e) throws FailException {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(CodeSendRecord e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(CodeSendRecord e) {
        e.updateAt();
        return dao.update(e);
    }

    public CodeSendRecord findLastByUsername(CodeSendRecord e) {
        CodeSendRecord result = dao.findLastByUsername(e);
        return result != null ? result : CodeSendRecord.NULL;
    }

    public CodeSendRecord findLastByUsername(CodeSendRecord e, int skip) {
        CodeSendRecord result = dao.findOffsetByUsername(e, skip);
        return result != null ? result : CodeSendRecord.NULL;
    }

    public long count(SearchCondition searchCondition, CodeSendRecord sample) {
        return dao.count(searchCondition, sample);
    }
    public List<CodeSendRecord> find(SearchCondition searchCondition, CodeSendRecord sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }
}
