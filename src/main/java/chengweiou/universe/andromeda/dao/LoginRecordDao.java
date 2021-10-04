package chengweiou.universe.andromeda.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord.Dto;

@Repository
@Mapper
public interface LoginRecordDao extends BaseDao<Dto> {

    @Select("select * from loginRecord where personId=#{personId} order by updateAt desc limit 1")
    Dto findLastByPerson(Dto e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<Dto> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    class Sql {

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, Dto sample) {
            return new SQL() {{
                FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getPersonId() != null) WHERE("personId=#{sample.personId}");
                }
            }};
        }
    }
}
