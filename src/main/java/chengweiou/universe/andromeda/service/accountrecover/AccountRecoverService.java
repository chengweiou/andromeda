package chengweiou.universe.andromeda.service.accountrecover;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

@Service
public class AccountRecoverService {
    @Autowired
    private AccountRecoverDio dio;

    public void save(AccountRecover e) throws FailException, ProjException {
        dio.save(e);
    }

    public void delete(AccountRecover e) throws FailException {
        dio.delete(e);
    }

    public long update(AccountRecover e) {
        return dio.update(e);
    }
    public long updateByPerson(AccountRecover e) {
        return dio.updateByPerson(e);
    }

    public AccountRecover findById(AccountRecover e) {
        return dio.findById(e);
    }

    public long countByKey(AccountRecover e) {
        return dio.countByKey(e);
    }
    public AccountRecover findByKey(AccountRecover e) {
        return dio.findByKey(e);
    }

    public AccountRecover findByActiveCode(AccountRecover e) throws ProjException {
        AccountRecover result = dio.findById(e);
        if (result.getId() == null) throw new ProjException(ProjectRestCode.CODE_NOT_MATCH);
        if (!result.getCode().equals(e.getCode())) throw new ProjException(ProjectRestCode.CODE_NOT_MATCH);
        if (LocalDateTime.now(ZoneId.of("UTC")).isAfter(result.getCodeExp())) throw new ProjException(ProjectRestCode.CODE_EXPIRED);
        result.cleanCode();
        dio.update(result);
        return result;
    }

    public long count(SearchCondition searchCondition, AccountRecover sample) {
        return dio.count(searchCondition, sample);
    }
    public List<AccountRecover> find(SearchCondition searchCondition, AccountRecover sample) {
        return dio.find(searchCondition, sample);
    }

}
