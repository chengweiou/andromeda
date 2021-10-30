package chengweiou.universe.andromeda.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.andromeda.base.dao.BaseDao;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa.Dto;

@Repository
@Mapper
public interface TwofaDao extends BaseDao<Dto> {

    @UpdateProvider(type = Sql.class, method = "updateByPerson")
    long updateByPerson(Dto e);

    @Select("select * from twofa where personId=#{personId}")
    Dto findByPerson(Dto e);

    @Select("select * from twofa where token=#{token} and code=#{code}")
    Dto findByTokenAndCode(Dto e);

    @Select("select count(*) from twofa where personId=#{personId}")
    long countByPerson(Dto e);

    class Sql {

        public String updateByPerson(final Dto e) {
            return new SQL() {{
                UPDATE("twofa");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getCodeTo() != null) SET("codeTo = #{codeTo}");
                if (e.getToken() != null) SET("token = #{token}");
                if (e.getCode() != null) SET("code = #{code}");
                if (e.getCodeExp() != null) SET("codeExp = #{codeExp}");
                SET("updateAt = #{updateAt}");
                WHERE("personId=#{personId}");
            }}.toString();
        }

    }
}
