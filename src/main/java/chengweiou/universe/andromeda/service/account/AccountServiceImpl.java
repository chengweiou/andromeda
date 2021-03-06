package chengweiou.universe.andromeda.service.account;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.twofa.TwofaDio;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDio dio;
    @Autowired
    private AccountRecoverDio accountRecoverDio;
    @Autowired
    private TwofaDio twofaDio;

    public void save(Account e) throws FailException, ProjException {
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        dio.save(e);
        // todo 还有udpate的时候，dao的id插入, delete的时候
        AccountRecover accountRecover = Builder.set("id", e.getId()).set("person", e.getPerson()).set("phone", e.getPhone()).set("email", e.getEmail()).to(new AccountRecover());
        accountRecoverDio.save(accountRecover);
    }

    @Override
    public void delete(Account e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Account e) throws ProjException {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.update(e);
    }

    @Override
    public long updateByPerson(Account e) throws ProjException {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.updateByPerson(e);
    }

    @Override
    public Account findById(Account e) {
        return dio.findById(e);
    }

    @Override
    public Account findByPerson(Account e) {
        return dio.findByPerson(e);
    }

    @Override
    public long countByLoginUsername(Account e) {
        return dio.countByLoginUsername(e);
    }
    @Override
    public Account findByLoginUsername(Account e) {
        return dio.findByLoginUsername(e);
    }
    @Override
    public Account login(Account e) throws ProjException {
        Account indb = dio.findByLoginUsername(e);
        if (indb.getId() == null) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        if (!indb.getActive()) throw new ProjException(ProjectRestCode.ACCOUNT_INACTIVE);
        if (!SecurityUtil.check(e.getPassword(), indb.getPassword())) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        return indb;
    }

    @Override
    public long countByUsernameOfOther(Account e) {
        return dio.countByUsernameOfOther(e);
    }
    @Override
    public long countByPhoneOfOther(Account e) {
        return dio.countByPhoneOfOther(e);
    }
    @Override
    public long countByEmailOfOther(Account e) {
        return dio.countByEmailOfOther(e);
    }

    @Override
    public Account findAfterCheckCode(Twofa twofa) throws ProjException {
        Twofa twofaIndb = twofaDio.findByTokenAndCode(twofa);
        if (twofaIndb.getId() == null) throw new ProjException(ProjectRestCode.TWOFA_CODE_NOT_MATCH);
        if (twofaIndb.getUpdateAt().plusMinutes(1).isBefore(LocalDateTime.now(ZoneId.of("UTC")))) throw new ProjException(ProjectRestCode.TWOFA_EXPIRED);

        Account result = dio.findByPerson(Builder.set("person", twofaIndb.getPerson()).to(new Account()));
        twofaIndb.cleanCode();
        twofaDio.update(twofaIndb);
        return result;
    }

    @Override
    public long count(SearchCondition searchCondition, Account sample) {
        return dio.count(searchCondition, sample);
    }

    @Override
    public List<Account> find(SearchCondition searchCondition, Account sample) {
        return dio.find(searchCondition, sample);
    }
}
