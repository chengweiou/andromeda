package chengweiou.universe.andromeda.dao;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;

@Repository
@Mapper
public interface CodeSendRecordDao {
    @Insert("insert into codeSendRecord(type, username, code, createAt, updateAt) " +
            "values(#{type}, #{username}, #{code}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(CodeSendRecord e);

    @Delete("delete from codeSendRecord where id=#{id}")
    long delete(CodeSendRecord e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(CodeSendRecord e);

    @Select("select * from codeSendRecord where username=#{username} order by updateAt desc limit 1")
    CodeSendRecord findLastByUsername(CodeSendRecord e);

    @Select("select * from codeSendRecord where username=#{e.username} order by updateAt desc limit 1, offset #{skip}")
    CodeSendRecord findOffsetByUsername(@Param("e")CodeSendRecord e, @Param("skip")int skip);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") CodeSendRecord sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<CodeSendRecord> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") CodeSendRecord sample);

    class Sql {

        public String update(final CodeSendRecord e) {
            return new SQL() {{
                UPDATE("codeSendRecord");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getUsername() != null) SET("username = #{username}");
                if (e.getCode() != null) SET("code = #{code}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final CodeSendRecord sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("codeSendRecord");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = #{sample.type}"); 
                }
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final CodeSendRecord sample) {
            return new SQL() {{
                SELECT("*"); FROM("codeSendRecord");
                if (searchCondition.getK() != null) WHERE("username LIKE #{searchCondition.like.k}");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = #{sample.type}"); 
                }
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
