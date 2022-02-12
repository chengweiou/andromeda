package chengweiou.universe.andromeda.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover.Dto;

@Repository
@Mapper
public interface AccountRecoverDao extends BaseDao<Dto> {
}
