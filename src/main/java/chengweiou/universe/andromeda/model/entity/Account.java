package chengweiou.universe.andromeda.model.entity;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Account implements NotNullObj, Serializable {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private Person person;
    private Boolean active;
    private String extra;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    public void fillNotRequire() {
        active = active!=null ? active : person != null;
        person = person!=null ? person : Builder.set("id", "0").to(new Person());
        extra = extra!=null ? extra : "";
    }

    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final Account NULL = new Null();
    private static class Null extends Account implements NullObj {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", person=" + person +
                ", active=" + active +
                ", extra='" + extra + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
