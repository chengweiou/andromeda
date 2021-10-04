package chengweiou.universe.andromeda.model.entity.accountrecover;


import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.andromeda.base.entity.DtoEntity;
import chengweiou.universe.andromeda.base.entity.DtoKey;
import chengweiou.universe.andromeda.base.entity.ServiceEntity;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountRecover extends ServiceEntity {
    private Person person;
    private String phone;
    private String email;
    private String q1;
    private String q2;
    private String q3;
    private String a1;
    private String a2;
    private String a3;
    private String code;
    private LocalDateTime codeExp;
    private Integer codeCount;
    public void cleanCode() {
        code = "";
        codeExp = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
        codeCount = 0;
    }
    public void fillNotRequire() {
        phone = phone!=null ? phone : "";
        email = email!=null ? phone : "";
        q1 = q1!=null ? q1 : "";
        q2 = q2!=null ? q2 : "";
        q3 = q3!=null ? q3 : "";
        a1 = a1!=null ? a1 : "";
        a2 = a2!=null ? a2 : "";
        a3 = a3!=null ? a3 : "";
    }

    public static final AccountRecover NULL = new Null();
    private static class Null extends AccountRecover implements NullObj {
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
        @DtoKey
        private Long personId;
        private String phone;
        private String email;
        private String q1;
        private String q2;
        private String q3;
        private String a1;
        private String a2;
        private String a3;
        private String code;
        private LocalDateTime codeExp;
        private Integer codeCount;

        public AccountRecover toBean() {
            AccountRecover result = new AccountRecover();
            BeanUtils.copyProperties(this, result);
            result.setPerson(Builder.set("id", personId).to(new Person()));
            return result;
        }
    }
}
