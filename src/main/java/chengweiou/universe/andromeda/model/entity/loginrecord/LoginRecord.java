package chengweiou.universe.andromeda.model.entity.loginrecord;


import java.time.Instant;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.blackhole.model.entity.DtoEntity;
import chengweiou.universe.blackhole.model.entity.ServiceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginRecord extends ServiceEntity {
    private Person person;
    private String ip;
    private String platform;
    private String loginTime;
    private String logoutTime;

    public void fillNotRequire() {
        loginTime = loginTime != null ? loginTime : Instant.now().toString();
        logoutTime = logoutTime != null ? logoutTime : "";
    }

    public static final LoginRecord NULL = new Null();
    private static class Null extends LoginRecord implements NullObj {
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
        private Long personId;
        private String ip;
        private String platform;
        private String loginTime;
        private String logoutTime;

        public LoginRecord toBean() {
            LoginRecord result = new LoginRecord();
            BeanUtils.copyProperties(this, result);
            result.setPerson(Builder.set("id", personId).to(new Person()));
            return result;
        }
    }
}
