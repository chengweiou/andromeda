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
import chengweiou.universe.andromeda.model.entity.Twofa;

@Repository
@Mapper
public interface TwofaDao {
    @Insert("insert into twofa(personId, type, codeTo, loginAccountId, token, code, codeExp, createAt, updateAt) " + 
            "values(#{person.id}, #{type}, #{codeTo}, #{loginAccount.id}, #{token}, #{code}, #{codeExp}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Twofa e);

    @Delete("delete from twofa where id=#{id}")
    long delete(Twofa e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(Twofa e);

    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(Twofa e);

    @Select("select * from twofa where id=#{id}")
    @Results({
        @Result(property = "person.id", column = "personId"),
        @Result(property = "loginAccount.id", column = "loginAccountId"),
    })
    Twofa findById(Twofa e);

    @Select("select * from twofa where personId=#{person.id}")
    @Results({
        @Result(property = "person.id", column = "personId"),
        @Result(property = "loginAccount.id", column = "loginAccountId"),
    })
    Twofa findByPerson(Twofa e);

    @Select("select * from twofa where token=#{token} and code=#{code}")
    @Results({
        @Result(property = "person.id", column = "personId"),
        @Result(property = "loginAccount.id", column = "loginAccountId"),
    })
    Twofa findByTokenAndCode(Twofa e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Twofa sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({
        @Result(property = "person.id", column = "personId"),
        @Result(property = "loginAccount.id", column = "loginAccountId"),
    })
    List<Twofa> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Twofa sample);

    @Select("select count(*) from twofa where personId=#{person.id}")
    long countByPerson(Twofa e);

    class Sql {
        public String update(final Twofa e) {
            return new SQL() {{
                UPDATE("twofa");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getCodeTo() != null) SET("codeTo = #{codeTo}");
                if (e.getLoginAccount() != null) SET("loginAccountId = #{loginAccount.id}");
                if (e.getToken() != null) SET("token = #{token}");
                if (e.getCode() != null) SET("code = #{code}");
                if (e.getCodeExp() != null) SET("codeExp = #{codeExp}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String updateByPerson(final Twofa e) {
            return new SQL() {{
                UPDATE("twofa");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getCodeTo() != null) SET("codeTo = #{codeTo}");
                if (e.getLoginAccount() != null) SET("loginAccountId = #{loginAccount.id}");
                if (e.getToken() != null) SET("token = #{token}");
                if (e.getCode() != null) SET("code = #{code}");
                if (e.getCodeExp() != null) SET("codeExp = #{codeExp}");
                SET("updateAt = #{updateAt}");
                WHERE("personId=#{person.id}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Twofa sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Twofa sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, Twofa sample) {
            return new SQL() {{
                FROM("twofa");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = #{sample.type}");
                }
            }};
        }
    }
}
