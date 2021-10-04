package chengweiou.universe.andromeda.service.phonemsg;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecordType;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.andromeda.service.vonage.VonageManager;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;


@Service
public class PhoneMsgService {
    @Autowired
    private CodeSendRecordDio dio;
    @Autowired
    private VonageManager manager;

        /**
     * need phone and code
     * @param twofa
     * @throws ProjException
     * @throws FailException
     */
    public void sendLogin(Twofa twofa) throws ProjException, FailException {
        String msg = "Andromeda code: " + twofa.getCode() + ". Valid for 1 min";
        manager.sendSms(twofa.getCodeTo(), msg);
        CodeSendRecord e = Builder.set("type", CodeSendRecordType.TWOFA).set("username", twofa.getCodeTo()).set("code", twofa.getCode()).to(new CodeSendRecord());
        dio.save(e);
    }

    /**
     * need phone and code
     * @param codeSendRecord
     * @throws ProjException
     * @throws FailException
     */
    public void sendForgetUrl(AccountRecover accountRecover) throws FailException {
        // tip: need config to setup server address
        String msg = "Please use link: " + "http://127.0.0.1:60000/andromeda/forgetPassword/3?id=" + accountRecover.getId() + "&code=" + accountRecover.getCode() + " to reset your Andromeda password";
        manager.sendSms(accountRecover.getPhone(), msg);
        CodeSendRecord e = Builder.set("type", CodeSendRecordType.FORGET_PASSWORD).set("username", accountRecover.getPhone()).set("code", accountRecover.getCode()).to(new CodeSendRecord());
        dio.save(e);
    }

}
