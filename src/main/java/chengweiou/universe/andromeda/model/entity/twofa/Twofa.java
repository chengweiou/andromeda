package chengweiou.universe.andromeda.model.entity.twofa;


import java.time.LocalDateTime;
import java.time.ZoneId;

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
public class Twofa extends ServiceEntity {
    private Person person;
    private TwofaType type;
    private String codeTo; // 用于接受验证的设备账号
    private String token; // 和 code 一起返回， 也可以用于email的link直接登录
    private String code;
    private LocalDateTime codeExp;

    public void cleanCode() {
        token = "";
        code = "";
        codeExp = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    }

    public void fillNotRequire() {
        type = type!=null ? type : TwofaType.NONE;
        codeTo = codeTo!=null ? codeTo : "";
    }

    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final Twofa NULL = new Null();
    private static class Null extends Twofa implements NullObj {
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
        private TwofaType type;
        private String codeTo; // 用于接受验证的设备账号
        private String token; // 和 code 一起返回， 也可以用于email的link直接登录
        private String code;
        private LocalDateTime codeExp;

        public Twofa toBean() {
            Twofa result = new Twofa();
            BeanUtils.copyProperties(this, result);
            result.setPerson(Builder.set("id", personId).to(new Person()));
            return result;
        }
    }
}
