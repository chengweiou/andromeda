package chengweiou.universe.andromeda.model;


import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;

import java.io.Serializable;

public class Auth implements NotNullObj, Serializable {
    private String token;
    private String refreshToken;
    private Person person;

    public static final Auth NULL = new Null();

    private static class Null extends Auth implements NullObj {
        @Override
        public Person getPerson() { return Person.NULL; }
    }

    @Override
    public String toString() {
        return "Auth{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", person=" + person +
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
