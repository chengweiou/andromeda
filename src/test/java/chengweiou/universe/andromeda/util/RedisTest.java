package chengweiou.universe.andromeda.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.base.redis.RedisUtil;
import redis.embedded.RedisServer;

@SpringBootTest({"spring.data.redis.port=6388", "onMock.redis=false"})
@ActiveProfiles("test")
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;
    @Value("${onMock.redis}")
    private boolean onMock;

    private static Stream<Arguments> setArg() {
        return Stream.of(
            Arguments.of("1", "aaa", 2, "aaa", false),
            Arguments.of("2", "bbb", 2, "bbb", false),
            Arguments.of("3", "ccc", 1, null, true),
            Arguments.of("4", "ddd", 2, "ddd", false)
        );
    }
    @ParameterizedTest
    @MethodSource("setArg")
    public void set(final String k, final String v, final long ex, final String exp, final boolean wait) throws InterruptedException {
        redisUtil.set(k, v, ex);
        if (wait) Thread.sleep(ex * 1000 + 10);
        String store = redisUtil.get(k);
        Assertions.assertEquals(exp, store);
    }

    @Test
    public void testOverwrite() throws InterruptedException {
        redisUtil.set("1", "aaa", 10*60);
        String store = redisUtil.get("1");
        Assertions.assertEquals("aaa", store);
        redisUtil.set("1", "aab", 10*60);
        store = redisUtil.get("1");
        Assertions.assertEquals("aab", store);
    }

    public static RedisServer redisServer;
    @BeforeAll
    public static void startRedis() throws Exception {
        redisServer = RedisServer.builder().port(6388).setting("maxmemory 128M").build();
        redisServer.start();
    }

    @AfterAll
    public static void stopRedis() throws InterruptedException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
