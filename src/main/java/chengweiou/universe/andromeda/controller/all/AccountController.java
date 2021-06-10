package chengweiou.universe.andromeda.controller.all;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.andromeda.base.jwt.JwtConfig;
import chengweiou.universe.andromeda.base.jwt.JwtUtil;
import chengweiou.universe.andromeda.base.redis.JedisUtil;
import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.TwofaType;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.account.TwofaService;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordTask;
import chengweiou.universe.andromeda.service.phonemsg.PhoneMsgService;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
public class AccountController {
    @Autowired
    private AccountService service;
    @Autowired
    private TwofaService twofaService;
    @Autowired
    private PhoneMsgService phoneMsgService;
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
        Twofa twofa = twofaService.findByPerson(Builder.set("person", indb.getPerson()).to(new Twofa()));
        
        if (twofa.getType() != null && twofa.getType() != TwofaType.NONE) {
            int code = RandomUtils.nextInt(100000, 999999);
            String token = RandomStringUtils.randomAlphabetic(30);
            twofa.setLoginAccount(indb);
            twofa.setCode(String.valueOf(code));
            twofa.setToken(token);
            switch(twofa.getType()) {
                case PHONE_MSG:
                    phoneMsgService.sendCode(twofa);
                    break;
                case EMAIL:
                    phoneMsgService.sendCode(twofa);
                    break;
                default:
            }
            twofaService.update(twofa);            
            return Rest.fail(ProjectRestCode.TWOFA_WAITING);
        }
        Auth auth = getAuthBySuccessLogin(indb);
        return Rest.ok(auth);
    }

    @PostMapping("/checkTwofa")
    public Rest<Auth> checkTwofa(Twofa twofa) throws ParamException, ProjException {
        Valid.check("twofa.token", twofa.getToken()).is().notEmpty();
        Valid.check("twofa.code", twofa.getCode()).is().notEmpty();

        Account indb = service.findAfterCheckCode(twofa);
        Auth auth = getAuthBySuccessLogin(indb);
        return Rest.ok(auth);
    }

    private Auth getAuthBySuccessLogin(Account indb) {
        String token = jwtUtil.sign(indb);
        String refreshToken = RandomStringUtils.randomAlphabetic(256);
        jedisUtil.set(refreshToken, token, jwtConfig.getRefreshExpMinute().intValue()*60);
        Auth result = Builder.set("token", token).set("refreshToken", refreshToken).set("person", indb.getPerson()).to(new Auth());
        loginRecordTask.save(
                Builder.set("account", indb).set("ip", request.getRemoteAddr()).set("platform", userAgentUtil.getPlatform(request.getHeader("User-Agent")))
                .to(new LoginRecord()));
        return result;
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
        jedisUtil.set(auth.getRefreshToken(), token, jwtConfig.getRefreshExpMinute().intValue()*60);
        return Rest.ok(Builder.set("token", token).to(auth));
    }

    @GetMapping("/account/username/check")
    public Rest<Boolean> checkUsername(Account e) throws ParamException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        long count = service.countByUsername(e);
        return Rest.ok(count == 0);
    }
}
