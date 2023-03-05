package chengweiou.universe.andromeda.util;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
/**
 * 同一个ip下, 30min内，登录失败 5 次，则封锁 30min
 */
public class LoginFailCache {
    private static final long max = 5;
    private static final long blockTimeMin = 30;
    private static final Cache<String, Integer> cache = Caffeine.newBuilder().maximumSize(10_000)
        .expireAfterWrite(blockTimeMin, TimeUnit.MINUTES) // 无论如何，最多 3min 就会0期
        .expireAfterAccess(1, TimeUnit.MINUTES).build(); // 每次使用加 1min，但不会超过上面的3min

    public void oneMoreFail(String ip, String username) {
        Integer failTry = cache.getIfPresent(ip);
        if (failTry == null) failTry = 0;
        failTry++;
        cache.put(ip, failTry);
        log.debug("ip: " + ip + ", fail to login: " + failTry + " times. username: " + username);
    }

    public boolean ok(String ip, String username) {
        Integer failTry = cache.getIfPresent(ip);
        if (failTry == null) return true;
        boolean result = failTry < max;
        if (!result) log.info("[BLOCK] ip: " + ip + ", fail to login: " + failTry + " times. username: " + username);
        return result;
    }

}
