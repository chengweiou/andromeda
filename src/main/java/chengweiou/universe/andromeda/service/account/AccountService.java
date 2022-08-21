package chengweiou.universe.andromeda.service.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;

@Service
public class AccountService {
    @Autowired
    private AccountDio dio;
    @Autowired
    private AccountRecoverDio accountRecoverDio;

    public void save(Account e) throws FailException, ProjException {
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        dio.save(e);
        AccountRecover accountRecover = Builder.set("id", e.getId()).set("person", e.getPerson()).set("phone", e.getPhone()).set("email", e.getEmail()).to(new AccountRecover());
        accountRecover.cleanCode();
        accountRecoverDio.save(accountRecover);
    }

    public void delete(Account e) throws FailException {
        dio.delete(e);
        AccountRecover accountRecover = Builder.set("id", e.getId()).to(new AccountRecover());
        accountRecoverDio.delete(accountRecover);
    }

    public long update(Account e) throws FailException {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.update(e);
    }

    public long updateByKey(Account e) throws FailException {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.updateByKey(e);
    }

    public Account login(Account e) throws ProjException {
        Account indb = dio.findByLoginUsername(e);
        if (indb.getId() == null) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        if (!indb.getActive()) throw new ProjException(ProjectRestCode.ACCOUNT_INACTIVE);
        if (!SecurityUtil.check(e.getPassword(), indb.getPassword())) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        return indb;
    }

    /**
     *
     * @param e 需要 account.person
     * @return
     * @throws ProjException
     * @throws FailException
     */
    public long changePassword(Account e) throws ProjException, FailException {
        Account indb = dio.findByKey(e);
        boolean success = SecurityUtil.check(e.getOldPassword(), indb.getPassword());
        if (!success) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        return dio.update(Builder.set("id", indb.getId()).set("password", SecurityUtil.hash(e.getPassword())).to(new Account()));
    }
}
