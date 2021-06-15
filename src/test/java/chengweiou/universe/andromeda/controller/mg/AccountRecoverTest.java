package chengweiou.universe.andromeda.controller.mg;


import java.util.List;

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

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;

@SpringBootTest
@ActiveProfiles("test")
public class AccountRecoverTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private AccountRecoverDio dio;

	@Test
	public void saveDelete() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/accountRecover")
				.header("inServer", "true")
				.param("person.id", "3").param("phone", "9790000000")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());
		Assertions.assertEquals(true, saveRest.getData() > 0);

		result = mvc.perform(MockMvcRequestBuilders.delete("/mg/accountRecover/" + saveRest.getData())
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
		Assertions.assertEquals(true, delRest.getData());
	}

	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/accountRecover/1")
				.header("inServer", "true")
				.param("phone", "9790000001")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		dio.update(data.accountRecoverList.get(0));
	}


	@Test
	public void findById() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/accountRecover/1")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<AccountRecover> rest = Rest.from(result, AccountRecover.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(data.accountRecoverList.get(0).getA2(), rest.getData().getA2());
	}

	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/accountRecover/count")
				.header("inServer", "true")
				.param("k", "o")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(Long.valueOf(0), rest.getData());
	}

	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/accountRecover")
				.header("inServer", "true")
				.param("k", "a")
		).andReturn().getResponse().getContentAsString();
		Rest<List<Account>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(1, rest.getData().size());
	}

	@Test
	public void saveFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/accountRecover")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void deleteFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.delete("/mg/accountRecover/0")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void updateFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/accountRecover/1")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void findByIdFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/accountRecover/0")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
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
