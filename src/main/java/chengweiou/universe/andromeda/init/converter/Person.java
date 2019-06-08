package chengweiou.universe.andromeda.init.converter;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;

import java.io.Serializable;

public class Person implements NotNullObj, Serializable {
    private Long id;
    public static final Person NULL = new Person.Null();
    public static class Null extends Person implements NullObj {
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
