package chengweiou.universe.andromeda.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.TwofaType;
import chengweiou.universe.andromeda.service.account.TwofaDio;
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
	private TwofaDio twofaDio;
	@Autowired
	private Data data;

	@Test
	public void sendTwofa() throws FailException, ProjException {
		Twofa twofa = twofaDio.findById(data.twofaList.get(0));
		Builder.set("type", TwofaType.PHONE_MSG).set("codeTo", "9790000000").set("loginAccount", data.accountList.get(0)).to(twofa);
		service.sendCode(twofa);
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
