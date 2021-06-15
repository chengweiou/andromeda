package chengweiou.universe.andromeda.service.loginrecord;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.base.jwt.JwtUtil;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;

@Component
public class LoginRecordTask {
    @Autowired
    private LoginRecordService service;
    @Autowired
    private UserAgentUtil userAgentUtil;
    @Autowired
    private JwtUtil jwtUtil;

    @Async
    public Future<Boolean> save(LoginRecord e) {
        try {
            service.save(e);
            return new AsyncResult<>(true);
        } catch (FailException ex) {
            return new AsyncResult<>(false);
        }
    }

    @Async
    public Future<Long> logout(String token) {
        try {
            Account account = jwtUtil.verify(token);
            LoginRecord e = service.findLastByPerson(Builder.set("person", account.getPerson()).to(new LoginRecord()));
            e.setLogoutTime(LocalDateTime.now(ZoneId.of("UTC")).toString());
            long count = service.update(e);
            return new AsyncResult<>(count);
        } catch (UnauthException e) {
            LogUtil.i("logout update record fail <-- jwt verify fail");
            return new AsyncResult<>(0L);
        }
    }
}
