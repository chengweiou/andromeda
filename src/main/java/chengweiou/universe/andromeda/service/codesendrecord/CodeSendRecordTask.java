package chengweiou.universe.andromeda.service.codesendrecord;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

@Component
public class CodeSendRecordTask {
    @Autowired
    private CodeSendRecordService service;
    @Async
    public Future<Boolean> save(CodeSendRecord e) throws ProjException {
        try {
            service.save(e);
            return new AsyncResult<>(true);
        } catch (FailException ex) {
            return new AsyncResult<>(false);
        }
    }
}
