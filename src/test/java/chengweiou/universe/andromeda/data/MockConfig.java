package chengweiou.universe.andromeda.data;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import chengweiou.universe.andromeda.service.vonage.VonageManager;
import redis.clients.jedis.JedisPool;

@Profile("test")
@Configuration
public class MockConfig {
    @ConditionalOnProperty("onMock.vonage")
    @Bean
    @Primary
    public VonageManager mockVonageManager() {
        return Mockito.mock(VonageManager.class);
    }

    @ConditionalOnProperty("onMock.redis")
    @Bean
    @Primary
    public JedisPool mockJedisPool() {
        return Mockito.mock(JedisPool.class);
    }
}
