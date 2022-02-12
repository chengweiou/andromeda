package chengweiou.universe.andromeda.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.entity.Account.Dto;

@Repository
@Mapper
public interface AccountDao extends BaseDao<Dto> {

    @Select("select count(*) from account where username=#{username}")
    long countByUsername(Dto e);
    @Select("select * from account where username=#{username}")
    Dto findByUsername(Dto e);

    @Select("select count(*) from account where phone=#{phone}")
    long countByPhone(Dto e);
    @Select("select * from account where phone=#{phone}")
    Dto findByPhone(Dto e);

    @Select("select count(*) from account where email=#{email}")
    long countByEmail(Dto e);
    @Select("select * from account where email=#{email}")
    Dto findByEmail(Dto e);

    @Select("select count(*) from account where username=#{username} or phone=#{username} or email=#{username} or phone=#{phone} or email=#{email}")
    long countByLoginUsername(Dto e);
    @Select("select * from account where username=#{username} or phone=#{username} or email=#{username} or phone=#{phone} or email=#{email}")
    Dto findByLoginUsername(Dto e);

    @SelectProvider(type = Sql.class, method = "countByUsernameOfOther")
    long countByUsernameOfOther(Dto e);
    @SelectProvider(type = Sql.class, method = "countByPhoneOfOther")
    long countByPhoneOfOther(Dto e);
    @SelectProvider(type = Sql.class, method = "countByEmailOfOther")
    long countByEmailOfOther(Dto e);

    class Sql {

        public String countByUsernameOfOther(final Dto e) {
            return otherFind(e).SELECT("count(*)").WHERE("username=#{username}").toString();
        }
        public String countByEmailOfOther(final Dto e) {
            return otherFind(e).SELECT("count(*)").WHERE("email=#{email}").toString();
        }
        public String countByPhoneOfOther(final Dto e) {
            return otherFind(e).SELECT("count(*)").WHERE("phone=#{phone}").toString();
        }
        private SQL otherFind(Dto e) {
            return new SQL() {{
                FROM("account");
                if (e.getId() != null) WHERE("id!=#{id}");
                if (e.getPersonId() != null) WHERE("personId!=#{personId}");
            }};
        }

    }

    // @Insert("insert into account(username, phone, email, wechat, weibo, google, facebook, password, personId, active, extra, createAt, updateAt) " +
    //     "values(#{username}, #{phone}, #{email}, #{wechat}, #{weibo}, #{google}, #{facebook}, #{password}, #{personId}, #{active}, #{extra}, #{createAt}, #{updateAt})")
    // @Options(useGeneratedKeys = true, keyProperty = "id")
    // long save(Account e);

    // @Delete("delete from account where id=#{id}")
    // long delete(Account e);

    // @UpdateProvider(type = Sql.class, method = "update")
    // long update(Account e);

    // @UpdateProvider(type = Sql.class, method = "updateByPerson")
    // long updateByPerson(Account e);

    // @Select("select * from account where id=#{id}")
    // @Results({@Result(property = "personId", column = "personId")})
    // Account findById(Account e);
    // @Select("select * from account where personId=#{personId}")
    // @Results({@Result(property = "personId", column = "personId")})
    // Account findByPerson(Account e);

    // @Select("select count(*) from account where username=#{username}")
    // long countByUsername(Account e);
    // @Select("select * from account where username=#{username}")
    // @Results({@Result(property = "personId", column = "personId")})
    // Account findByUsername(Account e);

    // @Select("select count(*) from account where phone=#{phone}")
    // long countByPhone(Account e);
    // @Select("select * from account where phone=#{phone}")
    // @Results({@Result(property = "personId", column = "personId")})
    // Account findByPhone(Account e);

    // @Select("select count(*) from account where email=#{email}")
    // long countByEmail(Account e);
    // @Select("select * from account where email=#{email}")
    // @Results({@Result(property = "personId", column = "personId")})
    // Account findByEmail(Account e);

    // @Select("select count(*) from account where username=#{username} or phone=#{username} or email=#{username} or phone=#{phone} or email=#{email}")
    // long countByLoginUsername(Account e);
    // @Select("select * from account where username=#{username} or phone=#{username} or email=#{username} or phone=#{phone} or email=#{email}")
    // @Results({@Result(property = "personId", column = "personId")})
    // Account findByLoginUsername(Account e);

