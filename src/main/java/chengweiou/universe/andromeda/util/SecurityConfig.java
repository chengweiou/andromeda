package chengweiou.universe.andromeda.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "security")
@Component
@Data
public class SecurityConfig {
    private long needLength = 6;
    private long needUpper = 0;
    private long needLower = 1;
    private long needNum = 0;
    private long needSpec = 0;
    private MixLevel needMixLevel = MixLevel.ALL;

    public enum MixLevel {
        ALL, NOT_SAME, NOT_INCLUDE,
    }
}
