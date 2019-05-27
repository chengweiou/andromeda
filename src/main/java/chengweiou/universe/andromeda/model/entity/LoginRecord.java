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

    @Override
    public String toString() {
        return "LoginRecord{" +
                "id=" + id +
                ", account=" + account +
                ", ip='" + ip + '\'' +
                ", platform='" + platform + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", logoutTime='" + logoutTime + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
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
