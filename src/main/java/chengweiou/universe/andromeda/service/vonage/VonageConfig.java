package chengweiou.universe.andromeda.service.vonage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "vonage")
@Component
@Data
public class VonageConfig {
    private String apiKey;
    private String apiSecret;
}
