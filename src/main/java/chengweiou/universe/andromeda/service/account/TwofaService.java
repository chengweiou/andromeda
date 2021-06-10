package chengweiou.universe.andromeda.service.account;


import java.util.List;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface TwofaService {
    void save(Twofa e) throws FailException, ProjException;
    void delete(Twofa e) throws FailException;

    long update(Twofa e);
    long updateByPerson(Twofa e);

    Twofa findById(Twofa e);
    Twofa findByPerson(Twofa e);

    long count(SearchCondition searchCondition, Twofa sample);
    List<Twofa> find(SearchCondition searchCondition, Twofa sample);

}
