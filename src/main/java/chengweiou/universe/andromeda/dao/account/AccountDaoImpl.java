package chengweiou.universe.andromeda.dao.account;


import chengweiou.universe.andromeda.base.dao.BaseDaoImpl;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl extends BaseDaoImpl<Account> implements AccountDao {
    @Autowired
    private MongoTemplate template;

    @Override
    public long update(Account e) {
        Update update = new Update();
        if (e.getUsername() != null) update.set("username", e.getUsername());
        if (e.getPerson() != null) update.set("person", e.getPerson());
        if (e.getActive() != null) update.set("active", e.getActive());
        if (e.getExtra() != null) update.set("extra", e.getExtra());
        return template.updateFirst(new Query(Criteria.where("id").is(e.getId())), update, e.getClass()).getModifiedCount();
    }
    @Override
    public long updateByPerson(Account e) {
        Update update = new Update();
        if (e.getUsername() != null) update.set("username", e.getUsername());
        if (e.getActive() != null) update.set("active", e.getActive());
        if (e.getExtra() != null) update.set("extra", e.getExtra());
        return template.updateMulti(new Query(Criteria.where("person.id").is(e.getPerson().getId())), update, e.getClass()).getModifiedCount();
    }

    @Override
    public long count(SearchCondition searchCondition) {
        Query query = new Query();
        if (searchCondition.getK() != null) query.addCriteria(Criteria.where("username").regex(searchCondition.getFull().getReg().getPattern()));
        return template.count(query, Account.class);
    }

    @Override
    public List<Account> find(SearchCondition searchCondition) {
        Query query = new Query();
        if (searchCondition.getK() != null) query.addCriteria(Criteria.where("username").regex(searchCondition.getFull().getReg().getPattern()));
        query.skip(searchCondition.getSkip()).limit(searchCondition.getLimit()).with(searchCondition.getMongoSort());
        return template.find(query, Account.class);
    }

    @Override
    public long countByUsername(Account e) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(e.getUsername()));
        return template.count(query, Account.class);
    }

    @Override
    public Account findByUsername(Account e) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(e.getUsername()));
        return template.findOne(query, Account.class);
    }

}
