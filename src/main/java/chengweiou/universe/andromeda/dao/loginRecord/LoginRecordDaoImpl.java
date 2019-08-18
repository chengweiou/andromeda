package chengweiou.universe.andromeda.dao.loginRecord;


import chengweiou.universe.andromeda.base.dao.BaseDaoImpl;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginRecordDaoImpl extends BaseDaoImpl<LoginRecord> implements LoginRecordDao {
    @Autowired
    private MongoTemplate template;

    @Override
    public long update(LoginRecord e) {
        Update update = new Update();
        if (e.getIp() != null) update.set("ip", e.getIp());
        if (e.getPlatform() != null) update.set("platform", e.getPlatform());
        if (e.getLoginTime() != null) update.set("loginTime", e.getLoginTime());
        if (e.getLogoutTime() != null) update.set("logoutTime", e.getLogoutTime());
        return template.updateFirst(new Query(Criteria.where("id").is(e.getId())), update, e.getClass()).getModifiedCount();
    }

    @Override
    public LoginRecord findLastByAccount(Account account) {
        Query query = new Query();
        query.addCriteria(Criteria.where("account.id").is(account.getId()));
        query.with(Sort.by(Sort.Direction.DESC, "updateAt"));
        return template.findOne(query, LoginRecord.class);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        Query query = new Query();
        if (searchCondition.getK() != null) query.addCriteria(new Criteria().orOperator(
                Criteria.where("ip").regex(searchCondition.getFull().getReg().getPattern()),
                Criteria.where("platform").regex(searchCondition.getFull().getReg().getPattern())
        ));
        return template.count(query, LoginRecord.class);
    }

    @Override
    public List<LoginRecord> find(SearchCondition searchCondition) {
        Query query = new Query();
        if (searchCondition.getK() != null) query.addCriteria(new Criteria().orOperator(
                Criteria.where("ip").regex(searchCondition.getFull().getReg().getPattern()),
                Criteria.where("platform").regex(searchCondition.getFull().getReg().getPattern())
        ));
        query.skip(searchCondition.getSkip()).limit(searchCondition.getLimit()).with(searchCondition.getMongoSort());
        return template.find(query, LoginRecord.class);
    }

    @Override
    public long countByPerson(SearchCondition searchCondition, Person person) {
        Query query = new Query();
        if (searchCondition.getK() != null) query.addCriteria(new Criteria().orOperator(
                Criteria.where("ip").regex(searchCondition.getFull().getReg().getPattern()),
                Criteria.where("platform").regex(searchCondition.getFull().getReg().getPattern())
        ));
        query.addCriteria(Criteria.where("person.id").is(person.getId()));
        return template.count(query, LoginRecord.class);
    }

    @Override
    public List<LoginRecord> findByPerson(SearchCondition searchCondition, Person person) {
        Query query = new Query();
        if (searchCondition.getK() != null) query.addCriteria(new Criteria().orOperator(
                Criteria.where("ip").regex(searchCondition.getFull().getReg().getPattern()),
                Criteria.where("platform").regex(searchCondition.getFull().getReg().getPattern())
        ));
        query.addCriteria(Criteria.where("person.id").is(person.getId()));
        query.skip(searchCondition.getSkip()).limit(searchCondition.getLimit()).with(searchCondition.getMongoSort());
        return template.find(query, LoginRecord.class);
    }

}
