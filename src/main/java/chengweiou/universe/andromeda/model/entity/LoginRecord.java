package chengweiou.universe.andromeda.model.entity;


import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LoginRecord implements NotNullObj, Serializable {
    private Long id;
    private Account account;
    private String ip;
    private String platform;
    private LocalDateTime updateAt;

//    todo 加入登出时间
    public void fillNotRequire() {
    }

    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final LoginRecord NULL = new Null();
    private static class Null extends LoginRecord implements NullObj {
        @Override
        public Account getAccount() { return Account.NULL; }
    }

    @Override
    public String toString() {
        return "LoginRecord{" +
                "id=" + id +
                ", account=" + account +
                ", ip='" + ip + '\'' +
                ", platform='" + platform + '\'' +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
