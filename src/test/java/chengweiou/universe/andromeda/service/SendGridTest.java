package chengweiou.universe.andromeda.service;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

@SpringBootTest
@ActiveProfiles("test")
public class SendGridTest {

	@Test
	public void sendSms() throws FailException, ProjException {
	}

	@Test
	public void sendTwofa() throws FailException, ProjException {
	}

}
