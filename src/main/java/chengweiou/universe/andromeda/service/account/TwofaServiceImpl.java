package chengweiou.universe.andromeda.service.account;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;


@Service
public class TwofaServiceImpl implements TwofaService {
    @Autowired
    private TwofaDio dio;

    public void save(Twofa e) throws FailException, ProjException {
        dio.save(e);
    }

    @Override
    public void delete(Twofa e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Twofa e) {
        return dio.update(e);
    }

    @Override
    public long updateByPerson(Twofa e) {
        return dio.updateByPerson(e);
    }

    @Override
    public Twofa findById(Twofa e) {
        return dio.findById(e);
    }
    @Override
    public Twofa findByPerson(Twofa e) {
        return dio.findByPerson(e);
    }

    @Override
    public long count(SearchCondition searchCondition, Twofa sample) {
        return dio.count(searchCondition, sample);
    }

    @Override
    public List<Twofa> find(SearchCondition searchCondition, Twofa sample) {
        return dio.find(searchCondition, sample);
    }
}
