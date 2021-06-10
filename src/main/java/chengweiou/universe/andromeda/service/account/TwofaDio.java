package chengweiou.universe.andromeda.service.account;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.TwofaDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;


@Component
public class TwofaDio {
    @Autowired
    private TwofaDao dao;

    public void save(Twofa e) throws ProjException, FailException {
        long count = dao.countByPerson(e);
        if (count != 0) throw new ProjException("dup key: " + e.getPerson() + " exists", BasicRestCode.EXISTS);
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(Twofa e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(Twofa e) {
        e.updateAt();
        return dao.update(e);
    }

    public long updateByPerson(Twofa e) {
        e.updateAt();
        return dao.updateByPerson(e);
    }

    public Twofa findById(Twofa e) {
        Twofa result = dao.findById(e);
        return result != null ? result : Twofa.NULL;
    }

    public Twofa findByPerson(Twofa e) {
        Twofa result = dao.findByPerson(e);
        return result != null ? result : Twofa.NULL;
    }

    public Twofa findByTokenAndCode(Twofa e) {
        Twofa result = dao.findByTokenAndCode(e);
        return result != null ? result : Twofa.NULL;
    }

    public long count(SearchCondition searchCondition, Twofa sample) {
        return dao.count(searchCondition, sample);
    }

    public List<Twofa> find(SearchCondition searchCondition, Twofa sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }
}
