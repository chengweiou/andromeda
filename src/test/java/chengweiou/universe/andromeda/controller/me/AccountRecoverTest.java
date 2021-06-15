package chengweiou.universe.andromeda.controller.me;


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

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
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
  @Autowired
	private CodeSendRecordDio codeSendRecordDio;
	private Account loginAccount;

	@Test
	public void findMe() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/accountRecover")
				.header("loginAccount", new Gson().toJson(loginAccount))
			).andReturn().getResponse().getContentAsString();
		Rest<AccountRecover> rest = Rest.from(result, AccountRecover.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("q1", rest.getData().getQ1());
	}

	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/me/accountRecover")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("phone", "9790000001")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());
		dio.update(data.accountRecoverList.get(0));
    // todo
    // CodeSendRecord delCodeSendRecord = codeSendRecordDio.findLastByUsername(Builder.set("username", "9790000001").to(new CodeSendRecord()));
    // codeSendRecordDio.delete(delCodeSendRecord);
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
