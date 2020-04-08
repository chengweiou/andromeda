package chengweiou.universe.andromeda.controller.api;


import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class LoginRecordTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;

	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/loginRecord/count")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(2, rest.getData());
	}
	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/loginRecord")
		).andReturn().getResponse().getContentAsString();
		Rest<List<LoginRecord>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(2, rest.getData().size());
	}

	@Test
	public void countByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/loginRecord/count")
				.param("account.person.id", "1")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(2, rest.getData());
	}
	@Test
	public void findByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/loginRecord")
				.param("account.person.id", "1")
		).andReturn().getResponse().getContentAsString();
		Rest<List<LoginRecord>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(2, rest.getData().size());
	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	@BeforeEach
	public void init() {
		data.init();
	}
}
