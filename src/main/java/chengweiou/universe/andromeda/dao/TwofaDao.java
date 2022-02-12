package chengweiou.universe.andromeda.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa.Dto;

@Repository
@Mapper
public interface TwofaDao extends BaseDao<Dto> {
    @Select("select * from twofa where token=#{token} and code=#{code}")
    Dto findByTokenAndCode(Dto e);

    class Sql {
    }
}
