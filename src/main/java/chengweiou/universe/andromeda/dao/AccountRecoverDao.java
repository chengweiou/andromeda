package chengweiou.universe.andromeda.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover.Dto;

@Repository
@Mapper
public interface AccountRecoverDao extends BaseDao<Dto> {
    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(Dto e);
    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<Dto> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    class Sql {

        public String updateByPerson(final Dto e) {
            return new SQL() {{
                UPDATE("accountRecover");
                if (e.getPhone() != null) SET("phone = #{phone}");
                if (e.getEmail() != null) SET("email = #{email}");
                if (e.getQ1() != null) SET("q1 = #{q1}");
                if (e.getQ2() != null) SET("q2 = #{q2}");
                if (e.getQ3() != null) SET("q3 = #{q3}");
                if (e.getA1() != null) SET("a1 = #{a1}");
                if (e.getA2() != null) SET("a2 = #{a2}");
                if (e.getA3() != null) SET("a3 = #{a3}");
                if (e.getCode() != null) SET("code = #{code}");
                if (e.getCodeExp() != null) SET("codeExp = #{codeExp}");
                if (e.getCodeCount() != null) SET("codeCount = #{codeCount}");
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
                FROM("accountRecover");
                if (searchCondition.getK() != null) WHERE("phone LIKE #{searchCondition.like.k}")
                        .OR().WHERE("email LIKE #{searchCondition.like.k}");
                if (sample != null) {
                }
            }};
        }
    }
}
