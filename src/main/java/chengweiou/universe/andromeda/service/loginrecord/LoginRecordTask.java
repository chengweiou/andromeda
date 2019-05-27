package chengweiou.universe.andromeda.service.loginrecord;

import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.model.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;

@Component
public class LoginRecordTask {
    @Autowired
    private LoginRecordService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserAgentUtil userAgentUtil;

    @Async
    public Future<Integer> save(Account account) {
        int count = service.save(
                Builder.set("account", account).set("ip", request.getRemoteAddr()).set("platform", userAgentUtil.getPlatform(request.getHeader("User-Agent")))
                        .to(new LoginRecord()));
        return new AsyncResult<>(count);
    }
}
