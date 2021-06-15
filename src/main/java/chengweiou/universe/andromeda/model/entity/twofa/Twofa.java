package chengweiou.universe.andromeda.model.entity.twofa;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

@Data
public class Twofa implements NotNullObj, Serializable {
    private Long id;
    private Person person;
    private TwofaType type;
    private String codeTo; // 用于接受验证的设备账号
    private Account loginAccount; // 生成 jwt 时候需要的参数, 本次登录时候的账号
    private String token; // 和 code 一起返回， 也可以用于email的link直接登录
    private String code;
    private LocalDateTime codeExp;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void cleanCode() {
        loginAccount = Builder.set("id", 0).to(new Account());
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
}
