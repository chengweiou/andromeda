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
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.model.entity.twofa.TwofaType;
import chengweiou.universe.andromeda.service.twofa.TwofaDio;
import chengweiou.universe.andromeda.service.twofa.TwofaService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class TwofaTest {
	@Autowired
	private TwofaService service;
	@Autowired
	private TwofaDio dio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		Twofa e = Builder.set("person", Builder.set("id", "10").to(new Person())).set("type", TwofaType.PHONE_MSG).to(new Twofa());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		dio.delete(e);
	}

	@Test
	public void saveDeleteFailSamePersonId() {
		Twofa e = Builder.set("person", data.twofaList.get(0).getPerson()).set("type", TwofaType.NONE).to(new Twofa());
		Assertions.assertThrows(FailException.class, () -> service.save(e));
	}

	@Test
	public void update() {
		Twofa e = Builder.set("id", 1).set("type", TwofaType.EMAIL).set("codeTo", "a@a.c").to(new Twofa());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);
		Twofa indb = dio.findById(e);
		Assertions.assertEquals("a@a.c", indb.getCodeTo());

		dio.update(data.twofaList.get(0));
	}

	@Test
	public void updateByPerson() {
		Twofa e = Builder.set("person", data.twofaList.get(0).getPerson()).set("codeTo", "update by person").to(new Twofa());
		long count = dio.updateByKey(e);
		Assertions.assertEquals(1, count);
		Twofa indb = dio.findById(data.twofaList.get(0));
		Assertions.assertEquals("update by person", indb.getCodeTo());

		dio.update(data.twofaList.get(0));
	}

	@Test
	public void findByPerson() {
		Twofa indb = dio.findByKey(Builder.set("person", data.twofaList.get(0).getPerson()).to(new Twofa()));
		Assertions.assertEquals(data.twofaList.get(0).getId(), indb.getId());
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void find() {
		List<Twofa> list = dio.find(new SearchCondition(), null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.twofaList.get(0).getCodeTo(), list.get(0).getCodeTo());
	}

	@Test
	public void findAndWaitForLogin() throws ProjException {
		Twofa twofa = service.findAndWaitForLogin(Builder.set("person", data.accountList.get(0).getPerson()).to(new Twofa()));
		Assertions.assertEquals(true, !twofa.notNull());
		dio.update(Builder.set("id", data.twofaList.get(0).getId()).set("type", TwofaType.PHONE_MSG).to(new Twofa()));
		twofa = service.findAndWaitForLogin(Builder.set("person", data.accountList.get(0).getPerson()).to(new Twofa()));
		Assertions.assertEquals(true, twofa.getCode().length() > 0);
		Assertions.assertEquals(true, twofa.getToken().length() > 0);
		Assertions.assertEquals(true, twofa.getCodeExp().isAfter(Instant.now()));
		dio.update(data.twofaList.get(0));
	}

	@Test
	public void findAndWaitForLoginFail() throws ProjException {
		dio.update(Builder.set("id", data.twofaList.get(0).getId()).set("type", TwofaType.PHONE_MSG).set("codeExp", Instant.now().plus(1, ChronoUnit.MINUTES)).to(new Twofa()));
		Assertions.assertThrows(ProjException.class, () -> service.findAndWaitForLogin(Builder.set("person", data.accountList.get(0).getPerson()).to(new Twofa())));
		dio.update(data.twofaList.get(0));
	}

	@Test
	public void findAfterCheckCode() throws ProjException {
		dio.update(Builder.set("id", data.twofaList.get(0).getId()).set("code", "123").set("token", "123").set("codeExp", Instant.now().plus(1, ChronoUnit.MINUTES)).to(new Twofa()));
		Twofa twofa = service.findAfterCheckCode(Builder.set("code", "123").set("token", "123").to(new Twofa()));
		Assertions.assertEquals(twofa.getPerson().getId(), data.twofaList.get(0).getPerson().getId());
		// 一次性
		Assertions.assertThrows(ProjException.class, () -> service.findAfterCheckCode(Builder.set("code", "123").set("token", "123").to(new Twofa())));
		dio.update(data.twofaList.get(0));

		dio.update(Builder.set("id", 1).set("type", TwofaType.EMAIL).set("codeTo", "a@a.c").set("person", data.accountList.get(0).getPerson()).set("token", "aaa").set("code", "111").set("codeExp", Instant.now().plus(1, ChronoUnit.MINUTES)).to(new Twofa()));
		twofa = service.findAfterCheckCode(Builder.set("token", "aaa").set("code", "111").to(new Twofa()));
		Assertions.assertEquals(data.twofaList.get(0).getId(), twofa.getId());

		dio.update(data.twofaList.get(0));
	}

	@Test
	public void findAfterCheckCodeFail() throws ProjException {
		dio.update(Builder.set("id", data.twofaList.get(0).getId()).set("code", "123").set("token", "123").set("codeExp", Instant.now().minus(1, ChronoUnit.MINUTES)).to(new Twofa()));
		Assertions.assertThrows(ProjException.class, () -> service.findAfterCheckCode(new Twofa()));
		Assertions.assertThrows(ProjException.class, () -> service.findAfterCheckCode(new Twofa()));
		dio.update(data.twofaList.get(0));

		dio.update(Builder.set("id", 1).set("type", TwofaType.EMAIL).set("codeTo", "a@a.c").set("person", data.accountList.get(0).getPerson()).set("token", "aaa").set("code", "111").to(new Twofa()));
		Assertions.assertThrows(ProjException.class, () -> service.findAfterCheckCode(Builder.set("token", "aaa").set("code", "222").to(new Twofa())));

		dio.update(data.twofaList.get(0));
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
