package chengweiou.universe.andromeda.dao;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LoginRecordDao {
    @Insert("insert into loginRecord(accountId, personId, ip, platform, loginTime, logoutTime, createAt, updateAt) " +
            "values(#{account.id}, #{account.person.id}, #{ip}, #{platform}, #{loginTime}, #{logoutTime}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(LoginRecord e);

    @Delete("delete from loginRecord where id=#{id}")
    long delete(LoginRecord e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(LoginRecord e);

    @Select("select * from loginRecord where accountId=#{id} order by updateAt desc limit 1")
    @Results({
            @Result(property = "account.id", column = "accountId"),
            @Result(property = "account.person.id", column = "personId"),
    })
    LoginRecord findLastByAccount(Account account);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") LoginRecord sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({
            @Result(property = "account.id", column = "accountId"),
            @Result(property = "account.person.id", column = "personId"),
    })
    List<LoginRecord> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") LoginRecord sample);

    class Sql {

        public String update(final LoginRecord e) {
            return new SQL() {{
                UPDATE("loginRecord");
                if (e.getIp() != null) SET("ip = #{ip}");
                if (e.getPlatform() != null) SET("platform = #{platform}");
                if (e.getLoginTime() != null) SET("loginTime = #{loginTime}");
                if (e.getLogoutTime() != null) SET("logoutTime = #{logoutTime}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final LoginRecord sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getAccount() != null) {
                        if (sample.getAccount().getId() != null) WHERE("accountId=#{sample.account.id}");
                        if (sample.getAccount().getPerson() != null) WHERE("personId=#{sample.account.person.id}");
                    }
                }
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final LoginRecord sample) {
            return new SQL() {{
                SELECT("*"); FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getAccount() != null) {
                        if (sample.getAccount().getId() != null) WHERE("accountId=#{sample.account.id}");
                        if (sample.getAccount().getPerson() != null) WHERE("personId=#{sample.account.person.id}");
                    }
                }
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
