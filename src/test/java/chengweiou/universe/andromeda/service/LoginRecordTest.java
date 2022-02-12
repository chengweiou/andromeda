package chengweiou.universe.andromeda.service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class LoginRecordTest {
	@Autowired
	private LoginRecordDio dio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		LoginRecord e = Builder.set("person", data.accountList.get(0).getPerson())
                .set("ip", "193.212.242.1").set("platform", "chrome").to(new LoginRecord());
		dio.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		dio.delete(e);
	}

	@Test
	public void update() {
		LoginRecord e = Builder.set("id", data.loginRecordList.get(0).getId()).set("logoutTime", Instant.now().toString()).to(new LoginRecord());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);

		LoginRecord indb = dio.findLastByPerson(Builder.set("person", data.accountList.get(0).getPerson()).to(new LoginRecord()));
		Assertions.assertEquals(true, indb.getLogoutTime().startsWith(LocalDate.now(ZoneId.of("UTC")).toString()));

		Builder.set("logoutTime", "").to(e);
		dio.update(e);
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		SearchCondition searchCondition = Builder.set("k", "ch").to(new SearchCondition());
		List<LoginRecord> list = dio.find(searchCondition, null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.loginRecordList.get(0).getPerson().getId(), list.get(0).getPerson().getId());
	}

    @Test
    public void countByPerson() {
			LoginRecord sample = Builder.set("person", data.accountList.get(0).getPerson()).to(new LoginRecord());
			long count = dio.count(new SearchCondition(), sample);
			Assertions.assertEquals(2, count);
    }

    @Test
    public void findByPerson() {
			SearchCondition searchCondition = Builder.set("k", "iphone").to(new SearchCondition());
			LoginRecord sample = Builder.set("person", data.accountList.get(0).getPerson()).to(new LoginRecord());
			List<LoginRecord> list = dio.find(searchCondition, sample);
			Assertions.assertEquals(1, list.size());
			Assertions.assertEquals(data.loginRecordList.get(0).getPerson().getId(), list.get(0).getPerson().getId());
    }
	@BeforeEach
	public void init() {
		data.init();
	}
}
