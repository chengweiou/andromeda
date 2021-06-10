package chengweiou.universe.andromeda.service.phonemsg;


import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.blackhole.exception.ProjException;

public interface PhoneMsgService {
    /**
     * send code should before update. using updateat as last send code time
     * @param twofa
     * @throws ProjException
     */
    void sendCode(Twofa twofa) throws ProjException;
}
