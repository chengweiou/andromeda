package com.universe.andromeda.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class SecurityUtil {

    public static String hash(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt(12));
    }

    public static boolean check(String pw, String hash) {
        return BCrypt.checkpw(pw, hash);
    }
}
