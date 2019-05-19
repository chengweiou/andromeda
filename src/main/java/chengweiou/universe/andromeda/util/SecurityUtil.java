package chengweiou.universe.andromeda.util;

import chengweiou.universe.blackhole.util.LogUtil;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class SecurityUtil {

    public static String hash(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt(12));
    }

    public static boolean check(String pw, String hash) {
        try {
            return BCrypt.checkpw(pw, hash);
        } catch (Exception ex) {
            LogUtil.e("check pw fail: " + hash, ex);
            return false;
        }
    }
}
