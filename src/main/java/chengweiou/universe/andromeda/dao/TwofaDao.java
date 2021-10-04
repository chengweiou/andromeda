package chengweiou.universe.andromeda.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa.Dto;

@Repository
@Mapper
public interface TwofaDao extends BaseDao<Dto> {

    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(Dto e);

    @Select("select * from twofa where personId=#{personId}")
    Dto findByPerson(Dto e);

    @Select("select * from twofa where token=#{token} and code=#{code}")
    Dto findByTokenAndCode(Dto e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<Dto> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    @Select("select count(*) from twofa where personId=#{personId}")
    long countByPerson(Dto e);

    class Sql {

        public String updateByPerson(final Dto e) {
            return new SQL() {{
                UPDATE("twofa");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getCodeTo() != null) SET("codeTo = #{codeTo}");
                if (e.getToken() != null) SET("token = #{token}");
                if (e.getCode() != null) SET("code = #{code}");
                if (e.getCodeExp() != null) SET("codeExp = #{codeExp}");
                SET("updateAt = #{updateAt}");
                WHERE("personId=#{personId}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, Dto sample) {
            return new SQL() {{
                FROM("twofa");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = #{sample.type}");
                }
            }};
        }
    }
}
