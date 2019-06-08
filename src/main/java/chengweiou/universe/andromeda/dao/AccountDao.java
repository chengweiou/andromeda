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
    @Insert("insert into account(username, password, personId, active, extra, createAt, updateAt) values(#{username}, #{password}, #{active}, #{person.id}, #{extra}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Account e);

    @Delete("delete from account where id=#{id}")
    int delete(Account e);

    @UpdateProvider(type = Sql.class, method = "update")
    int update(Account e);

    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    int updateByPerson(Account e);

    @Select("select * from account where id=#{id}")
    @Results({@Result(property = "person.id", column = "personId")})
    Account findById(Account e);

    @SelectProvider(type = Sql.class, method = "count")
    int count(@Param("searchCondition") SearchCondition searchCondition);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({@Result(property = "person.id", column = "personId")})
    List<Account> find(@Param("searchCondition") SearchCondition searchCondition);

    @Select("select * from account where username=#{username}")
    @Results({@Result(property = "person.id", column = "personId")})
    Account findByUsername(Account e);

    @Select("select count(*) from account where username=#{username}")
    int countByUsername(Account e);

    class Sql {
        public String update(final Account e) {
            return new SQL() {{
                UPDATE("account");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String updateByPerson(final Account e) {
            return new SQL() {{
                UPDATE("account");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                WHERE("personId=#{person.id}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition) {
            return new SQL() {{
                SELECT("count(*)"); FROM("account");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition) {
            return new SQL() {{
                SELECT("*"); FROM("account");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
