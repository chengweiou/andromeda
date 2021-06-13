package chengweiou.universe.andromeda.service.phonemsg;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.entity.AccountRecover;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecordType;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;


@Service
public class PhoneMsgServiceImpl implements PhoneMsgService {
    @Autowired
    private CodeSendRecordDio dio;
    
    @Override
    public void sendLogin(Twofa twofa) throws ProjException, FailException {
        // todo  send code
        CodeSendRecord e = Builder.set("type", CodeSendRecordType.TWOFA).set("username", twofa.getCodeTo()).set("code", twofa.getCode()).to(new CodeSendRecord());
        dio.save(e);
    }

    @Override
    public void sendForgetUrl(AccountRecover accountRecover) throws FailException {
        CodeSendRecord e = Builder.set("type", CodeSendRecordType.FORGET_PASSWORD).set("username", accountRecover.getPhone()).set("code", accountRecover.getCode()).to(new CodeSendRecord());
        // todo send code
        dio.save(e);
    }

}
