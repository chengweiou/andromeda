package com.universe.andromeda.service;


import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.SearchCondition;
import com.universe.andromeda.model.entity.Account;
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
	public void saveDelete() {
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
	public void findByUsernameAndPassword() {
		Account indb = service.findByUsername(Builder.set("username", "ou").to(new Account()));
		Assertions.assertEquals(true, !indb.isNull());
		Assertions.assertEquals("ou", indb.getUsername());

	}
}
