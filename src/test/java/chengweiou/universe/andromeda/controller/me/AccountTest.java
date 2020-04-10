package chengweiou.universe.andromeda.controller.me;


import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import com.google.gson.Gson;
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
public class AccountTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private AccountDio accountDio;
	private Account loginAccount;

	// todo
	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/account/2")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("username", "otest1")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		accountDio.update(Builder.set("id", 2).set("username", "chiu").to(new Account()));
	}

	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/account/count")
				.header("loginAccount", new Gson().toJson(loginAccount))
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(Long.valueOf(2), rest.getData());
	}

	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/account")
				.header("loginAccount", new Gson().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<List<Account>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(2, rest.getData().size());
	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		loginAccount = Builder.set("person", Builder.set("id", 1L).to(new Person()))
				.set("extra", "MEMBER")
				.to(new Account());
	}
	@BeforeEach
	public void init() {
		data.init();
	}
}
