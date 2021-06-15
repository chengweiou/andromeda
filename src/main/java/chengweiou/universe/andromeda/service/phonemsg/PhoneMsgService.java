package chengweiou.universe.andromeda.service.phonemsg;


import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

public interface PhoneMsgService {
    /**
     * need phone and code
     * @param twofa
     * @throws ProjException
     * @throws FailException
     */
    void sendLogin(Twofa twofa) throws ProjException, FailException;

    /**
     * need phone and code
     * @param codeSendRecord
     * @throws ProjException
     * @throws FailException
     */
    void sendForgetUrl(AccountRecover accountRecover) throws FailException;
}
