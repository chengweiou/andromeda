package chengweiou.universe.andromeda.service.account;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDio dio;
    @Autowired
    private TwofaDio twofaDio;

    public void save(Account e) throws FailException, ProjException {
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        dio.save(e);
    }

    @Override
    public void delete(Account e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Account e) {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.update(e);
    }

    @Override
    public long updateByPerson(Account e) {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.updateByPerson(e);
    }
    @Override
    public long updateByPersonAndType(Account e) {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.updateByPersonAndType(e);
    }

    @Override
    public Account findById(Account e) {
        return dio.findById(e);
    }

    @Override
    public Account login(Account e) throws ProjException {
        Account indb = dio.findByUsername(e);
        if (indb.getId() == null) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        if (!indb.getActive()) throw new ProjException(ProjectRestCode.ACCOUNT_INACTIVE);
        if (!SecurityUtil.check(e.getPassword(), indb.getPassword())) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        return indb;
    }

    @Override
    public Account findAfterCheckCode(Twofa twofa) throws ProjException {
        Twofa twofaIndb = twofaDio.findByTokenAndCode(twofa);
        if (twofaIndb.getId() == null) throw new ProjException(ProjectRestCode.TWOFA_CODE_NOT_MATCH);
        if (twofaIndb.getUpdateAt().plusMinutes(1).isBefore(LocalDateTime.now(ZoneId.of("UTC")))) throw new ProjException(ProjectRestCode.TWOFA_EXPIRED);

        Account result = dio.findById(twofaIndb.getLoginAccount());
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

    @Override
    public long countByUsername(Account e) {
        return dio.countByUsername(e);
    }
    @Override
    public Account findByUsername(Account e) {
        return dio.findByUsername(e);
    }
}
