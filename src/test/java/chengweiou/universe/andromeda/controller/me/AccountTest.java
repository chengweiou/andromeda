package chengweiou.universe.andromeda.controller.me;


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
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.GsonUtil;

@SpringBootTest
@ActiveProfiles("test")
public class AccountTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private AccountDio dio;
	@Autowired
	private AccountService service;
	private Account loginAccount;

	@Test
	public void findMe() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/account")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result, Account.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("ou", rest.getData().getUsername());
	}

	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/account")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("username", "otest1")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		dio.update(Builder.set("id", 1).set("username", "ou").to(new Account()));
	}

	@Test
	public void updatePassword() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/account/password")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("oldPassword", "123")
				.param("password", "abcdefg")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> saveRest = Rest.from(result, Boolean.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		service.updateByPerson(Builder.set("person", loginAccount.getPerson()).set("password", "123").to(new Account()));
	}

	@Test
	public void updateFailUnauth() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/account")
				.param("username", "otest1")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.UNAUTH, rest.getCode());
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
