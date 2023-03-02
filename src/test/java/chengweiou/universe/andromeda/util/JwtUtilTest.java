package chengweiou.universe.andromeda.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import chengweiou.universe.andromeda.base.jwt.JwtUtil;
import chengweiou.universe.andromeda.base.redis.JedisUtil;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.PersonType;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;
    private JedisUtil jedisUtil = Mockito.mock(JedisUtil.class);
    @Value("${onMock.redis}")
    private boolean onMock;

    @Test
    public void sign() throws UnauthException, IllegalArgumentException, IOException {
        String token = jwtUtil.sign(Builder.set("username", "ou1111").set("person", Builder.set("id", "1").to(new Person())).set("extra", PersonType.SUPER.toString()).to(new Account()));
        System.out.println(token);
        Account account = jwtUtil.verify(token);
        System.out.println("------------------------");
        System.out.println(account);
        Assertions.assertEquals(1, account.getPerson().getId());
        Assertions.assertEquals("SUPER", account.getExtra());
    }

    @Test
    public void signOut() {
        jwtUtil.signOut(null);
        String token = jwtUtil.sign(Builder.set("username", "ou1111").set("person", Builder.set("id", "1").to(new Person())).set("extra", PersonType.SUPER.toString()).to(new Account()));
        doReturn(null).when(jedisUtil).get("jwt-blacklist-" + token);
        jwtUtil.signOut(token);
        doReturn("").when(jedisUtil).get("jwt-blacklist-" + token);
        assertThrows(UnauthException.class, () -> jwtUtil.verify(token));
    }

    @BeforeEach
	public void setupMock() {
        if (!onMock) return;
        ReflectionTestUtils.setField(jwtUtil, "jedisUtil", jedisUtil);
	}
}
