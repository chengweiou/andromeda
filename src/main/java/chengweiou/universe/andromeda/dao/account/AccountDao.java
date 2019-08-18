package chengweiou.universe.andromeda.dao.account;


import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.entity.Account;

public interface AccountDao extends BaseDao<Account> {
    long update(Account e);

    long updateByPerson(Account e);

    long countByUsername(Account e);
    Account findByUsername(Account e);
}
