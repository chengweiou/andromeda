package chengweiou.universe.andromeda.service;

import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AccountTest {
	@Autowired
	private AccountService service;

	@Test
	public void saveDelete() throws FailException {
		Account e = Builder.set("username", "testusernamechengweiou").set("password", "test_account-service").to(new Account());
		int count = service.save(e);
		Assertions.assertEquals(1, count);
		Assertions.assertEquals(true, e.getId() > 0);
		count = service.delete(e);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void update() {
		Account e = Builder.set("id", 1).set("username", "ou1").to(new Account());
		int count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = service.findById(e);
		Assertions.assertEquals("ou1", indb.getUsername());

		Builder.set("username", "ou").to(e);
		service.update(e);
	}

	@Test
	public void updateByPerson() {
		Account e = Builder.set("person", Builder.set("id", "1").to(new Person())).set("extra", "extra by person").to(new Account());
		int count = service.updateByPerson(e);
		Assertions.assertEquals(2, count);
		Account indb = service.findById(Builder.set("id", 1).to(new Account()));
		Assertions.assertEquals("extra by person", indb.getExtra());

		service.update(Builder.set("id", 1).set("username", "none").to(new Account()));
	}

	@Test
	public void updatePerson() {
		Account e = Builder.set("person", Builder.set("id", "1").to(new Person())).set("extra", "extra by person").to(new Account());
		int count = service.updateByPerson(e);
		Assertions.assertEquals(2, count);
		Account indb = service.findById(Builder.set("id", 1).to(new Account()));
		Assertions.assertEquals("extra by person", indb.getExtra());

		service.update(Builder.set("id", 1).set("username", "none").to(new Account()));
	}

	@Test
	public void count() {
		int count = service.count(new SearchCondition());
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		SearchCondition searchCondition = Builder.set("k", "o").to(new SearchCondition());
		List<Account> list = service.find(searchCondition);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals("ou", list.get(0).getUsername());
	}

	@Test
	public void login() throws ProjException {
		Account indb = service.login(Builder.set("username", "ou").set("password", "123").to(new Account()));
		Assertions.assertEquals(true, indb.isNotNull());
		Assertions.assertEquals("ou", indb.getUsername());

	}
}
