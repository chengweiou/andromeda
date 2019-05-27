package chengweiou.universe.andromeda.service.loginrecord;

import chengweiou.universe.andromeda.init.jwt.JwtUtil;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Future;

@Component
public class LoginRecordTask {
    @Autowired
    private LoginRecordService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserAgentUtil userAgentUtil;
    @Autowired
    private JwtUtil jwtUtil;

    @Async
    public Future<Integer> save(Account account) {
        int count = service.save(
                Builder.set("account", account).set("ip", request.getRemoteAddr()).set("platform", userAgentUtil.getPlatform(request.getHeader("User-Agent")))
                        .to(new LoginRecord()));
        return new AsyncResult<>(count);
    }

    @Async
    public Future<Integer> logout(String token) {
        try {
            Account account = jwtUtil.verify(token);
            LoginRecord e = service.findLast(account);
            e.setLogoutTime(LocalDateTime.now(ZoneId.of("UTC")).toString());
            int count = service.update(e);
            return new AsyncResult<>(count);
        } catch (UnauthException e) {
            LogUtil.i("logout update record fail <-- jwt verify fail");
            return new AsyncResult<>(0);
        }
    }
}
