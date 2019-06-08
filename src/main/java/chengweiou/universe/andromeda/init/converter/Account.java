package chengweiou.universe.andromeda.init.converter;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;

import java.io.Serializable;

public class Account implements NotNullObj, Serializable {
    private Person person;
    private String extra;
    public static final Account NULL = new Account.Null();
    public static class Null extends Account implements NullObj {
        @Override
        public Person getPerson() { return Person.NULL; }
    }

    @Override
    public String toString() {
        return "Account{" +
                "person=" + person +
                ", extra='" + extra + '\'' +
                '}';
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
