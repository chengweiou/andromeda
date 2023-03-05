package chengweiou.universe.andromeda.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LoginFailCacheTest {

    @Autowired
    private LoginFailCache loginFailCache;

    @Test
    public void ok() {
        String ip1 = "111";
        String username1 = "222";
        String ip2 = "222";
        String username2 = "222";
        loginFailCache.oneMoreFail(ip1, username1);
        Assertions.assertEquals(true, loginFailCache.ok(ip1, username1));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username2));
        loginFailCache.oneMoreFail(ip1, username1);
        Assertions.assertEquals(true, loginFailCache.ok(ip1, username1));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username2));
        loginFailCache.oneMoreFail(ip1, username1);
        Assertions.assertEquals(true, loginFailCache.ok(ip1, username1));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username2));
        loginFailCache.oneMoreFail(ip1, username1);
        Assertions.assertEquals(true, loginFailCache.ok(ip1, username1));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username2));
        loginFailCache.oneMoreFail(ip1, username1);
        Assertions.assertEquals(false, loginFailCache.ok(ip1, username1));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username2));
        loginFailCache.oneMoreFail(ip1, username1);
        Assertions.assertEquals(false, loginFailCache.ok(ip1, username1));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username2));
        Assertions.assertEquals(false, loginFailCache.ok(ip1, username2));
        Assertions.assertEquals(true, loginFailCache.ok(ip2, username1));
    }

}
