package chengweiou.universe.andromeda.data;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Data {
    public List<Person> personList = Arrays.asList(
            Builder.set("id", "1").to(new Person()),
            Builder.set("id", "2").to(new Person())
    );
    @Autowired
    private AccountDio accountDio;
    public List<Account> accountList = Arrays.asList(
            Builder.set("username", "ou").set("password", "123").set("person", this.personList.get(0)).set("active", true).set("extra", "none").to(new Account()),
            Builder.set("username", "chiu").set("password", "123").set("person", this.personList.get(0)).set("active", true).set("extra", "none").to(new Account())
    );

    @Autowired
    private LoginRecordDio loginRecordDio;
    public List<LoginRecord> loginRecordList = Arrays.asList(
            Builder.set("account", this.accountList.get(0))
                    .set("ip", "127.0.0.1").set("platform", "chrom").set("loginTime", "2019-01-01T00:00:00").set("logoutTime", "").to(new LoginRecord()),
            Builder.set("account", this.accountList.get(1))
                    .set("ip", "www.google.com").set("platform", "iphone").set("loginTime", "2019-01-01T00:00:00").set("logoutTime", "").to(new LoginRecord())
    );


    public void init() {
        try {
            for(Account e: accountList) {
                accountDio.save(e);
                e.setPassword("123");
            }
            for(LoginRecord e: loginRecordList) {
                loginRecordDio.save(e);
            }
        } catch (FailException ex) {
            ex.printStackTrace();
        }
    }
    @Autowired
    private MongoTemplate mongoTemplate;
    public void clean() {
        mongoTemplate.getDb().drop();
    }
}
