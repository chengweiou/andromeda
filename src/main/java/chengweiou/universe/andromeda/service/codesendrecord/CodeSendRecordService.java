package chengweiou.universe.andromeda.service.codesendrecord;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

@Service
public class CodeSendRecordService {
    @Autowired
    private CodeSendRecordDio dio;

    public void save(CodeSendRecord e) throws FailException, ProjException {
        dio.save(e);
    }

    public void delete(CodeSendRecord e) throws FailException {
        dio.delete(e);
    }

    public long update(CodeSendRecord e) {
        return dio.update(e);
    }

    public CodeSendRecord findLastByUsername(CodeSendRecord e) {
        return dio.findLastByUsername(e);
    }

    public long count(SearchCondition searchCondition, CodeSendRecord sample) {
        return dio.count(searchCondition, sample);
    }
    public List<CodeSendRecord> find(SearchCondition searchCondition, CodeSendRecord sample) {
        return dio.find(searchCondition, sample);
    }

}
