package chengweiou.universe.andromeda.service.vonage;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;

@Profile("!test")
@Service
public class VonageManager {
    @Autowired
    private VonageConfig config;

    public void sendSms(String to, String msg) throws FailException {
        VonageClient client = VonageClient.builder().apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).build();
		TextMessage message = new TextMessage("18773370831", to, msg);

		SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
        if (response.getMessages().get(0).getStatus() != MessageStatus.OK) throw new FailException("vonage sms: " + response.getMessages().get(0).getErrorText());

    }

}
