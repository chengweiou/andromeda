package chengweiou.universe.andromeda.service.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.AccountDao;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.Account.Dto;
import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;


@Component
public class AccountDio extends BaseDio<Account, Account.Dto> {
    @Autowired
    private AccountDao dao;
    @Override
    protected AccountDao getDao() { return dao; }
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
                (username LIKE #{searchCondition.like.k}
                or phone LIKE #{searchCondition.like.k}
                or email LIKE #{searchCondition.like.k})
                """);
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
            }
        }}.toString();
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

}
