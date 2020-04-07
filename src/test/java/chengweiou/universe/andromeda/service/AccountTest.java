package chengweiou.universe.andromeda.service;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		Account e = Builder.set("username", "testusernamechengweiou").set("password", "test_account-service").to(new Account());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		service.delete(e);
	}

	@Test
	public void saveDeleteFailSameName() {
		Account e = Builder.set("username", data.accountList.get(0).getUsername()).set("password", data.accountList.get(0).getPassword()).to(new Account());
		Assertions.assertThrows(ProjException.class, () -> service.save(e));
	}

	@Test
	public void update() {
		Account e = Builder.set("id", 1).set("username", "ou1").to(new Account());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = service.findById(e);
		Assertions.assertEquals("ou1", indb.getUsername());

		Builder.set("username", data.accountList.get(0).getUsername()).to(e);
		service.update(e);
	}

	@Test
	public void updatePassword() {
		String old = "123";
		Account e = Builder.set("id", 1).set("password", "123456").to(new Account());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = service.findById(e);
		Assertions.assertEquals(true, SecurityUtil.check("123456", indb.getPassword()));

		Builder.set("password", old).to(e);
		service.update(e);
	}

	@Test
	public void updateByPerson() {
		Account e = Builder.set("person", data.accountList.get(0).getPerson()).set("extra", "extra by person").to(new Account());
		long count = service.updateByPerson(e);
		Assertions.assertEquals(2, count);
		Account indb = service.findById(data.accountList.get(0));
		Assertions.assertEquals("extra by person", indb.getExtra());

		service.update(data.accountList.get(0));
	}

	@Test
	public void updatePerson() {
		Account e = Builder.set("id", data.accountList.get(0).getId()).set("person", Builder.set("id", "666").to(new Person())).to(new Account());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = service.findById(data.accountList.get(0));
		Assertions.assertEquals("666", indb.getPerson().getId());
		service.update(data.accountList.get(0));
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition());
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		SearchCondition searchCondition = Builder.set("k", "o").to(new SearchCondition());
		List<Account> list = service.find(searchCondition);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.accountList.get(0).getUsername(), list.get(0).getUsername());
	}

	@Test
	public void countByName() {
		// todo 是否合法，在家一个config名单,不允许注册的系统名，比如管理员xxx。这边检查要加，注册也要处理。
		String username = data.accountList.get(0).getUsername();
		long count = service.countByUsername(Builder.set("username", username).to(new Account()));
		Assertions.assertEquals(1, count);
		count = service.countByUsername(Builder.set("username", username.substring(0, username.length() - 1)).to(new Account()));
		Assertions.assertEquals(0, count);
	}

	@Test
	public void login() throws ProjException {
		Account indb = service.login(Builder.set("username", data.accountList.get(0).getUsername()).set("password", data.accountList.get(0).getPassword()).to(new Account()));
		Assertions.assertEquals(true, indb.notNull());
		Assertions.assertEquals(data.accountList.get(0).getUsername(), indb.getUsername());
		data.accountList.get(0).setPassword("123");

	}
	@BeforeEach
	public void init() {
		data.init();
	}
}
