package chengweiou.universe.andromeda.model.entity;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AccountComb implements NotNullObj, Serializable {
    private Person person;
    private String extra;
    private String username;
    @JsonIgnore
    private String password;
    private Boolean normalActive;
    private String phone;
    private Boolean phoneActive;
    private String email;
    private Boolean emailActive;
    private String wechat;
    private Boolean wechatActive;
    private String weibo;
    private Boolean weiboActive;
    private String google;
    private Boolean googleActive;
    private String facebook;
    private Boolean facebookActive;

    public static final AccountComb NULL = new Null();

    public static AccountComb from(List<Account> list) {
        if (list == null || list.size() == 0) return AccountComb.NULL;
        AccountComb result = Builder.set("person", list.get(0).getPerson()).set("extra", list.get(0).getExtra()).to(new AccountComb());
        list.stream().forEach(e -> {
            String type = e.getType().toString().toLowerCase();
            Builder.set(e.getType() == AccountType.NORMAL ? "username" : type, e.getUsername()).set(type + "Active", e.getActive()).to(result);
            if (e.getType() == AccountType.NORMAL || e.getType() == AccountType.PHONE || e.getType() == AccountType.EMAIL) Builder.set("password", e.getPassword()).to(result);
        });
        return result;
    }
    public static List<Account> toUpdate(AccountComb e) {
        List<Account> result = new ArrayList<>();
        if (e.getUsername() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.NORMAL).set("active", e.getNormalActive()).set("username", e.getUsername()).set("password", e.getPassword())
                            .to(new Account())
            );
        }
        if (e.getPhone() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.PHONE).set("active", e.getPhoneActive()).set("username", e.getPhone()).set("password", e.getPassword())
                            .to(new Account())
            );
        }
        if (e.getEmail() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.PHONE).set("active", e.getEmailActive()).set("username", e.getEmail()).set("password", e.getPassword())
                            .to(new Account())
            );
        }
        if (e.getGoogle() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.GOOGLE).set("active", e.getGoogleActive())
                            .to(new Account())
            );
        }
        if (e.getFacebook() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.FACEBOOK).set("active", e.getFacebookActive())
                            .to(new Account())
            );
        }
        if (e.getWechat() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.WECHAT).set("active", e.getWechatActive())
                            .to(new Account())
            );
        }
        if (e.getWeibo() != null) {
            result.add(
                    Builder.set("person", e.getPerson()).set("extra", e.getExtra())
                            .set("type", AccountType.WEIBO).set("active", e.getWeiboActive())
                            .to(new Account())
            );
        }
        return result;
    }

    private static class Null extends AccountComb implements NullObj {
    }
}
