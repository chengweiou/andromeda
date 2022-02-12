package chengweiou.universe.andromeda.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.blackhole.model.entity.DtoEntity;
import chengweiou.universe.blackhole.model.entity.DtoKey;
import chengweiou.universe.blackhole.model.entity.ServiceEntity;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Account extends ServiceEntity {
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

    public static final Account NULL = new Null();
    private static class Null extends Account implements NullObj {
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        if (person != null) result.setPersonId(person.getId());
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        private String username;
        private String phone;
        private String email;
        private String wechat;
        private String weibo;
        private String google;
        private String facebook;
        private String password;
        @DtoKey
        private Long personId;
        private Boolean active;
        private String extra;

        public Account toBean() {
            Account result = new Account();
            BeanUtils.copyProperties(this, result);
            result.setPerson(Builder.set("id", personId).to(new Person()));
            return result;
        }
    }
}
