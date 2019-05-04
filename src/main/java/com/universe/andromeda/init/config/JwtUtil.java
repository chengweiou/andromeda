package com.universe.andromeda.init.config;

import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.universe.andromeda.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    @Autowired
    private JwtConfig config;
    public String sign(Account account) {
        try {
            LocalDateTime.now();
            Date expiresAt = Date.from(LocalDateTime.now(ZoneId.of("UTC")).plus(config.getExpMinute(), ChronoUnit.MINUTES).atZone(ZoneId.of("UTC")).toInstant());
            return JWT.create()
                    .withIssuer(config.getIssuer())
                    .withClaim("personId", account.getPersonId())
                    .withClaim("extra", account.getExtra())
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC512(config.getSign()));
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            LogUtil.e("fail to sign jwt", exception);
            return "";
        }
    }

    public Account verify(String token) throws UnauthException {
        try {
            Algorithm algorithm = Algorithm.HMAC512(config.getSign());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(config.getIssuer())
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            jwt.getClaim("");
            return Builder
                    .set("personId", jwt.getClaim("personId").asString())
                    .set("extra", jwt.getClaim("extra").asString())
                    .to(new Account());
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            throw new UnauthException();
        }
    }
}
