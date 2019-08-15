package chengweiou.universe.andromeda.util;

import chengweiou.universe.andromeda.base.jwt.JwtUtil;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;
    @Test
    public void sign() throws UnauthException {
        String token = jwtUtil.sign(Builder.set("username", "ou").set("person", Builder.set("id", "1").to(new Person())).set("extra", "aa").to(new Account()));
        System.out.println(token);
        Account account = jwtUtil.verify(token);
        Assertions.assertEquals("1", account.getPerson().getId());
        Assertions.assertEquals("aa", account.getExtra());
    }
}
