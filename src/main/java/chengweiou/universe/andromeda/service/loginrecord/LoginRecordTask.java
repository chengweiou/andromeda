package chengweiou.universe.andromeda.service.loginrecord;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.base.jwt.JwtUtil;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginRecordTask {
    @Autowired
    private LoginRecordDio dio;
    @Autowired
    private UserAgentUtil userAgentUtil;
    @Autowired
    private JwtUtil jwtUtil;

    @Async
    public CompletableFuture<Boolean> save(LoginRecord e) {
        try {
            dio.save(e);
            return CompletableFuture.completedFuture(true);
        } catch (FailException ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public CompletableFuture<Long> logout(String token) throws FailException {
        try {
            Account account = jwtUtil.verify(token);
            LoginRecord e = dio.findLastByPerson(Builder.set("person", account.getPerson()).to(new LoginRecord()));
            e.setLogoutTime(Instant.now().toString());
            long count = dio.update(e);
            return CompletableFuture.completedFuture(count);
        } catch (UnauthException e) {
            log.info("logout update record fail <-- jwt verify fail");
            return CompletableFuture.completedFuture(0L);
        }
    }
}
