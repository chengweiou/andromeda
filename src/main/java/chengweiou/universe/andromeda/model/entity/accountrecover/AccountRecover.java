package chengweiou.universe.andromeda.model.entity.accountrecover;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

@Data
public class AccountRecover implements NotNullObj, Serializable {
    private Long id;
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
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
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
    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final AccountRecover NULL = new Null();
    private static class Null extends AccountRecover implements NullObj {
    }
}
