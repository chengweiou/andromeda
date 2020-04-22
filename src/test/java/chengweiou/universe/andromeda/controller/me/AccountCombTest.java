package chengweiou.universe.andromeda.controller.me;


import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.AccountComb;
import chengweiou.universe.andromeda.service.account.AccountService;
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

@SpringBootTest
@ActiveProfiles("test")
public class AccountCombTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private AccountService service;
	private Account loginAccount;

	@Test
	public void findByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/accountComb")
				.header("loginAccount", new Gson().toJson(loginAccount))
			).andReturn().getResponse().getContentAsString();
		Rest<AccountComb> rest = Rest.from(result, AccountComb.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("ou", rest.getData().getUsername());
		Assertions.assertEquals("chiu", rest.getData().getWechat());
	}
	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/accountComb")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("username", "oresttest").param("password", "abcdefg").param("wechatActive", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> saveRest = Rest.from(result, Boolean.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		service.updateByPersonAndType(data.accountList.get(0));
		service.updateByPersonAndType(data.accountList.get(1));
	}
	@Test
	public void updatePassword() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/accountComb/password")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("oldPassword", "123")
				.param("password", "abcdefg")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> saveRest = Rest.from(result, Boolean.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		service.updateByPersonAndType(data.accountList.get(0));
		service.updateByPersonAndType(data.accountList.get(1));
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
