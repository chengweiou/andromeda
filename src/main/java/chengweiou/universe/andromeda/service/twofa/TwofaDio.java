package chengweiou.universe.andromeda.service.twofa;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.dao.TwofaDao;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;


@Component
public class TwofaDio {
    @Autowired
    private TwofaDao dao;

    public void save(Twofa e) throws ProjException, FailException {

        long count = dao.countByKey(e.toDto());
        if (count != 0) throw new ProjException("dup key: " + e.getPerson() + " exists", BasicRestCode.EXISTS);
        e.cleanCode();
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        Twofa.Dto dto = e.toDto();
        count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(Twofa e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(Twofa e) {
        e.updateAt();
        System.out.println(e.toDto());
        return dao.update(e.toDto());
    }

    public long updateByPerson(Twofa e) {
        e.updateAt();
        return dao.updateByPerson(e.toDto());
    }

    public Twofa findById(Twofa e) {
        Twofa.Dto result = dao.findById(e.toDto());
        if (result == null) return Twofa.NULL;
        return result.toBean();
    }

    public Twofa findByPerson(Twofa e) {
        Twofa.Dto result = dao.findByPerson(e.toDto());
        if (result == null) return Twofa.NULL;
        return result.toBean();
    }

    public Twofa findByTokenAndCode(Twofa e) {
        Twofa.Dto result = dao.findByTokenAndCode(e.toDto());
        if (result == null) return Twofa.NULL;
        return result.toBean();
    }

    public long count(SearchCondition searchCondition, Twofa sample) {
        return dao.count(searchCondition, sample!=null ? sample.toDto() : null);
    }
    public List<Twofa> find(SearchCondition searchCondition, Twofa sample) {
        searchCondition.setDefaultSort("createAt");
        List<Twofa.Dto> dtoList = dao.find(searchCondition, sample!=null ? sample.toDto() : null);
        List<Twofa> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

}
