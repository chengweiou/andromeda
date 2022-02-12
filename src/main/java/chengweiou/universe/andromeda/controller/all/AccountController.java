package chengweiou.universe.andromeda.controller.all;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
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
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.model.entity.twofa.TwofaType;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverService;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordService;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordTask;
import chengweiou.universe.andromeda.service.phonemsg.PhoneMsgService;
import chengweiou.universe.andromeda.service.twofa.TwofaService;
import chengweiou.universe.andromeda.util.UserAgentUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;

@RestController
public class AccountController {
    @Autowired
    private AccountService service;
    @Autowired
    private AccountDio dio;
    @Autowired
    private TwofaService twofaService;
    @Autowired
    private PhoneMsgService phoneMsgService;
    @Autowired
    private CodeSendRecordService codeSendRecordService;
    @Autowired
    private AccountRecoverService accountRecoverService;
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
    public Rest<Auth> login(Account e) throws ParamException, ProjException, FailException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        Valid.check("account.password", e.getPassword()).is().notEmpty();
        Account indb = service.login(e);
        Twofa twofa = twofaService.findAndWaitForLogin(Builder.set("person", indb.getPerson()).to(new Twofa()));
        if (twofa.notNull()) {
            switch(twofa.getType()) {
                case PHONE_MSG:
                    phoneMsgService.sendLogin(twofa);
                    break;
                case EMAIL:
                    phoneMsgService.sendLogin(twofa);
                    break;
                default:
            }
            return Rest.ok(ProjectRestCode.TWOFA_WAITING, Builder.set("token", twofa.getToken()).to(new Auth()));
        }
        Auth auth = getAuthBySuccessLogin(indb);
        return Rest.ok(auth);
    }

    @PostMapping("/checkTwofa")
    public Rest<Auth> checkTwofa(Twofa twofa) throws ParamException, ProjException {
        Valid.check("twofa.token", twofa.getToken()).is().notEmpty();
        Valid.check("twofa.code", twofa.getCode()).is().notEmpty();
        Twofa twofaIndb = twofaService.findAfterCheckCode(twofa);
        Account indb = dio.findByKey(Builder.set("person", twofaIndb.getPerson()).to(new Account()));
        Auth auth = getAuthBySuccessLogin(indb);
        return Rest.ok(auth);
    }

    private Auth getAuthBySuccessLogin(Account indb) {
        String token = jwtUtil.sign(indb);
        String refreshToken = RandomStringUtils.randomAlphabetic(256);
        jedisUtil.set(refreshToken, token, jwtConfig.getRefreshExpMinute().intValue()*60);
        Auth result = Builder.set("token", token).set("refreshToken", refreshToken).set("person", indb.getPerson()).to(new Auth());
        loginRecordTask.save(
                Builder.set("person", indb.getPerson()).set("ip", request.getRemoteAddr()).set("platform", userAgentUtil.getPlatform(request.getHeader("User-Agent")))
                .to(new LoginRecord()));
        return result;
    }

    @PostMapping("/logout")
    public Rest<Boolean> logout(Auth auth) throws ParamException {
        if (auth.getRefreshToken()==null || auth.getRefreshToken().isEmpty()) return Rest.ok(false);
        // Valid.check("auth.refreshToken", auth.getRefreshToken()).is().notEmpty();
        // todo put token to block list
        String token = jedisUtil.get(auth.getRefreshToken());
        jedisUtil.del(auth.getRefreshToken());
        loginRecordTask.logout(token);
        return Rest.ok(true);
    }

    @PostMapping("/token/refresh")
    public Rest<Auth> refresh(Auth auth) throws UnauthException, ParamException {
        Valid.check("auth.refreshToken", auth.getRefreshToken()).is().notEmpty();
        String oldToken = jedisUtil.get(auth.getRefreshToken());
        Account e = jwtUtil.decode(oldToken);
        String token = jwtUtil.sign(e);
        jedisUtil.set(auth.getRefreshToken(), token, jwtConfig.getRefreshExpMinute().intValue()*60);
        return Rest.ok(Builder.set("token", token).to(auth));
    }

    @GetMapping("/account/username/check")
    public Rest<Boolean> checkUsername(Account e) throws ParamException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        long count = dio.countByLoginUsername(e);
        return Rest.ok(count == 0);
    }

    // apple
    // 输入appleid，点击忘记密码，
    // ***-***-**50 输入完整电话号码
    // 发送短信，请输入短信验证码

    // 忘记密码：1. 输入用户名，返回可选的恢复账号方式
    @PostMapping("/forgetPassword/1")
    public Rest<AccountRecover> forgetPasswordS1(Account e) throws ParamException, ProjException, FailException {
        Valid.check("account.username", e.getUsername()).is().lengthIn(30);
        AccountRecover result = accountRecoverService.forgetPasswordS1(e);
        return Rest.ok(result);
    }

    // 忘记密码：2. 客户端填充完整信息。服务端发送code
    @PostMapping("/forgetPassword/2")
    public Rest<String> forgetPasswordS2(AccountRecover userConfirm) throws ParamException, ProjException, FailException {
        Valid.check("accountRecover.id", userConfirm.getId()).is().positive();
        Valid.check("accountRecover.phone | email | a1 | a2 | a3",
                userConfirm.getPhone(), userConfirm.getEmail(), userConfirm.getA1(), userConfirm.getA2(), userConfirm.getA3()
            ).are().notAllNull();
        String result = null;
        String code = accountRecoverService.forgetPasswordS2(userConfirm);
        userConfirm.setCode(code);
        // 输入电话发送短信，输入email发送email， 如果是密保问题，直接返回
        if (userConfirm.getPhone() != null) phoneMsgService.sendForgetUrl(userConfirm);
        else if (userConfirm.getEmail() != null) phoneMsgService.sendForgetUrl(userConfirm);
        else result = code;
        return Rest.ok(result);
    }

    // 忘记密码: 3. 新密码
    @PostMapping("/forgetPassword/3")
    public Rest<String> forgetPasswordS3(AccountRecover accountRecover, Account e) throws ParamException, ProjException {
        Valid.check("accountRecover.id", accountRecover.getId()).is().positive();
        Valid.check("accountRecover.code", accountRecover.getCode()).is().lengthIs(50);
        Valid.check("account.password", e.getPassword()).is().lengthIn(30);
        long count = accountRecoverService.forgetPasswordS3(accountRecover, e);
        if (count != 1) {
            return Rest.fail(BasicRestCode.FAIL);
        }
        return Rest.ok(null);
    }

}
