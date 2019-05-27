package chengweiou.universe.andromeda.service;


import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.blackhole.model.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class LoginRecordTest {
	@Autowired
	private LoginRecordService service;

	@Test
	public void saveDelete() {
		LoginRecord e = Builder.set("account", Builder.set("id", 1L).set("person", Builder.set("id", "1").to(new Person())).to(new Account()))
                .set("ip", "193.212.242.1").set("platform", "chrome").to(new LoginRecord());
		int count = service.save(e);
		Assertions.assertEquals(1, count);
		Assertions.assertEquals(true, e.getId() > 0);
		count = service.delete(e);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void count() {
		int count = service.count(new SearchCondition());
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		SearchCondition searchCondition = Builder.set("k", "ch").to(new SearchCondition());
		List<LoginRecord> list = service.find(searchCondition);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(1, list.get(0).getAccount().getId());
	}

    @Test
    public void countByPerson() {
        int count = service.count(new SearchCondition(), Builder.set("id", "1").to(new Person()));
        Assertions.assertEquals(2, count);
    }

    @Test
    public void findByPerson() {
        SearchCondition searchCondition = Builder.set("k", "iphone").to(new SearchCondition());
        List<LoginRecord> list = service.find(searchCondition, Builder.set("id", "1").to(new Person()));
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(2, list.get(0).getAccount().getId());
    }
}
