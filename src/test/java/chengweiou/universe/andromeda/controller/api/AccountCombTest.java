package chengweiou.universe.andromeda.controller.api;


import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.entity.AccountComb;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
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

@SpringBootTest
@ActiveProfiles("test")
public class AccountCombTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private LoginRecordDio loginRecordDio;
	@Autowired
	private AccountService service;

	@Test
	public void findByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/accountComb/person/1")
			).andReturn().getResponse().getContentAsString();
		Rest<AccountComb> rest = Rest.from(result, AccountComb.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("ou", rest.getData().getUsername());
		Assertions.assertEquals("chiu", rest.getData().getWechat());
	}
	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/api/accountComb")
				.param("person.id", "1")
				.param("username", "oresttest").param("password", "abcdefg").param("wechatActive", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> saveRest = Rest.from(result, Boolean.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		service.updateByPersonAndType(data.accountList.get(0));
		service.updateByPersonAndType(data.accountList.get(1));
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
