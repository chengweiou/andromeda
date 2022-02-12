package chengweiou.universe.andromeda.service.twofa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.TwofaDao;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa.Dto;
import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;

@Component
public class TwofaDio extends BaseDio<Twofa, Twofa.Dto> {
    @Autowired
    private TwofaDao dao;
    @Override
    protected TwofaDao getDao() { return dao; }
    @Override
    protected Class getTClass() { return Twofa.class; };
    @Override
    protected String getDefaultSort() { return "createAt"; };
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (sample != null) {
                if (sample.getType() != null) WHERE("type = #{sample.type}");
            }
        }}.toString();
    }

    public Twofa findByTokenAndCode(Twofa e) {
        Twofa.Dto result = dao.findByTokenAndCode(e.toDto());
        if (result == null) return Twofa.NULL;
        return result.toBean();
    }
}
