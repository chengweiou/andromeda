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
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class CodeSendRecordTest {
	@Autowired
	private CodeSendRecordDio dio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		CodeSendRecord e = Builder.set("type", CodeSendRecordType.FORGET_PASSWORD).set("username", "a@a.c").set("code", "656").to(new CodeSendRecord());
		dio.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		dio.delete(e);
	}

	@Test
	public void updateAndFindLast() {
		CodeSendRecord e = Builder.set("id", data.codeSendRecordList.get(0).getId()).set("code", "aaa").to(new CodeSendRecord());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);

		CodeSendRecord indb = dio.findLastByUsername(data.codeSendRecordList.get(0));
		Assertions.assertEquals("aaa", indb.getCode());

		dio.update(data.codeSendRecordList.get(0));
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		CodeSendRecord sample = Builder.set("type", "REGISTER").to(new CodeSendRecord());
		List<CodeSendRecord> list = dio.find(new SearchCondition(), sample);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.codeSendRecordList.get(0).getUsername(), list.get(0).getUsername());
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
