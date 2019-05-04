package com.universe.andromeda.controller.rest.all;


import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import com.universe.andromeda.init.redis.JedisUtil;
import com.universe.andromeda.model.Auth;
import com.universe.andromeda.model.ProjectRestCode;
import com.universe.andromeda.model.entity.Account;
import com.universe.andromeda.service.AccountService;
import com.universe.andromeda.init.config.JwtUtil;
import com.universe.andromeda.util.SecurityUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountService service;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Rest<Auth> login(Account e) throws ParamException, ProjException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        Valid.check("account.password", e.getPassword()).is().notEmpty();
        Account indb = service.findByUsername(e);
        if (!SecurityUtil.check(e.getPassword(), indb.getPassword())) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        String token = jwtUtil.sign(indb);
        String refreshToken = RandomStringUtils.random(256);
        jedisUtil.set(refreshToken, token, 60*10);
        Auth auth = Builder.set("token", token).set("refreshToken", refreshToken).set("personId", indb.getPersonId()).to(new Auth());
        return Rest.ok(auth);
    }

    @PostMapping("/logout")
    public Rest<Boolean> logout(Auth auth) {
        // put token to block list
        jedisUtil.del(auth.getRefreshToken());
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

    @GetMapping("/test")
    public Rest<Auth> testparam(Auth auth) throws UnauthException {
        jedisUtil.set("aaa", "111", 60);
        auth.setToken(jedisUtil.get("aaa"));
        return Rest.ok(auth);
    }
}
