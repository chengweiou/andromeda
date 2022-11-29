package chengweiou.universe.andromeda.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtil {

    public static String hash(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt(12));
    }

    public static boolean check(String pw, String hash) {
        try {
            return BCrypt.checkpw(pw, hash);
        } catch (Exception ex) {
            log.error("check pw fail: " + hash, ex);
            return false;
        }
    }
}
