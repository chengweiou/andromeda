package chengweiou.universe.andromeda.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest({"security.needUpper=1", "security.needNum=1", "security.needSpec=1", "security.needMixLevel=NOT_INCLUDE"})
@ActiveProfiles("test")
@Slf4j
public class SecurityUtilTest {

    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void ok() {

        String hashpw = securityUtil.hash("123aaa");
        boolean success = securityUtil.check("123aaa", hashpw);
        Assertions.assertEquals(true, success);
        log.debug("--------------------take to test data---------------");
        log.debug(hashpw);
        log.debug("--------------------take to test data---------------");
        // diff salt
        hashpw = securityUtil.hash("aaa");
        success = securityUtil.check("aaa", hashpw);
        Assertions.assertEquals(true, success);
    }

    @Test
    public void fail() {
        // pw
        String hashpw = securityUtil.hash("aaaa");
        boolean success = securityUtil.check("aaa", hashpw);
        Assertions.assertEquals(false, success);
    }


    private static Stream<Arguments> canUsePasswordArg() {
        return Stream.of(
            Arguments.of("123Aa!", true),
            Arguments.of("123456Aa!", true),
            Arguments.of("123456Aa", false),
            Arguments.of("123456A!", false),
            Arguments.of("123456a!", false),
            Arguments.of("AaaaAa!", false),
            Arguments.of("1Aa!", false)
        );
    }
    @ParameterizedTest
    @MethodSource("canUsePasswordArg")
    public void canUsePassword(final String pw, final boolean success) {
        Assertions.assertEquals(success, securityUtil.canUsePassword(pw));
    }

    private static Stream<Arguments> canUseMixLevelArg() {
        return Stream.of(
            Arguments.of("1234Aa!", "123Aa!", true),
            Arguments.of("123BAa!", "123Aa!", true),
            Arguments.of("123Aa!", "123Aa!", false),
            Arguments.of("0123Aa!3", "123Aa!", false),
            Arguments.of("123Aa!", "123Aa!bb", false)
        );
    }
    @ParameterizedTest
    @MethodSource("canUseMixLevelArg")
    public void canUseMixLevel(final String username, final String pw, final boolean success) {
        Assertions.assertEquals(success, securityUtil.canUseMixLevel(username, pw));
    }

    private static Stream<Arguments> canUseUsernameArg() {
        return Stream.of(
            Arguments.of("1234.A.a@aaa.a", true),
            Arguments.of("1234abc", true),
            Arguments.of("1234555", true),
            Arguments.of("fadgasdgsd", true),
            Arguments.of("123aaa", true),
            Arguments.of("123aaa-", true),
            Arguments.of("123aaa_", true),
            Arguments.of("1234.A.a@aaa .a", false),
            Arguments.of(".1234.A.a@aaa.a%", false),
            Arguments.of("1234.A.a@aaa.a+", false),
            Arguments.of("1234.A.a@aaa.a\\", false),
            Arguments.of("1234.A.a@aaa.a=", false),
            Arguments.of("1234.A.a@aaa.a+", false),
            Arguments.of(".1234.A.a@aaa.a", false)
        );
    }
    @ParameterizedTest
    @MethodSource("canUseUsernameArg")
    public void canUseUsername(final String username, final boolean success) {
        Assertions.assertEquals(success, securityUtil.canUseUsername(username));
    }
}
