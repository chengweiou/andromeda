package chengweiou.universe.andromeda.data;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import chengweiou.universe.andromeda.base.redis.RedisUtil;
import chengweiou.universe.andromeda.service.vonage.VonageManager;

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
    public RedisUtil mockRedisUtil(RedisConnectionFactory redisConnectionFactory) {
        return Mockito.mock(RedisUtil.class);
    }

}
