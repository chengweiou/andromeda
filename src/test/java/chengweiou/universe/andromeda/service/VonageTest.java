package chengweiou.universe.andromeda.service;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import com.vonage.client.verify.VerifyResponse;
import com.vonage.client.verify.VerifyStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.service.vonage.VonageConfig;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;

@SpringBootTest
@ActiveProfiles("test")
public class VonageTest {
	@Autowired
	private VonageConfig config;

	@Test
	public void sendSms() throws FailException, ProjException {
		VonageClient client = VonageClient.builder().apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).build();
		TextMessage message = new TextMessage("18773370831", "", "vonage sms from service test");

		SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

		if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
				System.out.println("Message sent successfully.");
		} else {
				System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
		}
	}

	@Test
	public void sendTwofa() throws FailException, ProjException {
		VonageClient client = VonageClient.builder().apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).build();
		VerifyResponse response = client.getVerifyClient().verify("", "Vonage");

		if (response.getStatus() == VerifyStatus.OK) {
				System.out.printf("RequestID: %s", response.getRequestId());
		} else {
				System.out.printf("ERROR! %s: %s", response.getStatus(), response.getErrorText());
		}
	}

}
