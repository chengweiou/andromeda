package com.universe.andromeda.util;

import com.universe.andromeda.init.redis.JedisUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RedisTest {
    @Autowired
    private JedisUtil jedisUtil;
    @Test
    public void set() throws InterruptedException {
        jedisUtil.set("1", "aaa", 20 * 60);
        String store = jedisUtil.get("1");
        Assertions.assertEquals("aaa", store);

        // set another
        jedisUtil.set("1", "bbb", 10 * 60);
        store = jedisUtil.get("1");
        Assertions.assertEquals("bbb", store);

        // expired
        jedisUtil.set("1", "aaa", 1);
        Thread.sleep(1500);
        store = jedisUtil.get("1");
        Assertions.assertEquals(null, store);

        jedisUtil.set("1", "aaa", 10 * 60);
        store = jedisUtil.get("1");
        Assertions.assertEquals("aaa", store);

    }
}
