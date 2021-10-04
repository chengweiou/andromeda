package chengweiou.universe.andromeda.service.accountrecover;

import java.util.List;
import java.util.stream.Collectors;

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

        long count = dao.countByKey(e.toDto());
        if (count != 0) throw new ProjException("dup key: " + e.getPerson().getId() + " exists", BasicRestCode.EXISTS);
        e.cleanCode();
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        AccountRecover.Dto dto = e.toDto();
        count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(AccountRecover e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(AccountRecover e) {
        e.updateAt();
        return dao.update(e.toDto());
    }
    public long updateByPerson(AccountRecover e) {
        e.updateAt();
        return dao.updateByPerson(e.toDto());
    }

    public AccountRecover findById(AccountRecover e) {
        AccountRecover.Dto result = dao.findById(e.toDto());
        if (result == null) return AccountRecover.NULL;
        return result.toBean();
    }

    public long countByKey(AccountRecover e) {
        return dao.countByKey(e.toDto());
    }
    public AccountRecover findByKey(AccountRecover e) {
        AccountRecover.Dto result = dao.findByKey(e.toDto());
        if (result == null) return AccountRecover.NULL;
        return result.toBean();
    }

        public long count(SearchCondition searchCondition, AccountRecover sample) {
            return dao.count(searchCondition, sample!=null ? sample.toDto() : null);
        }

        public List<AccountRecover> find(SearchCondition searchCondition, AccountRecover sample) {
            searchCondition.setDefaultSort("updateAt");
            List<AccountRecover.Dto> dtoList = dao.find(searchCondition, sample!=null ? sample.toDto() : null);
            List<AccountRecover> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
            return result;
        }
}
