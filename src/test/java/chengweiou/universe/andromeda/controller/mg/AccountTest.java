package chengweiou.universe.andromeda.controller.mg;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
@ActiveProfiles("test")
public class AccountTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private LoginRecordDio loginRecordDio;

	@Test
	public void saveDelete() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/account")
				.header("inServer", "true")
				.param("username", "oresttest").param("password", "abcdefg").param("person.id", "3")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());
		Assertions.assertEquals(true, saveRest.getData() > 0);

		result = mvc.perform(MockMvcRequestBuilders.delete("/mg/account/" + saveRest.getData())
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
		Assertions.assertEquals(true, delRest.getData());
	}

	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/account/1")
				.header("inServer", "true")
				.param("username", "otest1")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		mvc.perform(MockMvcRequestBuilders.put("/mg/account/1")
				.header("inServer", "true")
				.param("username", "ou")
		).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void updateByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/account/person/1")
				.header("inServer", "true")
				.param("active", "false")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
		).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.ACCOUNT_INACTIVE, loginRest.getCode());

		mvc.perform(MockMvcRequestBuilders.put("/mg/account/person/1")
				.header("inServer", "true")
				.param("active", "true")
		).andReturn().getResponse().getContentAsString();

		result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
		).andReturn().getResponse().getContentAsString();
		loginRest = Rest.from(result, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, loginRest.getCode(), loginRest.getMessage());

		List<LoginRecord> delLoginRecordList = loginRecordDio.find(new SearchCondition(), null);
		loginRecordDio.delete(delLoginRecordList.get(0));

	}

	@Test
	public void updatePerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/account")
				.header("inServer", "true")
				.param("username", "oresttest").param("password", "abcdefg").param("person.id", "3")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.put("/mg/account/" + saveRest.getData() + "/person")
				.header("inServer", "true")
				.param("person.id", "3")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		result = mvc.perform(MockMvcRequestBuilders.get("/mg/account/" + saveRest.getData())
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Account> findRest = Rest.from(result, Account.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(3, findRest.getData().getPerson().getId());
		Assertions.assertEquals(true, findRest.getData().getActive());

		result = mvc.perform(MockMvcRequestBuilders.delete("/mg/account/" + saveRest.getData())
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
	}

	@Test
	public void findById() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/account/1")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result, Account.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("ou", rest.getData().getUsername());
	}

	@Test
	public void findByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/account/person/1")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result, Account.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("ou", rest.getData().getUsername());
	}

	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/account/count")
				.header("inServer", "true")
				.param("k", "o")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(Long.valueOf(1), rest.getData());
	}

	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/account")
				.header("inServer", "true")
				.param("k", "c")
		).andReturn().getResponse().getContentAsString();
		Rest<List<Account>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(1, rest.getData().size());
	}

	@Test
	public void saveFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/account")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void saveDeleteFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/account")
				.header("inServer", "true")
				.param("username", data.accountList.get(0).getUsername()).param("password", "123456653")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.FAIL, saveRest.getCode());
	}

	@Test
	public void deleteFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.delete("/mg/account/0")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void updateFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/account/1")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void findByIdFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/account/0")
				.header("inServer", "true")
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}


	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		Jedis jedis = mock(Jedis.class);
		doReturn(jedis).when(jedisPool).getResource();
		doReturn("jedis").when(jedis).setex(any(byte[].class), anyLong(), any(byte[].class));

	}
	@BeforeEach
	public void init() {
		data.init();
	}
}
