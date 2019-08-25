package chengweiou.universe.andromeda.model;


import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

import java.io.Serializable;

@Data
public class Auth implements NotNullObj, Serializable {
    private String token;
    private String refreshToken;
    private Person person;

    public static final Auth NULL = new Null();

    private static class Null extends Auth implements NullObj {
        @Override
        public Person getPerson() { return Person.NULL; }
    }
}
