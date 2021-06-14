package chengweiou.universe.andromeda.service.account;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.AccountNew;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;


@Service
public class AccountNewServiceImpl implements AccountNewService {
    @Autowired
    private AccountNewDio dio;
    @Autowired
    private TwofaDio twofaDio;

    public void save(AccountNew e) throws FailException, ProjException {
        e.setPassword(SecurityUtil.hash(e.getPassword()));
        dio.save(e);
    }

    @Override
    public void delete(AccountNew e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(AccountNew e) throws ProjException {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.update(e);
    }

    @Override
    public long updateByPerson(AccountNew e) throws ProjException {
        if (e.getPassword() != null) e.setPassword(SecurityUtil.hash(e.getPassword()));
        return dio.updateByPerson(e);
    }

    @Override
    public AccountNew findById(AccountNew e) {
        return dio.findById(e);
    }

    @Override
    public long countByLoginUsername(AccountNew e) {
        return dio.countByLoginUsername(e);
    }
    @Override
    public AccountNew login(AccountNew e) throws ProjException {
        AccountNew indb = dio.findByLoginUsername(e);
        if (indb.getId() == null) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        if (!indb.getActive()) throw new ProjException(ProjectRestCode.ACCOUNT_INACTIVE);
        if (!SecurityUtil.check(e.getPassword(), indb.getPassword())) throw new ProjException(ProjectRestCode.USERNAME_PASSWORD_MISMATCH);
        return indb;
    }

    @Override
    public long countByUsernameOfOther(AccountNew e) {
        return dio.countByUsernameOfOther(e);
    }
    @Override
    public long countByPhoneOfOther(AccountNew e) {
        return dio.countByPhoneOfOther(e);
    }
    @Override
    public long countByEmailOfOther(AccountNew e) {
        return dio.countByEmailOfOther(e);
    }

    @Override
    public AccountNew findAfterCheckCode(Twofa twofa) throws ProjException {
        Twofa twofaIndb = twofaDio.findByTokenAndCode(twofa);
        if (twofaIndb.getId() == null) throw new ProjException(ProjectRestCode.TWOFA_CODE_NOT_MATCH);
        if (twofaIndb.getUpdateAt().plusMinutes(1).isBefore(LocalDateTime.now(ZoneId.of("UTC")))) throw new ProjException(ProjectRestCode.TWOFA_EXPIRED);

        // todo 看看这里还需不需要
        // AccountNew result = dio.findById(twofaIndb.getLoginAccount());
        AccountNew result = dio.findById(Builder.set("id", twofaIndb.getLoginAccount().getId()).to(new AccountNew()));
        twofaIndb.cleanCode();
        twofaDio.update(twofaIndb);
        return result;
    }

    @Override
    public long count(SearchCondition searchCondition, AccountNew sample) {
        return dio.count(searchCondition, sample);
    }

    @Override
    public List<AccountNew> find(SearchCondition searchCondition, AccountNew sample) {
        return dio.find(searchCondition, sample);
    }
}
