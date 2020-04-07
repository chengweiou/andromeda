package chengweiou.universe.andromeda.controller.all;


import chengweiou.universe.andromeda.base.jwt.JwtConfig;
import chengweiou.universe.andromeda.base.jwt.JwtUtil;
import chengweiou.universe.andromeda.base.redis.JedisUtil;
import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordTask;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {
    @Autowired
    private AccountService service;
    @Autowired
    private LoginRecordTask loginRecordTask;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserAgentUtil userAgentUtil;
    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/login")
    public Rest<Auth> login(Account e) throws ParamException, ProjException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        Valid.check("account.password", e.getPassword()).is().notEmpty();
        Account indb = service.login(e);
        String token = jwtUtil.sign(indb);
        String refreshToken = RandomStringUtils.randomAlphabetic(256);
        jedisUtil.set(refreshToken, token, jwtConfig.getRefreshExpMinute().intValue());
        Auth auth = Builder.set("token", token).set("refreshToken", refreshToken).set("person", indb.getPerson()).to(new Auth());
        loginRecordTask.save(
                Builder.set("account", indb).set("ip", request.getRemoteAddr()).set("platform", userAgentUtil.getPlatform(request.getHeader("User-Agent")))
                .to(new LoginRecord()));
        return Rest.ok(auth);
    }

    @PostMapping("/logout")
    public Rest<Boolean> logout(Auth auth) {
        // todo put token to block list
        String token = jedisUtil.get(auth.getRefreshToken());
        jedisUtil.del(auth.getRefreshToken());
        loginRecordTask.logout(token);
        return Rest.ok(true);
    }

    @PostMapping("/token/refresh")
    public Rest<Auth> refresh(Auth auth) throws UnauthException {
        String oldToken = jedisUtil.get(auth.getRefreshToken());
        Account e = jwtUtil.verify(oldToken);
        String token = jwtUtil.sign(e);
        jedisUtil.set(auth.getRefreshToken(), token, jwtConfig.getRefreshExpMinute().intValue());
        return Rest.ok(Builder.set("token", token).to(auth));
    }

    @GetMapping("/account/username/check")
    public Rest<Boolean> checkUsername(Account e) throws ParamException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        long count = service.countByUsername(e);
        return Rest.ok(count == 0);
    }
}
