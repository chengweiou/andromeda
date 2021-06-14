// package chengweiou.universe.andromeda.controller.all;


// import java.time.LocalDateTime;
// import java.time.ZoneId;

// import javax.servlet.http.HttpServletRequest;

// import org.apache.commons.lang3.RandomStringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RestController;

// import chengweiou.universe.andromeda.base.jwt.JwtConfig;
// import chengweiou.universe.andromeda.base.jwt.JwtUtil;
// import chengweiou.universe.andromeda.base.redis.JedisUtil;
// import chengweiou.universe.andromeda.model.Auth;
// import chengweiou.universe.andromeda.model.ProjectRestCode;
// import chengweiou.universe.andromeda.model.entity.Account;
// import chengweiou.universe.andromeda.model.entity.AccountRecover;
// import chengweiou.universe.andromeda.model.entity.LoginRecord;
// import chengweiou.universe.andromeda.model.entity.Twofa;
// import chengweiou.universe.andromeda.model.entity.TwofaType;
// import chengweiou.universe.andromeda.service.account.AccountService;
// import chengweiou.universe.andromeda.service.account.TwofaService;
// import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverService;
// import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordService;
// import chengweiou.universe.andromeda.service.loginrecord.LoginRecordTask;
// import chengweiou.universe.andromeda.service.phonemsg.PhoneMsgService;
// import chengweiou.universe.andromeda.util.UserAgentUtil;
// import chengweiou.universe.blackhole.exception.FailException;
// import chengweiou.universe.blackhole.exception.ParamException;
// import chengweiou.universe.blackhole.exception.ProjException;
// import chengweiou.universe.blackhole.exception.UnauthException;
// import chengweiou.universe.blackhole.model.BasicRestCode;
// import chengweiou.universe.blackhole.model.Builder;
// import chengweiou.universe.blackhole.model.Rest;
// import chengweiou.universe.blackhole.param.Valid;

// @RestController
// public class AccountController {
//     @Autowired
//     private AccountService service;
//     @Autowired
//     private TwofaService twofaService;
//     @Autowired
//     private PhoneMsgService phoneMsgService;
//     @Autowired
//     private CodeSendRecordService codeSendRecordService;
//     @Autowired
//     private AccountRecoverService accountRecoverService;
//     @Autowired
//     private LoginRecordTask loginRecordTask;
//     @Autowired
//     private JedisUtil jedisUtil;
//     @Autowired
//     private JwtUtil jwtUtil;
//     @Autowired
//     private HttpServletRequest request;
//     @Autowired
//     private UserAgentUtil userAgentUtil;
//     @Autowired
//     private JwtConfig jwtConfig;

//     @PostMapping("/login")
//     public Rest<Auth> login(Account e) throws ParamException, ProjException, FailException {
//         Valid.check("account.username", e.getUsername()).is().lengthIn(30);
//         Valid.check("account.password", e.getPassword()).is().notEmpty();
//         Account indb = service.login(e);
//         Twofa twofa = twofaService.findByPerson(Builder.set("person", indb.getPerson()).to(new Twofa()));
        
//         if (twofa.getType() != null && twofa.getType() != TwofaType.NONE) {
//             if (LocalDateTime.now(ZoneId.of("UTC")).isBefore(twofa.getCodeExp())) {
//                 throw new ProjException(ProjectRestCode.PHONE_MSG_TOO_MANY);
//             }
//             String code = RandomStringUtils.randomNumeric(6);
//             String token = RandomStringUtils.randomAlphabetic(30);
//             twofa.setLoginAccount(indb);
//             twofa.setCode(code);
//             twofa.setToken(token);
//             switch(twofa.getType()) {
//                 case PHONE_MSG:
//                     phoneMsgService.sendLogin(twofa);
//                     break;
//                 case EMAIL:
//                     phoneMsgService.sendLogin(twofa);
//                     break;
//                 default:
//             }
//             twofaService.update(twofa);
//             return Rest.ok(ProjectRestCode.TWOFA_WAITING, Builder.set("token", token).to(new Auth()));
//         }
//         Auth auth = getAuthBySuccessLogin(indb);
//         return Rest.ok(auth);
//     }

//     @PostMapping("/checkTwofa")
//     public Rest<Auth> checkTwofa(Twofa twofa) throws ParamException, ProjException {
//         Valid.check("twofa.token", twofa.getToken()).is().notEmpty();
//         Valid.check("twofa.code", twofa.getCode()).is().notEmpty();

//         Account indb = service.findAfterCheckCode(twofa);
//         Auth auth = getAuthBySuccessLogin(indb);
//         return Rest.ok(auth);
//     }

//     private Auth getAuthBySuccessLogin(Account indb) {
//         String token = jwtUtil.sign(indb);
//         String refreshToken = RandomStringUtils.randomAlphabetic(256);
//         jedisUtil.set(refreshToken, token, jwtConfig.getRefreshExpMinute().intValue()*60);
//         Auth result = Builder.set("token", token).set("refreshToken", refreshToken).set("person", indb.getPerson()).to(new Auth());
//         loginRecordTask.save(
//                 Builder.set("account", indb).set("ip", request.getRemoteAddr()).set("platform", userAgentUtil.getPlatform(request.getHeader("User-Agent")))
//                 .to(new LoginRecord()));
//         return result;
//     }

