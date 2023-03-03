package chengweiou.universe.andromeda.base.redis;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(String k, String v, long expSec) {
        redisTemplate.opsForValue().set(k, v, expSec, TimeUnit.SECONDS);
    }

    public String get(String k) {
        String result = redisTemplate.opsForValue().get(k);
        return result;
    }

    public void del(String k) {
        redisTemplate.delete(k);
    }
}
