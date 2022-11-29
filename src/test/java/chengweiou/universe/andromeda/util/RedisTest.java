package chengweiou.universe.andromeda.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import chengweiou.universe.andromeda.base.redis.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
@ActiveProfiles("test")
public class RedisTest {
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JedisPool jedisPool;
    private Jedis jedis = Mockito.mock(Jedis.class);
    @Value("${onMock.redis}")
    private boolean onMock;

    private static Stream<Arguments> setArg() {
        return Stream.of(
            Arguments.of("1", "aaa", 20*60, "aaa", false),
            Arguments.of("2", "bbb", 10*60, "bbb", false),
            Arguments.of("3", "ccc", 1, null, true),
            Arguments.of("4", "ddd", 10*60, "ddd", false)
        );
    }
    @ParameterizedTest
    @MethodSource("setArg")
    public void set(final String k, final String v, final long ex, final String exp, final boolean wait) throws InterruptedException {
        jedisUtil.set(k, v, ex);
        if (wait) Thread.sleep(ex + 10);
        String store = jedisUtil.get(k);
        Assertions.assertEquals(exp, store);
    }

    @Test
    public void testOverwrite() throws InterruptedException {
        jedisUtil.set("1", "aaa", 10*60);
        String store = jedisUtil.get("1");
        Assertions.assertEquals("aaa", store);
        if (onMock) doReturn("aab").when(jedis).get(eq("1"));
        store = jedisUtil.get("1");
        Assertions.assertEquals("aab", store);
        if (onMock) doReturn("aaa").when(jedis).get(eq("1"));
    }

    @BeforeEach
	public void setupMock() {
        if (!onMock) return;
        ReflectionTestUtils.setField(jedisUtil, "pool", jedisPool);
        doReturn(jedis).when(jedisPool).getResource();
        doReturn("").when(jedis).setex(any(byte[].class), anyLong(), any(byte[].class));
        doReturn(1L).when(jedis).del(anyString());
        doReturn("aaa").when(jedis).get(eq("1"));
        doReturn("bbb").when(jedis).get(eq("2"));
        doReturn(null).when(jedis).get(eq("3"));
        doReturn("ddd").when(jedis).get(eq("4"));
	}
}
