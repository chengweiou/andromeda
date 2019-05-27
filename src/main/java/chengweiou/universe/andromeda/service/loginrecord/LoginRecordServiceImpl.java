package chengweiou.universe.andromeda.service.loginrecord;

import chengweiou.universe.andromeda.dao.LoginRecordDao;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordDao dao;

    public int save(LoginRecord e) {
        e.fillNotRequire();
        e.updateAt();
        return dao.save(e);
    }

    public int delete(LoginRecord e) {
        return dao.delete(e);
    }

    @Override
    public int count(SearchCondition searchCondition) {
        return dao.count(searchCondition);
    }
    @Override
    public List<LoginRecord> find(SearchCondition searchCondition) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition);
    }

    @Override
    public int count(SearchCondition searchCondition, Person person) {
        return dao.countByPerson(searchCondition, person);
    }
    @Override
    public List<LoginRecord> find(SearchCondition searchCondition, Person person) {
        searchCondition.setDefaultSort("updateAt");
        return dao.findByPerson(searchCondition, person);
    }

}