    // @SelectProvider(type = Sql.class, method = "count")
    // long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Account sample);

    // @SelectProvider(type = Sql.class, method = "find")
    // @Results({@Result(property = "personId", column = "personId")})
    // List<Account> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Account sample);

    // @SelectProvider(type = Sql.class, method = "countByUsernameOfOther")
    // long countByUsernameOfOther(Account e);
    // @SelectProvider(type = Sql.class, method = "countByPhoneOfOther")
    // long countByPhoneOfOther(Account e);
    // @SelectProvider(type = Sql.class, method = "countByEmailOfOther")
    // long countByEmailOfOther(Account e);

    // class Sql {
    //     public String update(final Account e) {
    //         return new SQL() {{
    //             UPDATE("account");
    //             if (e.getUsername() != null) SET("username = #{username}");
    //             if (e.getPhone() != null) SET("phone = #{phone}");
    //             if (e.getEmail() != null) SET("email = #{email}");
    //             if (e.getWechat() != null) SET("wechat = #{wechat}");
    //             if (e.getWeibo() != null) SET("weibo = #{weibo}");
    //             if (e.getGoogle() != null) SET("google = #{google}");
    //             if (e.getFacebook() != null) SET("facebook = #{facebook}");
    //             if (e.getPassword() != null) SET("password = #{password}");
    //             if (e.getPerson() != null) SET("personId = #{personId}");
    //             if (e.getActive() != null) SET("active = #{active}");
    //             if (e.getExtra() != null) SET("extra = #{extra}");
    //             SET("updateAt = #{updateAt}");
    //             WHERE("id=#{id}");
    //         }}.toString();
    //     }

    //     public String updateByPerson(final Account e) {
    //         return new SQL() {{
    //             UPDATE("account");
    //             if (e.getUsername() != null) SET("username = #{username}");
    //             if (e.getPhone() != null) SET("phone = #{phone}");
    //             if (e.getEmail() != null) SET("email = #{email}");
    //             if (e.getWechat() != null) SET("wechat = #{wechat}");
    //             if (e.getWeibo() != null) SET("weibo = #{weibo}");
    //             if (e.getGoogle() != null) SET("google = #{google}");
    //             if (e.getFacebook() != null) SET("facebook = #{facebook}");
    //             if (e.getPassword() != null) SET("password = #{password}");
    //             if (e.getActive() != null) SET("active = #{active}");
    //             if (e.getExtra() != null) SET("extra = #{extra}");
    //             SET("updateAt = #{updateAt}");
    //             WHERE("personId=#{personId}");
    //         }}.toString();
    //     }

    //     public String countByUsernameOfOther(final Account e) {
    //         return otherFind(e).SELECT("count(*)").WHERE("username=#{username}").toString();
    //     }
    //     public String countByEmailOfOther(final Account e) {
    //         return otherFind(e).SELECT("count(*)").WHERE("email=#{email}").toString();
    //     }
    //     public String countByPhoneOfOther(final Account e) {
    //         return otherFind(e).SELECT("count(*)").WHERE("phone=#{phone}").toString();
    //     }
    //     private SQL otherFind(Account e) {
    //         return new SQL() {{
    //             FROM("account");
    //             if (e.getId() != null) WHERE("id!=#{id}");
    //             if (e.getPerson() != null) WHERE("personId!=#{personId}");
    //         }};
    //     }

    //     public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Account sample) {
    //         return baseFind(searchCondition, sample).SELECT("count(*)").toString();
    //     }

    //     public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Account sample) {
    //         return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
    //     }

    //     private SQL baseFind(SearchCondition searchCondition, Account sample) {
    //         return new SQL() {{
    //             FROM("account");
    //             if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}")
    //                     .OR().WHERE("phone LIKE #{searchCondition.like.k}")
    //                     .OR().WHERE("email LIKE #{searchCondition.like.k}");
    //             if (sample != null) {
    //                 if (sample.getPerson() != null) WHERE("personId = #{sample.personId}");
    //             }
    //         }};
    //     }
    // }
}
