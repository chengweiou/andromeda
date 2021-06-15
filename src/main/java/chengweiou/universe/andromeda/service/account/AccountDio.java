package chengweiou.universe.andromeda.service.account;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.AccountDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;


@Component
public class AccountDio {
    @Autowired
    private AccountDao dao;

    public void save(Account e) throws ProjException, FailException {
        checkDupKey(e);
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(Account e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(Account e) throws ProjException {
        checkDupKey(e);
        e.updateAt();
        return dao.update(e);
    }

    public long updateByPerson(Account e) throws ProjException {
        checkDupKey(e);
        e.updateAt();
        return dao.updateByPerson(e);
    }

    private void checkDupKey(Account e) throws ProjException {
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

    public Account findById(Account e) {
        Account result = dao.findById(e);
        return result != null ? result : Account.NULL;
    }
    public Account findByPerson(Account e) {
        Account result = dao.findByPerson(e);
        return result != null ? result : Account.NULL;
    }

    public long countByUsername(Account e) {
        return dao.countByUsername(e);
    }
    public Account findByUsername(Account e) {
        Account result = dao.findByUsername(e);
        return result != null ? result : Account.NULL;
    }
    public long countByPhone(Account e) {
        return dao.countByPhone(e);
    }
    public Account findByPhone(Account e) {
        Account result = dao.findByPhone(e);
        return result != null ? result : Account.NULL;
    }
    public long countByEmail(Account e) {
        return dao.countByEmail(e);
    }
    public Account findByEmail(Account e) {
        Account result = dao.findByEmail(e);
        return result != null ? result : Account.NULL;
    }

    /**
     * 用户名可能是：username，phone, email 或者是 phone, email 放在 username 的位置
     * @param e
     * @return
     */
    public long countByLoginUsername(Account e) {
        return dao.countByLoginUsername(e);
    }    
    /**
    * 用户名可能是：username，phone, email 或者是 phone, email 放在 username 的位置
    * @param e
    * @return
    */
    public Account findByLoginUsername(Account e) {
        Account result = dao.findByLoginUsername(e);
        return result != null ? result : Account.NULL;
    }

    public long countByUsernameOfOther(Account e) {
        return dao.countByUsernameOfOther(e);
    }   
    public long countByPhoneOfOther(Account e) {
        return dao.countByPhoneOfOther(e);
    }   
    public long countByEmailOfOther(Account e) {
        return dao.countByEmailOfOther(e);
    }   

    public long count(SearchCondition searchCondition, Account sample) {
        return dao.count(searchCondition, sample);
    }

    public List<Account> find(SearchCondition searchCondition, Account sample) {
        searchCondition.setDefaultSort("createAt");
        return dao.find(searchCondition, sample);
    }



}
