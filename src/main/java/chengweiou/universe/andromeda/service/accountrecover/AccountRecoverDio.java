package chengweiou.universe.andromeda.service.accountrecover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.AccountRecoverDao;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover.Dto;
import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;

@Component
public class AccountRecoverDio extends BaseDio<AccountRecover, AccountRecover.Dto> {
    @Autowired
    private AccountRecoverDao dao;
    @Override
    protected AccountRecoverDao getDao() { return dao; }
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
            (phone LIKE #{searchCondition.like.k} or email LIKE #{searchCondition.like.k})
            """);
            if (sample != null) {
            }
        }}.toString();
    }
}
