package chengweiou.universe.andromeda.util;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SecurityUtil {
    @Autowired
    private SecurityConfig config;

    public boolean canUsePassword(String pw) {
        if (pw.length() < config.getNeedLength()) return false;
        long countUpper = Pattern.compile("(?=.*[A-Z])").matcher(pw).results().count();
        if (countUpper < config.getNeedUpper()) return false;
        long countLower = Pattern.compile("(?=.*[a-z])").matcher(pw).results().count();
        if (countLower < config.getNeedLower()) return false;
        long countNum = Pattern.compile("(?=.*\\d)").matcher(pw).results().count();
        if (countNum < config.getNeedNum()) return false;
        long countSpec = Pattern.compile("([^\\w\\s])").matcher(pw).results().count();
        if (countSpec < config.getNeedSpec()) return false;
        return true;
    }

    public boolean canUseMixLevel(String username, String password) {
        return switch(config.getNeedMixLevel()) {
            case ALL -> true;
            case NOT_SAME -> !username.equals(password);
            case NOT_INCLUDE -> !username.contains(password) && !password.contains(username);
        };
    }

    public boolean canUseUsername(String username) {
        boolean lengthOk = username.length() >= config.getNeedLength();
        if (!lengthOk) return false;
        boolean startWith = Pattern.compile("^[0-9A-Za-z]").matcher(username).find();
        if (!startWith) return false;
        boolean onlyNormal = Pattern.compile("^[\\d\\w\\.@-]+$").matcher(username).find();
        if (!onlyNormal) return false;
        return true;
    }


    public String hash(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt(12));
    }

    public boolean check(String pw, String hash) {
        try {
            return BCrypt.checkpw(pw, hash);
        } catch (Exception ex) {
            log.error("check pw fail: " + hash, ex);
            return false;
        }
    }
}
