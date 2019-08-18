package chengweiou.universe.andromeda.base.dao;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.blackhole.exception.FailException;

import java.util.List;

public interface BaseDao<T> {
    void save(T e) throws FailException;
    long delete(T e);
    T findById(T e);
    long count(SearchCondition searchCondition);
    List<T> find(SearchCondition searchCondition);
}
