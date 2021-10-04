package chengweiou.universe.andromeda.service.account;


import java.util.List;
import java.util.stream.Collectors;

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
        Account.Dto dto = e.toDto();
        long count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(Account e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(Account e) throws ProjException {
        checkDupKey(e);
        e.updateAt();
        return dao.update(e.toDto());
    }

    public long updateByPerson(Account e) throws ProjException {
        checkDupKey(e);
        e.updateAt();
        return dao.updateByPerson(e.toDto());
    }

    private void checkDupKey(Account e) throws ProjException {
        if (e.getUsername() != null && !e.getUsername().isEmpty()) {
            long count = dao.countByUsernameOfOther(e.toDto());
            if (count != 0) throw new ProjException("dup key: " + e.getUsername() + " exists", BasicRestCode.EXISTS);
        }
        if (e.getPhone() != null && !e.getPhone().isEmpty()) {
            long count = dao.countByPhoneOfOther(e.toDto());
            if (count != 0) throw new ProjException("dup key: " + e.getPhone() + " exists", BasicRestCode.EXISTS);
        }
        if (e.getEmail() != null && !e.getEmail().isEmpty()) {
            long count = dao.countByEmailOfOther(e.toDto());
            if (count != 0) throw new ProjException("dup key: " + e.getEmail() + " exists", BasicRestCode.EXISTS);
        }
    }

    public Account findById(Account e) {
        Account.Dto result = dao.findById(e.toDto());
        if (result == null) return Account.NULL;
        return result.toBean();
    }
    public Account findByPerson(Account e) {
        Account.Dto result = dao.findByPerson(e.toDto());
        if (result == null) return Account.NULL;
        return result.toBean();
    }

    public long countByUsername(Account e) {
        return dao.countByUsername(e.toDto());
    }
    public Account findByUsername(Account e) {
        Account.Dto result = dao.findByUsername(e.toDto());
        if (result == null) return Account.NULL;
        return result.toBean();
    }
    public long countByPhone(Account e) {
        return dao.countByPhone(e.toDto());
    }
    public Account findByPhone(Account e) {
        Account.Dto result = dao.findByPhone(e.toDto());
        if (result == null) return Account.NULL;
        return result.toBean();
    }
    public long countByEmail(Account e) {
        return dao.countByEmail(e.toDto());
    }
    public Account findByEmail(Account e) {
        Account.Dto result = dao.findByEmail(e.toDto());
        if (result == null) return Account.NULL;
        return result.toBean();
    }

    /**
     * 用户名可能是：username，phone, email 或者是 phone, email 放在 username 的位置
     * @param e
     * @return
     */
    public long countByLoginUsername(Account e) {
        return dao.countByLoginUsername(e.toDto());
    }
    /**
    * 用户名可能是：username，phone, email 或者是 phone, email 放在 username 的位置
    * @param e
    * @return
    */
    public Account findByLoginUsername(Account e) {
        Account.Dto result = dao.findByLoginUsername(e.toDto());
        if (result == null) return Account.NULL;
        return result.toBean();
    }

    public long countByUsernameOfOther(Account e) {
        return dao.countByUsernameOfOther(e.toDto());
    }
    public long countByPhoneOfOther(Account e) {
        return dao.countByPhoneOfOther(e.toDto());
    }
    public long countByEmailOfOther(Account e) {
        return dao.countByEmailOfOther(e.toDto());
    }

    public long count(SearchCondition searchCondition, Account sample) {
        return dao.count(searchCondition, sample!=null ? sample.toDto() : null);
    }

    public List<Account> find(SearchCondition searchCondition, Account sample) {
        searchCondition.setDefaultSort("createAt");
        List<Account.Dto> dtoList = dao.find(searchCondition, sample!=null ? sample.toDto() : null);
        List<Account> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }



}
