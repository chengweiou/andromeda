package chengweiou.universe.andromeda.service.codesendrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.blackhole.exception.FailException;

@Service
public class CodeSendRecordServiceImpl implements CodeSendRecordService {
    @Autowired
    private CodeSendRecordDio dio;

    @Override
    public void save(CodeSendRecord e) throws FailException {
        dio.save(e);
    }

    @Override
    public void delete(CodeSendRecord e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(CodeSendRecord e) {
        return dio.update(e);
    }

    @Override
    public CodeSendRecord findLastByUsername(CodeSendRecord e) {
        return dio.findLastByUsername(e);
    }

    @Override
    public long count(SearchCondition searchCondition, CodeSendRecord sample) {
        return dio.count(searchCondition, sample);
    }
    @Override
    public List<CodeSendRecord> find(SearchCondition searchCondition, CodeSendRecord sample) {
        return dio.find(searchCondition, sample);
    }

}
