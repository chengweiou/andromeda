package chengweiou.universe.andromeda.dao;


import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountDao {
    @Insert("insert into account(type, username, password, personId, active, extra, createAt, updateAt) values(#{type}, #{username}, #{password}, #{person.id}, #{active}, #{extra}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Account e);

    @Delete("delete from account where id=#{id}")
    long delete(Account e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(Account e);

    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(Account e);
    @UpdateProvider(type = Sql.class, method = "updateByPersonAndType")
    long updateByPersonAndType(Account e);

    @Select("select * from account where id=#{id}")
    @Results({@Result(property = "person.id", column = "personId")})
    Account findById(Account e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Account sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({@Result(property = "person.id", column = "personId")})
    List<Account> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Account sample);

    @Select("select * from account where username=#{username}")
    @Results({@Result(property = "person.id", column = "personId")})
    Account findByUsername(Account e);

    @Select("select count(*) from account where username=#{username}")
    long countByUsername(Account e);

    class Sql {
        public String update(final Account e) {
            return new SQL() {{
                UPDATE("account");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getPassword() != null) SET("password = #{password}");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String updateByPerson(final Account e) {
            return new SQL() {{
                UPDATE("account");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getPassword() != null) SET("password = #{password}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                SET("updateAt = #{updateAt}");
                WHERE("personId=#{person.id}");
            }}.toString();
        }

        public String updateByPersonAndType(final Account e) {
            return new SQL() {{
                UPDATE("account");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getPassword() != null) SET("password = #{password}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                SET("updateAt = #{updateAt}");
                WHERE("personId=#{person.id} and type=#{type}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Account sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("account");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                }
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Account sample) {
            return new SQL() {{
                SELECT("*"); FROM("account");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                }
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
