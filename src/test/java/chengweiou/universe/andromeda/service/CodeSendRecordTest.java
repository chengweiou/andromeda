package chengweiou.universe.andromeda.service;


import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecordType;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordService;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class CodeSendRecordTest {
	@Autowired
	private CodeSendRecordService service;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		CodeSendRecord e = Builder.set("type", CodeSendRecordType.FORGET_PASSWORD).set("username", "a@a.c").set("code", "656").to(new CodeSendRecord());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		service.delete(e);
	}

	@Test
	public void updateAndFindLast() {
		CodeSendRecord e = Builder.set("id", data.codeSendRecordList.get(0).getId()).set("code", "aaa").to(new CodeSendRecord());
		long count = service.update(e);
		Assertions.assertEquals(1, count);

		CodeSendRecord indb = service.findLastByUsername(data.codeSendRecordList.get(0));
		Assertions.assertEquals("aaa", indb.getCode());

		service.update(data.codeSendRecordList.get(0));
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		CodeSendRecord sample = Builder.set("type", "REGISTER").to(new CodeSendRecord());
		List<CodeSendRecord> list = service.find(new SearchCondition(), sample);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.codeSendRecordList.get(0).getUsername(), list.get(0).getUsername());
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
