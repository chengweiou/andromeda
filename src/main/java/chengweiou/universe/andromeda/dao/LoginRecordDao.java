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
import chengweiou.universe.andromeda.model.entity.AccountNew;
import chengweiou.universe.andromeda.model.entity.LoginRecord;

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
    LoginRecord findLastByAccount(AccountNew account);

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
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final LoginRecord sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, LoginRecord sample) {
            return new SQL() {{
                FROM("loginRecord");
                if (searchCondition.getK() != null) WHERE("ip LIKE #{searchCondition.like.k}")
                        .OR().WHERE("platform LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getAccount() != null) {
                        if (sample.getAccount().getId() != null) WHERE("accountId=#{sample.account.id}");
                        if (sample.getAccount().getPerson() != null) WHERE("personId=#{sample.account.person.id}");
                    }
                }
            }};
        }
    }
}
