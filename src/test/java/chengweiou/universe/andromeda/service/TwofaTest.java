package chengweiou.universe.andromeda.service;

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
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.TwofaType;
import chengweiou.universe.andromeda.service.account.TwofaDio;
import chengweiou.universe.andromeda.service.account.TwofaService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class TwofaTest {
	@Autowired
	private TwofaService service;
	@Autowired
	private Data data;
	@Autowired
	private TwofaDio dio;

	@Test
	public void saveDelete() throws FailException, ProjException {
		Twofa e = Builder.set("person", Builder.set("id", "10").to(new Person())).set("type", TwofaType.PHONE_MSG).to(new Twofa());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		service.delete(e);
	}

	@Test
	public void saveDeleteFailSamePersonId() {
		Twofa e = Builder.set("person", data.twofaList.get(0).getPerson()).set("type", TwofaType.NONE).to(new Twofa());
		Assertions.assertThrows(ProjException.class, () -> service.save(e));
	}

	@Test
	public void update() {
		Twofa e = Builder.set("id", 1).set("type", TwofaType.EMAIL).set("codeTo", "a@a.c").to(new Twofa());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Twofa indb = service.findById(e);
		Assertions.assertEquals("a@a.c", indb.getCodeTo());

		dio.update(data.twofaList.get(0));
	}

	@Test
	public void updateByPerson() {
		Twofa e = Builder.set("person", data.twofaList.get(0).getPerson()).set("codeTo", "update by person").to(new Twofa());
		long count = service.updateByPerson(e);
		Assertions.assertEquals(1, count);
		Twofa indb = service.findById(data.twofaList.get(0));
		Assertions.assertEquals("update by person", indb.getCodeTo());

		dio.update(data.twofaList.get(0));
	}

	@Test
	public void findByPerson() {
		Twofa indb = service.findByPerson(Builder.set("person", data.twofaList.get(0).getPerson()).to(new Twofa()));
		Assertions.assertEquals(data.twofaList.get(0).getId(), indb.getId());
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition(), null);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void find() {
		List<Twofa> list = service.find(new SearchCondition(), null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.twofaList.get(0).getCodeTo(), list.get(0).getCodeTo());
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
