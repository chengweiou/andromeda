package chengweiou.universe.andromeda.util;

import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;

@Component
public class UserAgentUtil {
    public String getPlatform(String userAgentString) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return userAgent.getOperatingSystem().getName() + ": " + userAgent.getBrowser().getName();
    }
}
