package chengweiou.universe.andromeda.service.codesendrecord;

import java.util.List;
import java.util.stream.Collectors;

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
        CodeSendRecord.Dto dto = e.toDto();
        long count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(CodeSendRecord e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(CodeSendRecord e) {
        e.updateAt();
        return dao.update(e.toDto());
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

    public long count(SearchCondition searchCondition, CodeSendRecord sample) {
        return dao.count(searchCondition, sample!=null ? sample.toDto() : null);
    }
    public List<CodeSendRecord> find(SearchCondition searchCondition, CodeSendRecord sample) {
        searchCondition.setDefaultSort("createAt");
        List<CodeSendRecord.Dto> dtoList = dao.find(searchCondition, sample!=null ? sample.toDto() : null);
        List<CodeSendRecord> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }
}
