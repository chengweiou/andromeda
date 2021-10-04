package chengweiou.universe.andromeda.service.twofa;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;


@Service
public class TwofaService {
    @Autowired
    private TwofaDio dio;

    public void save(Twofa e) throws FailException, ProjException {
        dio.save(e);
    }

    public void delete(Twofa e) throws FailException {
        dio.delete(e);
    }

    public long update(Twofa e) {
        return dio.update(e);
    }

    public long updateByPerson(Twofa e) {
        return dio.updateByPerson(e);
    }

    public Twofa findById(Twofa e) {
        return dio.findById(e);
    }
    public Twofa findByPerson(Twofa e) {
        return dio.findByPerson(e);
    }

    public long count(SearchCondition searchCondition, Twofa sample) {
        return dio.count(searchCondition, sample);
    }

    public List<Twofa> find(SearchCondition searchCondition, Twofa sample) {
        return dio.find(searchCondition, sample);
    }
}
