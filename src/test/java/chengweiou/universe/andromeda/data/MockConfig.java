package chengweiou.universe.andromeda.data;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import chengweiou.universe.andromeda.service.vonage.VonageManager;

@Profile("test")
@Configuration
public class MockConfig {
    @Bean("vonageManager")
    public VonageManager vonageManager() {
        return Mockito.mock(VonageManager.class);
    }
}
