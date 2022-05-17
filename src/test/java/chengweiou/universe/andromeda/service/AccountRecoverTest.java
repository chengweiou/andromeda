package chengweiou.universe.andromeda.service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverService;
import chengweiou.universe.andromeda.util.SecurityUtil;
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
	private AccountDio accountDio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		AccountRecover e = Builder.set("person", Builder.set("id", "10").to(new Person())).set("q1", "q1-service-test").set("a1", "a1-service-test").to(new AccountRecover());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		dio.delete(e);
	}

	@Test
	public void update() {
		AccountRecover e = Builder.set("id", data.accountRecoverList.get(0).getId()).set("q1", "q1-service-test").to(new AccountRecover());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);

		AccountRecover indb = dio.findById(data.accountRecoverList.get(0));
		Assertions.assertEquals("q1-service-test", indb.getQ1());

		dio.update(data.accountRecoverList.get(0));
	}

	@Test
	public void updateByPerson() {
		AccountRecover e = Builder.set("person", data.accountRecoverList.get(0).getPerson()).set("q1", "q1-service-test").to(new AccountRecover());
		long count = dio.updateByKey(e);
		Assertions.assertEquals(1, count);

		AccountRecover indb = dio.findById(data.accountRecoverList.get(0));
		Assertions.assertEquals("q1-service-test", indb.getQ1());

		dio.update(data.accountRecoverList.get(0));
	}

	@Test
	public void countByKey() {
		AccountRecover e = Builder.set("person", data.accountRecoverList.get(0).getPerson()).to(new AccountRecover());
		long count = dio.countByKey(e);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void findByKey() {
		AccountRecover e = Builder.set("person", data.accountRecoverList.get(0).getPerson()).to(new AccountRecover());
		AccountRecover indb = dio.findByKey(e);
		Assertions.assertEquals("a2", indb.getA2());
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void find() {
		List<AccountRecover> list = dio.find(new SearchCondition(), null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals("a1", list.get(0).getA1());
	}

	@Test
	public void forgetPasswordS1() throws Exception {
		AccountRecover indb = service.forgetPasswordS1(Builder.set("username", "9790000000").to(new Account()));
		Assertions.assertEquals("********00", indb.getPhone());
		Assertions.assertEquals("a***@a***", indb.getEmail());
	}
	@Test
	public void forgetPasswordS1Fail() throws Exception {
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS1(Builder.set("username", "9790001111").to(new Account())));
	}
	@Test
	public void forgetPasswordS2() throws Exception {
		String code = service.forgetPasswordS2(Builder.set("id", data.accountRecoverList.get(0).getId()).set("phone", "9790000000").to(new AccountRecover()));
		Assertions.assertEquals(true, code != null);
		code = service.forgetPasswordS2(Builder.set("id", data.accountRecoverList.get(0).getId()).set("a1", "a1").to(new AccountRecover()));
		Assertions.assertEquals(true, code != null);

		dio.update(data.accountRecoverList.get(0));
	}
	@Test
	public void forgetPasswordS2Fail() throws Exception {
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS2(Builder.set("id", data.accountRecoverList.get(0).getId()).set("phone", "9790001111").to(new AccountRecover())));
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS2(Builder.set("id", data.accountRecoverList.get(0).getId()).set("a1", "a2").to(new AccountRecover())));
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS2(Builder.set("id", data.accountRecoverList.get(0).getId()).to(new AccountRecover())));
	}

	@Test
	public void forgetPasswordS3() throws Exception {
		dio.update(Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", "123").set("codeExp", Instant.now().plus(10, ChronoUnit.MINUTES)).to(new AccountRecover()));

		long count = service.forgetPasswordS3(
			Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", "123").to(new AccountRecover()),
			Builder.set("password", "123").to(new Account())
		);
		Assertions.assertEquals(1, count);
		Account accountIndb = accountDio.findById(data.accountList.get(0));
		Assertions.assertEquals(true, SecurityUtil.check(data.accountList.get(0).getPassword(), accountIndb.getPassword()));

		dio.update(data.accountRecoverList.get(0));
	}

	@Test
	public void forgetPasswordS3Fail() throws Exception {
		dio.update(Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", "123").set("codeExp", Instant.now().plus(10, ChronoUnit.MINUTES)).to(new AccountRecover()));
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS3(Builder.set("phone", "9790001111").to(new AccountRecover()), data.accountList.get(0)));
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS3(Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", "345").to(new AccountRecover()), data.accountList.get(0)));
		dio.update(Builder.set("id", data.accountRecoverList.get(0).getId()).set("code", "123").set("codeExp", Instant.now().minus(10, ChronoUnit.MINUTES)).to(new AccountRecover()));
		Assertions.assertThrows(ProjException.class, () -> service.forgetPasswordS3(Builder.set("id", data.accountRecoverList.get(0).getId()).to(new AccountRecover()), data.accountList.get(0)));
		dio.update(data.accountRecoverList.get(0));
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
