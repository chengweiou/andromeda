package chengweiou.universe.andromeda.dao;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LoginRecordDao {
    @Insert("insert into loginRecord(accountId, personId, ip, platform, updateAt) values(#{account.id}, #{account.person.id}, #{ip}, #{platform}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(LoginRecord e);

    @Delete("delete from loginRecord where id=#{id}")
    int delete(LoginRecord e);

    @SelectProvider(type = Sql.class, method = "count")
    int count(@Param("searchCondition") SearchCondition searchCondition);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({
            @Result(property = "account.id", column = "accountId"),
            @Result(property = "account.person.id", column = "personId"),
    })
    List<LoginRecord> find(@Param("searchCondition") SearchCondition searchCondition);

    @SelectProvider(type = Sql.class, method = "countByPerson")
    int countByPerson(@Param("searchCondition") SearchCondition searchCondition, @Param("person")Person person);

    @SelectProvider(type = Sql.class, method = "findByPerson")
    @Results({
            @Result(property = "account.id", column = "accountId"),
            @Result(property = "account.person.id", column = "personId"),
    })
    List<LoginRecord> findByPerson(@Param("searchCondition") SearchCondition searchCondition, @Param("person")Person person);
    class Sql {

        public String count(@Param("searchCondition")final SearchCondition searchCondition) {
            return new SQL() {{
                SELECT("count(*)"); FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition) {
            return new SQL() {{
                SELECT("*"); FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        public String countByPerson(@Param("searchCondition")final SearchCondition searchCondition, @Param("person")final Person person) {
            return new SQL() {{
                SELECT("count(*)"); FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
                AND().WHERE("personId=#{person.id}");
            }}.toString();
        }

        public String findByPerson(@Param("searchCondition")final SearchCondition searchCondition, @Param("person")final Person person) {
            return new SQL() {{
                SELECT("*"); FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
                AND().WHERE("personId=#{person.id}");
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
