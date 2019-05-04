package com.universe.andromeda.model;


import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;

import java.io.Serializable;

public class Auth implements NotNullObj, Serializable {
    private String token;
    private String refreshToken;
    private String personId;

    public static final Auth NULL = new Null();
    private static class Null extends Auth implements NullObj {
    }

    @Override
    public String toString() {
        return "Auth{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", personId='" + personId + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
