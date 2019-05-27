package chengweiou.universe.andromeda.controller.rest.all;


import chengweiou.universe.andromeda.init.jwt.JwtUtil;
import chengweiou.universe.andromeda.init.redis.JedisUtil;
import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordTask;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public Rest<Auth> login(Account e) throws ParamException, ProjException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        Valid.check("account.password", e.getPassword()).is().notEmpty();
        Account indb = service.login(e);
        
        String token = jwtUtil.sign(indb);
        String refreshToken = RandomStringUtils.randomAlphabetic(256);
        jedisUtil.set(refreshToken, token, 60*10);
        Auth auth = Builder.set("token", token).set("refreshToken", refreshToken).set("person", indb.getPerson()).to(new Auth());
        loginRecordTask.save(indb);
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
        jedisUtil.set(auth.getRefreshToken(), token, 60 * 10);
        return Rest.ok(Builder.set("token", token).to(auth));
    }
}
