package chengweiou.universe.andromeda.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.entity.AccountRecover;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.andromeda.service.phonemsg.PhoneMsgService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class PhoneMsgTest {
	@Autowired
	private PhoneMsgService service;
	@Autowired
	private CodeSendRecordDio codeSendRecordDio;
	@Autowired
	private Data data;

	@Test
	public void sendTwofa() throws FailException, ProjException {
		String code = RandomStringUtils.randomNumeric(6);
		Twofa twofa = Builder.set("codeTo", "9790000000").set("code", code).to(new Twofa());
		service.sendLogin(twofa);
		CodeSendRecord indb = codeSendRecordDio.findLastByUsername(Builder.set("username", twofa.getCodeTo()).to(new CodeSendRecord()));
		Assertions.assertEquals(code, indb.getCode());
		codeSendRecordDio.delete(indb);
	}

	@Test
	public void sendForgetUrl() throws FailException, ProjException {
		String code = RandomStringUtils.randomAlphanumeric(30);
		AccountRecover accountRecover = Builder.set("phone", "9790000000").set("code", code).to(new AccountRecover());
		service.sendForgetUrl(accountRecover);
		CodeSendRecord indb = codeSendRecordDio.findLastByUsername(Builder.set("username", accountRecover.getPhone()).to(new CodeSendRecord()));
		Assertions.assertEquals(code, indb.getCode());
		codeSendRecordDio.delete(indb);
	}


	@BeforeEach
	public void init() {
		data.init();
	}
}
