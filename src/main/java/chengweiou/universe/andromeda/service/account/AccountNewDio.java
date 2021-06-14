package chengweiou.universe.andromeda.service.account;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.AccountNewDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.AccountNew;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;


@Component
public class AccountNewDio {
    @Autowired
    private AccountNewDao dao;

    public void save(AccountNew e) throws ProjException, FailException {
        checkDupKey(e);
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(AccountNew e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(AccountNew e) throws ProjException {
        checkDupKey(e);
        e.updateAt();
        return dao.update(e);
    }

    public long updateByPerson(AccountNew e) throws ProjException {
        checkDupKey(e);
        e.updateAt();
        return dao.updateByPerson(e);
    }

    private void checkDupKey(AccountNew e) throws ProjException {
        if (e.getUsername() != null && !e.getUsername().isEmpty()) {
            long count = dao.countByUsernameOfOther(e);
            if (count != 0) throw new ProjException("dup key: " + e.getUsername() + " exists", BasicRestCode.EXISTS);
        }
        if (e.getPhone() != null && !e.getPhone().isEmpty()) {
            long count = dao.countByPhoneOfOther(e);
            if (count != 0) throw new ProjException("dup key: " + e.getPhone() + " exists", BasicRestCode.EXISTS);
        }
        if (e.getEmail() != null && !e.getEmail().isEmpty()) {
            long count = dao.countByEmailOfOther(e);
            if (count != 0) throw new ProjException("dup key: " + e.getEmail() + " exists", BasicRestCode.EXISTS);
        }
    }

    public AccountNew findById(AccountNew e) {
        AccountNew result = dao.findById(e);
        return result != null ? result : AccountNew.NULL;
    }
    public AccountNew findByPerson(AccountNew e) {
        AccountNew result = dao.findByPerson(e);
        return result != null ? result : AccountNew.NULL;
    }

    public long countByUsername(AccountNew e) {
        return dao.countByUsername(e);
    }
    public AccountNew findByUsername(AccountNew e) {
        AccountNew result = dao.findByUsername(e);
        return result != null ? result : AccountNew.NULL;
    }
    public long countByPhone(AccountNew e) {
        return dao.countByPhone(e);
    }
    public AccountNew findByPhone(AccountNew e) {
        AccountNew result = dao.findByPhone(e);
        return result != null ? result : AccountNew.NULL;
    }
    public long countByEmail(AccountNew e) {
        return dao.countByEmail(e);
    }
    public AccountNew findByEmail(AccountNew e) {
        AccountNew result = dao.findByEmail(e);
        return result != null ? result : AccountNew.NULL;
    }

    /**
     * 用户名可能是：username，phone, email 或者是 phone, email 放在 username 的位置
     * @param e
     * @return
     */
    public long countByLoginUsername(AccountNew e) {
        return dao.countByLoginUsername(e);
    }    
    /**
    * 用户名可能是：username，phone, email 或者是 phone, email 放在 username 的位置
    * @param e
    * @return
    */
    public AccountNew findByLoginUsername(AccountNew e) {
        AccountNew result = dao.findByLoginUsername(e);
        return result != null ? result : AccountNew.NULL;
    }

    public long countByUsernameOfOther(AccountNew e) {
        return dao.countByUsernameOfOther(e);
    }   
    public long countByPhoneOfOther(AccountNew e) {
        return dao.countByPhoneOfOther(e);
    }   
    public long countByEmailOfOther(AccountNew e) {
        return dao.countByEmailOfOther(e);
    }   

    public long count(SearchCondition searchCondition, AccountNew sample) {
        return dao.count(searchCondition, sample);
    }

    public List<AccountNew> find(SearchCondition searchCondition, AccountNew sample) {
        searchCondition.setDefaultSort("createAt");
        return dao.find(searchCondition, sample);
    }



}
