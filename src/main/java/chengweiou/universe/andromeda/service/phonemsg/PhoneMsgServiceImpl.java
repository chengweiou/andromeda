package chengweiou.universe.andromeda.service.phonemsg;


import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;


@Service
public class PhoneMsgServiceImpl implements PhoneMsgService {
    @Autowired
    private CodeSendRecordDio dio;
    
    public void sendCode(Twofa twofa) throws ProjException {
        CodeSendRecord lastCodeSendRecord = dio.findLastByUsername(Builder.set("username", twofa.getCodeTo()).to(new CodeSendRecord()));
        if (lastCodeSendRecord.getId() != null) {
            if (lastCodeSendRecord.getCreateAt().plusMinutes(1).isAfter(LocalDateTime.now(ZoneId.of("UTC")))) throw new ProjException(ProjectRestCode.PHONE_MSG_TOO_MANY);
        }
        
        // todo  send code
    }

}
