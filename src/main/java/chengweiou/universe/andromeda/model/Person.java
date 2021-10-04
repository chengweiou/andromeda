package chengweiou.universe.andromeda.model;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements NotNullObj, Serializable {
    private Long id;
    public static final Person NULL = new Person.Null();
    public static class Null extends Person implements NullObj {
    }
}
