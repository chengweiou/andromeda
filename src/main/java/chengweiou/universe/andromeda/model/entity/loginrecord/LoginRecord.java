package chengweiou.universe.andromeda.model.entity.loginrecord;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

@Data
public class LoginRecord implements NotNullObj, Serializable {
    private Long id;
    private Account account;
    private String ip;
    private String platform;
    private String loginTime;
    private String logoutTime;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void fillNotRequire() {
        loginTime = loginTime != null ? loginTime : LocalDateTime.now(ZoneId.of("UTC")).toString();
        logoutTime = logoutTime != null ? logoutTime : "";
    }

    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final LoginRecord NULL = new Null();
    private static class Null extends LoginRecord implements NullObj {
        @Override
        public Account getAccount() { return Account.NULL; }
    }

}
