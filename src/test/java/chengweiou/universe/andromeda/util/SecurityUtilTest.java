package chengweiou.universe.andromeda.util;

import chengweiou.universe.blackhole.util.LogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityUtilTest {
    @Test
    public void ok() {

        String hashpw = SecurityUtil.hash("123");
        boolean success = SecurityUtil.check("123", hashpw);
        Assertions.assertEquals(true, success);
        LogUtil.d("--------------------take to test data---------------");
        LogUtil.d(hashpw);
        LogUtil.d("--------------------take to test data---------------");
        // diff salt
        hashpw = SecurityUtil.hash("aaa");
        success = SecurityUtil.check("aaa", hashpw);
        Assertions.assertEquals(true, success);
    }

    @Test
    public void fail() {
        // pw
        String hashpw = SecurityUtil.hash("aaaa");
        boolean success = SecurityUtil.check("aaa", hashpw);
        Assertions.assertEquals(false, success);
    }
}
