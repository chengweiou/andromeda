package chengweiou.universe.andromeda.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord.Dto;

@Repository
@Mapper
public interface LoginRecordDao extends BaseDao<Dto> {

    @Select("select * from loginRecord where personId=#{personId} order by updateAt desc limit 1")
    Dto findLastByPerson(Dto e);

    class Sql {

    }
}
