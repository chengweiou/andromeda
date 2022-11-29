package chengweiou.universe.andromeda.service.codesendrecord;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

@Component
public class CodeSendRecordTask {
    @Autowired
    private CodeSendRecordDio dio;
    @Async
    public CompletableFuture<Boolean> save(CodeSendRecord e) throws ProjException {
        try {
            dio.save(e);
            return CompletableFuture.completedFuture(true);
        } catch (FailException ex) {
            return CompletableFuture.completedFuture(false);
        }
    }
}
