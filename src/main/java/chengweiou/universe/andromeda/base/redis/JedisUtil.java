package chengweiou.universe.andromeda.base.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
    @Autowired
    private JedisPool pool;
    @Bean
    public JedisUtil getJedisUtil() {
        return new JedisUtil();
    }

    public void set(String k, String v, long expSec) {
        Jedis jedis = pool.getResource();
        jedis.setex(k, expSec, v);
        jedis.close();
    }

    public String get(String k) {
        Jedis jedis = pool.getResource();
        String result = jedis.get(k);
        jedis.close();
        return result;
    }

    public void del(String k) {
        Jedis jedis = pool.getResource();
        jedis.del(k);
        jedis.close();
    }
}
