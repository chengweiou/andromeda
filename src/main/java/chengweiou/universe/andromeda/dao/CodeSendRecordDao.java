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
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord.Dto;

@Repository
@Mapper
public interface CodeSendRecordDao extends BaseDao<Dto> {

    @Select("select * from codeSendRecord where username=#{username} order by updateAt desc limit 1")
    Dto findLastByUsername(Dto e);

    @Select("select * from codeSendRecord where username=#{e.username} order by updateAt desc limit 1, offset #{skip}")
    Dto findOffsetByUsername(@Param("e")Dto e, @Param("skip")int skip);

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
                FROM("codeSendRecord");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = #{sample.type}");
                }
            }};
        }
    }
}
