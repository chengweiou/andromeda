package chengweiou.universe.andromeda.init.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String issuer;
    private Long expMinute;
    private String sign;

    @Override
    public String toString() {
        return "JwtConfig{" +
                "issuer='" + issuer + '\'' +
                ", expMinute=" + expMinute +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Long getExpMinute() {
        return expMinute;
    }

    public void setExpMinute(Long expMinute) {
        this.expMinute = expMinute;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
