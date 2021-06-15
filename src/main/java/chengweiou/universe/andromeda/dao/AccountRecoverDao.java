package chengweiou.universe.andromeda.dao;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;

@Repository
@Mapper
public interface AccountRecoverDao {
    @Insert("insert into accountRecover (personId, phone, email, q1, q2, q3, a1, a2, a3, code, codeExp, codeCount, createAt, updateAt) " +
            "values(#{person.id}, #{phone}, #{email}, #{q1}, #{q2}, #{q3}, #{a1}, #{a2}, #{a3}, #{code}, #{codeExp}, #{codeCount}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(AccountRecover e);

    @Delete("delete from accountRecover where id=#{id}")
    long delete(AccountRecover e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(AccountRecover e);
    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(AccountRecover e);

    @Select("select * from accountRecover where id=#{id}")
    @Results({ @Result(property = "person.id", column = "personId"), })
    AccountRecover findById(AccountRecover e);

    @Select("select count(*) from accountRecover where personId=#{person.id}")
    long countByPerson(AccountRecover e);
    @Select("select * from accountRecover where personId=#{person.id}")
    @Results({ @Result(property = "person.id", column = "personId"), })
    AccountRecover findByPerson(AccountRecover e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") AccountRecover sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({ @Result(property = "person.id", column = "personId"), })
    List<AccountRecover> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") AccountRecover sample);

    class Sql {

        public String update(final AccountRecover e) {
            return new SQL() {{
                UPDATE("accountRecover");
                if (e.getPerson() != null) SET("personId = #{person.id}");
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
                WHERE("id=#{id}");
            }}.toString();
        }
        public String updateByPerson(final AccountRecover e) {
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
                WHERE("personId=#{person.id}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final AccountRecover sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final AccountRecover sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, AccountRecover sample) {
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