//     @PostMapping("/logout")
//     public Rest<Boolean> logout(Auth auth) {
//         // todo put token to block list
//         String token = jedisUtil.get(auth.getRefreshToken());
//         jedisUtil.del(auth.getRefreshToken());
//         loginRecordTask.logout(token);
//         return Rest.ok(true);
//     }

//     @PostMapping("/token/refresh")
//     public Rest<Auth> refresh(Auth auth) throws UnauthException {
//         String oldToken = jedisUtil.get(auth.getRefreshToken());
//         Account e = jwtUtil.verify(oldToken);
//         String token = jwtUtil.sign(e);
//         jedisUtil.set(auth.getRefreshToken(), token, jwtConfig.getRefreshExpMinute().intValue()*60);
//         return Rest.ok(Builder.set("token", token).to(auth));
//     }

//     @GetMapping("/account/username/check")
//     public Rest<Boolean> checkUsername(Account e) throws ParamException {
//         Valid.check("account.username", e.getUsername()).is().lengthIn(30);
//         long count = service.countByUsername(e);
//         return Rest.ok(count == 0);
//     }

//     // apple
//     // 输入appleid，点击忘记密码，
//     // ***-***-**50 输入完整电话号码
//     // 发送短信，请输入短信验证码

//     // 忘记密码：1. 输入用户名，返回可选的恢复账号方式
//     @PostMapping("/forgetPassword/1")
//     public Rest<AccountRecover> forgetPassword1(Account e) throws ParamException, ProjException, FailException {
//         Valid.check("account.username", e.getUsername()).is().lengthIn(30);
//         Account indb = service.findByUsername(e);
//         AccountRecover result = accountRecoverService.findByPerson(Builder.set("person", indb.getPerson()).to(new AccountRecover()));
//         if (result.getPhone() != null) {
//             result.setPhone("********" + result.getPhone().substring(result.getPhone().length() - 2));
//         }
//         if (result.getEmail() != null) {
//             int atIndex = result.getEmail().indexOf("@");
//             result.setEmail(result.getEmail().substring(0, Math.min(2, atIndex)) + "***@" + result.getEmail().substring(atIndex+1, atIndex+2) + "***");
//         }
//         result.setA1(null);
//         result.setA2(null);
//         result.setA3(null);
//         return Rest.ok(result);
//     }

//     // 忘记密码：2. 客户端填充完整信息。服务端发送code
//     @PostMapping("/forgetPassword/2")
//     public Rest<String> forgetPassword2(AccountRecover userConfirm) throws ParamException, ProjException, FailException {
//         Valid.check("accountRecover.id", userConfirm.getId()).is().positive();
//         Valid.check("accountRecover.phone | email | a1 | a2 | a3", 
//                 userConfirm.getPhone(), userConfirm.getEmail(), userConfirm.getA1(), userConfirm.getA2(), userConfirm.getA3()
//             ).are().notAllNull();
//         String result = null;
//         AccountRecover indb = accountRecoverService.findById(userConfirm);
//         // 在过期前，只能发三次
//         if (LocalDateTime.now(ZoneId.of("UTC")).isBefore(indb.getCodeExp()) && indb.getCodeCount() == 3) throw new ProjException(ProjectRestCode.CODE_TOO_MANY);
//         if (
//             (userConfirm.getPhone() == null || !userConfirm.getPhone().equals(indb.getPhone()))
//             && (userConfirm.getEmail() == null || !userConfirm.getEmail().equals(indb.getEmail()))
//             && (userConfirm.getA1() == null || !userConfirm.getA1().equals(indb.getA1()))
//             && (userConfirm.getA2() == null || !userConfirm.getA2().equals(indb.getA2()))
//             && (userConfirm.getA3() == null || !userConfirm.getA3().equals(indb.getA3()))
//         ) throw new ProjException(ProjectRestCode.ACCOUNT_NOT_MATCH);
//         String code = RandomStringUtils.randomAlphanumeric(50);
//         Builder.set("code", code).set("codeExp", LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(10)).set("codeCount", indb.getCodeCount()+1).to(indb);
//         if (userConfirm.getPhone() != null) phoneMsgService.sendForgetUrl(indb);
//         else if (userConfirm.getEmail() != null) phoneMsgService.sendForgetUrl(indb);
//         else result = code;
//         accountRecoverService.update(indb);
//         return Rest.ok(result);
//     }

//     // 忘记密码: 3. 新密码
//     @PostMapping("/forgetPassword/3")
//     public Rest<String> forgetPassword3(AccountRecover accountRecover, Account e) throws ParamException, ProjException {
//         Valid.check("accountRecover.id", accountRecover.getId()).is().positive();
//         Valid.check("accountRecover.code", accountRecover.getCode()).is().lengthIs(50);
//         Valid.check("account.password", e.getPassword()).is().lengthIn(30);
//         e.setId(null);
//         AccountRecover accountRecoverIndb = accountRecoverService.findByActiveCode(accountRecover);        
//         // 可能多个账号
//         boolean success = service.updateByPerson(Builder.set("person", accountRecoverIndb.getPerson()).set("password", e.getPassword()).to(new Account())) > 0;
//         if (!success) {
//             return Rest.fail(BasicRestCode.FAIL);
//         }
//         return Rest.ok(null);
//     }

// }
