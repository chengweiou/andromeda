package chengweiou.universe.andromeda.service.twofa;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.model.entity.twofa.TwofaType;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;


@Service
public class TwofaService {
    @Autowired
    private TwofaDio dio;

    public void save(Twofa e) throws FailException, ProjException {
        e.cleanCode();
        dio.save(e);
    }

    /**
     * 如果用户没有设置twofa， 返回 Twofa.NULL,
     * 如果用户 有 设置twofa， 返回 twofa，并携带code， token。下一步应等待checkTwofa的code是否匹配
     * @param e 通过 key 查找, 需要 twofa.person
     * @return
     * @throws ProjException
     * @throws FailException
     */
    public Twofa findAndWaitForLogin(Twofa e) throws ProjException, FailException {
        Twofa indb = dio.findByKey(e);
        if (indb.getType() == null || indb.getType() == TwofaType.NONE) return Twofa.NULL;
        if (Instant.now().isBefore(indb.getCodeExp())) throw new ProjException(ProjectRestCode.PHONE_MSG_TOO_MANY);
        String code = RandomStringUtils.randomNumeric(6);
        String token = RandomStringUtils.randomAlphabetic(30);
        dio.update(Builder
            .set("code", code)
            .set("token", token)
            .set("codeExp", Instant.now().plus(1, ChronoUnit.MINUTES))
            .to(indb)
            );
        return indb;
    }

    /**
     * check code needs token+code
     * @param twofa
     * @return
     * @throws ProjException
     * @throws FailException
     */
    public Twofa findAfterCheckCode(Twofa e) throws ProjException, FailException {
        Twofa indb = dio.findByTokenAndCode(e);
        if (indb.getId() == null) throw new ProjException(ProjectRestCode.TWOFA_CODE_NOT_MATCH);
        if (Instant.now().isAfter(indb.getCodeExp())) throw new ProjException(ProjectRestCode.TWOFA_EXPIRED);
        indb.cleanCode();
        dio.update(indb);
        return indb;
    }
}
