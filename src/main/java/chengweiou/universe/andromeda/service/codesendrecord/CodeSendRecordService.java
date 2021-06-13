package chengweiou.universe.andromeda.service.codesendrecord;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface CodeSendRecordService {
    // todo 要确保code不存在
    void save(CodeSendRecord e) throws FailException, ProjException;
    void delete(CodeSendRecord e) throws FailException;

    long update(CodeSendRecord e);

    CodeSendRecord findLastByUsername(CodeSendRecord e);

    long count(SearchCondition searchCondition, CodeSendRecord sample);
    List<CodeSendRecord> find(SearchCondition searchCondition, CodeSendRecord sample);

    

}
