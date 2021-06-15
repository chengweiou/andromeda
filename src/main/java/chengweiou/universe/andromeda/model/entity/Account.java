package chengweiou.universe.andromeda.model.entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

@Data
public class Account implements NotNullObj, Serializable {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String wechat;
    private String weibo;
    private String google;
    private String facebook;
    @JsonIgnore
    private String password;
    private String oldPassword;
    private Person person;
    private Boolean active;
    private String extra;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void fillNotRequire() {
        username = username!=null ? username : "";
        phone = phone!=null ? phone : "";
        email = email!=null ? email : "";
        wechat = wechat!=null ? wechat : "";
        weibo = weibo!=null ? weibo : "";
        google = google!=null ? google : "";
        facebook = facebook!=null ? facebook : "";
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
}
