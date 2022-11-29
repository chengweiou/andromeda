package chengweiou.universe.andromeda.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class SecurityUtilTest {
    @Test
    public void ok() {

        String hashpw = SecurityUtil.hash("123");
        boolean success = SecurityUtil.check("123", hashpw);
        Assertions.assertEquals(true, success);
        log.debug("--------------------take to test data---------------");
        log.debug(hashpw);
        log.debug("--------------------take to test data---------------");
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
