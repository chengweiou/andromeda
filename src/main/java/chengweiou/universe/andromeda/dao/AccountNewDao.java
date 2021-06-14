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

@Repository
@Mapper
public interface AccountNewDao {
    @Insert("insert into accountNew(username, phone, email, wechat, weibo, google, facebook, password, personId, active, extra, createAt, updateAt) " + 
        "values(#{username}, #{phone}, #{email}, #{wechat}, #{weibo}, #{google}, #{facebook}, #{password}, #{person.id}, #{active}, #{extra}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(AccountNew e);

    @Delete("delete from accountNew where id=#{id}")
    long delete(AccountNew e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(AccountNew e);

    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(AccountNew e);

    @Select("select * from accountNew where id=#{id}")
    @Results({@Result(property = "person.id", column = "personId")})
    AccountNew findById(AccountNew e);

    @Select("select count(*) from accountNew where username=#{username}")
    long countByUsername(AccountNew e);
    @Select("select * from accountNew where username=#{username}")
    @Results({@Result(property = "person.id", column = "personId")})
    AccountNew findByUsername(AccountNew e);

    @Select("select count(*) from accountNew where phone=#{phone}")
    long countByPhone(AccountNew e);
    @Select("select * from accountNew where phone=#{phone}")
    @Results({@Result(property = "person.id", column = "personId")})
    AccountNew findByPhone(AccountNew e);

    @Select("select count(*) from accountNew where email=#{email}")
    long countByEmail(AccountNew e);
    @Select("select * from accountNew where email=#{email}")
    @Results({@Result(property = "person.id", column = "personId")})
    AccountNew findByEmail(AccountNew e);

    @Select("select count(*) from accountNew where username=#{username} or phone=#{username} or email=#{username} or phone=#{phone} or email=#{email}")
    long countByLoginUsername(AccountNew e);
    @Select("select * from accountNew where username=#{username} or phone=#{username} or email=#{username} or phone=#{phone} or email=#{email}")
    @Results({@Result(property = "person.id", column = "personId")})
    AccountNew findByLoginUsername(AccountNew e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") AccountNew sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({@Result(property = "person.id", column = "personId")})
    List<AccountNew> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") AccountNew sample);

    @SelectProvider(type = Sql.class, method = "countByUsernameOfOther")
    long countByUsernameOfOther(AccountNew e);
    @SelectProvider(type = Sql.class, method = "countByPhoneOfOther")
    long countByPhoneOfOther(AccountNew e);
    @SelectProvider(type = Sql.class, method = "countByEmailOfOther")
    long countByEmailOfOther(AccountNew e);

    class Sql {
        public String update(final AccountNew e) {
            return new SQL() {{
                UPDATE("accountNew");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getPhone() != null) SET("phone = #{phone}");
                if (e.getEmail() != null) SET("email = #{email}");
                if (e.getWechat() != null) SET("wechat = #{wechat}");
                if (e.getWeibo() != null) SET("weibo = #{weibo}");
                if (e.getGoogle() != null) SET("google = #{google}");
                if (e.getFacebook() != null) SET("facebook = #{facebook}");
                if (e.getPassword() != null) SET("password = #{password}");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String updateByPerson(final AccountNew e) {
            return new SQL() {{
                UPDATE("accountNew");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getPhone() != null) SET("phone = #{phone}");
                if (e.getEmail() != null) SET("email = #{email}");
                if (e.getWechat() != null) SET("wechat = #{wechat}");
                if (e.getWeibo() != null) SET("weibo = #{weibo}");
                if (e.getGoogle() != null) SET("google = #{google}");
                if (e.getFacebook() != null) SET("facebook = #{facebook}");
                if (e.getPassword() != null) SET("password = #{password}");
                if (e.getActive() != null) SET("active = #{active}");
                if (e.getExtra() != null) SET("extra = #{extra}");
                SET("updateAt = #{updateAt}");
                WHERE("personId=#{person.id}");
            }}.toString();
        }

        public String countByUsernameOfOther(final AccountNew e) {
            return otherFind(e).SELECT("count(*)").WHERE("username=#{username}").toString();
        }
        public String countByEmailOfOther(final AccountNew e) {
            return otherFind(e).SELECT("count(*)").WHERE("email=#{email}").toString();
        }
        public String countByPhoneOfOther(final AccountNew e) {
            return otherFind(e).SELECT("count(*)").WHERE("phone=#{phone}").toString();
        }
        private SQL otherFind(AccountNew e) {
            return new SQL() {{
                FROM("accountNew");
                if (e.getId() != null) WHERE("id!=#{id}");
                if (e.getPerson() != null) WHERE("personId!=#{person.id}");
            }};
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final AccountNew sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final AccountNew sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, AccountNew sample) {
            return new SQL() {{
                FROM("accountNew");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}")
                        .OR().WHERE("phone LIKE #{searchCondition.like.k}")
                        .OR().WHERE("email LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                }
            }};
        }
    }
}
