package chengweiou.universe.andromeda.service.accountrecover;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;

@Service
public class AccountRecoverService {
    @Autowired
    private AccountRecoverDio dio;
    @Autowired
    private AccountDio accountDio;

    public void save(AccountRecover e) throws FailException, ProjException {
        e.cleanCode();
        dio.save(e);
    }

    // apple
    // 输入appleid，点击忘记密码，
    // ***-***-**50 输入完整电话号码
    // 发送短信，请输入短信验证码

    // 忘记密码：1. 输入用户名，返回可选的恢复账号方式
    public AccountRecover forgetPasswordS1(Account e) throws ProjException {
        Account indb = accountDio.findByLoginUsername(e);
        if (!indb.notNull()) throw new ProjException(BasicRestCode.NOT_EXISTS);
        AccountRecover result = dio.findByKey(Builder.set("person", indb.getPerson()).to(new AccountRecover()));
        if (result.getPhone() != null) {
            result.setPhone("********" + result.getPhone().substring(result.getPhone().length() - 2));
        }
        if (result.getEmail() != null) {
            int atIndex = result.getEmail().indexOf("@");
            result.setEmail(result.getEmail().substring(0, Math.min(2, atIndex)) + "***@" + result.getEmail().substring(atIndex+1, atIndex+2) + "***");
        }
        result.setA1(null);
        result.setA2(null);
        result.setA3(null);
        return result;
    }

    // 忘记密码：2. 客户端填充完整信息。服务端发送code
    public String forgetPasswordS2(AccountRecover e) throws ProjException {
        AccountRecover indb = dio.findById(e);
        // 在过期前，只能发三次
        if (Instant.now().isBefore(indb.getCodeExp()) && indb.getCodeCount() == 3) throw new ProjException(ProjectRestCode.CODE_TOO_MANY);
        if (
            (e.getPhone() == null || !e.getPhone().equals(indb.getPhone()))
            && (e.getEmail() == null || !e.getEmail().equals(indb.getEmail()))
            && (e.getA1() == null || !e.getA1().equals(indb.getA1()))
            && (e.getA2() == null || !e.getA2().equals(indb.getA2()))
            && (e.getA3() == null || !e.getA3().equals(indb.getA3()))
        ) throw new ProjException(ProjectRestCode.ACCOUNT_NOT_MATCH);
        String code = RandomStringUtils.randomAlphanumeric(50);
        Builder.set("code", code).set("codeExp", Instant.now().plus(10, ChronoUnit.MINUTES)).set("codeCount", Instant.now().isBefore(indb.getCodeExp()) ? indb.getCodeCount()+1 : 1).to(indb);
        dio.update(indb);
        return code;
    }

    // 忘记密码: 3. 新密码
    /**
     * account.password 新密码
     * @param e
     * @param account
     * @return
     * @throws ProjException
     */
    public long forgetPasswordS3(AccountRecover e, Account account) throws ProjException {
        // 通过激活的code 找到对应的 accountRecover
        AccountRecover indb = dio.findById(e);
        if (indb.getId() == null) throw new ProjException(ProjectRestCode.CODE_NOT_MATCH);
        if (!indb.getCode().equals(e.getCode())) throw new ProjException(ProjectRestCode.CODE_NOT_MATCH);
        if (Instant.now().isAfter(indb.getCodeExp())) throw new ProjException(ProjectRestCode.CODE_EXPIRED);
        indb.cleanCode();
        dio.update(indb);
        return accountDio.updateByKey(Builder.set("person", indb.getPerson()).set("password", SecurityUtil.hash(account.getPassword())).to(new Account()));
    }

}
