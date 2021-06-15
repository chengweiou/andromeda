package chengweiou.universe.andromeda.service.accountrecover;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.AccountRecoverDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;

@Component
public class AccountRecoverDio {
    @Autowired
    private AccountRecoverDao dao;

    public void save(AccountRecover e) throws FailException, ProjException {
        long count = dao.countByPerson(e);
        if (count != 0) throw new ProjException("dup key: " + e.getPerson().getId() + " exists", BasicRestCode.EXISTS);
        e.cleanCode();
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(AccountRecover e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(AccountRecover e) {
        e.updateAt();
        return dao.update(e);
    }

    public AccountRecover findById(AccountRecover e) {
        return dao.findById(e);
    }

    public long countByPerson(AccountRecover e) {
        return dao.countByPerson(e);
    }
    public AccountRecover findByPerson(AccountRecover e) {
        return dao.findByPerson(e);
    }

    public long count(SearchCondition searchCondition, AccountRecover sample) {
        return dao.count(searchCondition, sample);
    }
    public List<AccountRecover> find(SearchCondition searchCondition, AccountRecover sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }
}
