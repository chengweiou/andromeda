package chengweiou.universe.andromeda.service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class AccountRecoverTest {
	@Autowired
	private AccountRecoverService service;
	@Autowired
	private AccountRecoverDio dio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		AccountRecover e = Builder.set("person", Builder.set("id", "10").to(new Person())).set("q1", "q1-service-test").set("a1", "a1-service-test").to(new AccountRecover());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		service.delete(e);
	}

	@Test
	public void update() {
		AccountRecover e = Builder.set("id", data.accountRecoverList.get(0).getId()).set("q1", "q1-service-test").to(new AccountRecover());
		long count = service.update(e);
		Assertions.assertEquals(1, count);

		AccountRecover indb = service.findById(data.accountRecoverList.get(0));
		Assertions.assertEquals("q1-service-test", indb.getQ1());

		service.update(data.accountRecoverList.get(0));
	}

	@Test
	public void updateByPerson() {
		AccountRecover e = Builder.set("person", data.accountRecoverList.get(0).getPerson()).set("q1", "q1-service-test").to(new AccountRecover());
		long count = service.updateByPerson(e);
		Assertions.assertEquals(1, count);

		AccountRecover indb = service.findById(data.accountRecoverList.get(0));
		Assertions.assertEquals("q1-service-test", indb.getQ1());

		service.update(data.accountRecoverList.get(0));
	}

	@Test
	public void countByKey() {
		AccountRecover e = Builder.set("person", data.accountRecoverList.get(0).getPerson()).to(new AccountRecover());
		long count = service.countByKey(e);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void findByKey() {
		AccountRecover e = Builder.set("person", data.accountRecoverList.get(0).getPerson()).to(new AccountRecover());
		AccountRecover indb = service.findByKey(e);
		Assertions.assertEquals("a2", indb.getA2());
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition(), null);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void find() {
		List<AccountRecover> list = service.find(new SearchCondition(), null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals("a1", list.get(0).getA1());
	}

	@Test
	public void findByActiveCode() throws FailException, ProjException {
		String code = RandomStringUtils.random(50);
		AccountRecover e = Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", code).set("codeExp", Instant.now().minus(10, ChronoUnit.MINUTES)).to(new AccountRecover());
		dio.update(e);
		AccountRecover indb = service.findByActiveCode(e);
		Assertions.assertEquals(data.accountRecoverList.get(0).getA1(), indb.getA1());
		Assertions.assertEquals("", indb.getCode());
		dio.update(data.accountRecoverList.get(0));
	}

	@Test
	public void findByActiveCodeFail() throws FailException, ProjException {
		String code = RandomStringUtils.random(50);
		AccountRecover e = Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", code).set("codeExp", Instant.now().minus(10, ChronoUnit.MINUTES)).to(new AccountRecover());
		dio.update(e);
		// expired code
		Assertions.assertThrows(ProjException.class, () -> service.findByActiveCode(e));
		dio.update(data.accountRecoverList.get(0));
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
