package chengweiou.universe.andromeda.controller.all;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.model.entity.accountrecover.AccountRecover;
import chengweiou.universe.andromeda.model.entity.codesendrecord.CodeSendRecord;
import chengweiou.universe.andromeda.model.entity.loginrecord.LoginRecord;
import chengweiou.universe.andromeda.model.entity.twofa.Twofa;
import chengweiou.universe.andromeda.model.entity.twofa.TwofaType;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.service.accountrecover.AccountRecoverDio;
import chengweiou.universe.andromeda.service.codesendrecord.CodeSendRecordDio;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.andromeda.service.twofa.TwofaDio;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
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
	private JedisPool jedisPool;
	@Autowired
	private Data data;
	@Autowired
	private LoginRecordDio loginRecordDio;
	@Autowired
	private TwofaDio twofaDio;
	@Autowired
	private CodeSendRecordDio codeSendRecordDio;
	@Autowired
	private AccountRecoverDio accountRecoverDio;
	@Autowired
	private AccountService service;
	@Autowired
	private AccountDio dio;

	@Test
	public void login() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, loginRest.getCode());
		Assertions.assertEquals(true, !loginRest.getData().getToken().equals(""));
		Assertions.assertEquals(true, !loginRest.getData().getRefreshToken().equals(""));

		result = mvc.perform(MockMvcRequestBuilders.post("/logout")
				.param("refreshToken", loginRest.getData().getRefreshToken())
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> logoutRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, logoutRest.getCode());
		LoginRecord delLoginRecord = loginRecordDio.findLastByPerson(Builder.set("person", data.accountList.get(0).getPerson()).to(new LoginRecord()));
		loginRecordDio.delete(delLoginRecord);
	}

	@Test
	public void loginWithPhoneCode() throws Exception {
		Twofa updateTwofa = Builder.set("id", data.twofaList.get(0).getId()).set("type", TwofaType.PHONE_MSG).set("codeTo", "9790000000").to(new Twofa());
		twofaDio.update(updateTwofa);

		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class, Auth.class);
		Assertions.assertEquals(ProjectRestCode.TWOFA_WAITING, loginRest.getCode());

		Twofa codedTwofa = twofaDio.findById(data.twofaList.get(0));

		result = mvc.perform(MockMvcRequestBuilders.post("/checkTwofa")
				.param("token", codedTwofa.getToken()).param("code", codedTwofa.getCode())
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> twofaRest = Rest.from(result, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, twofaRest.getCode());
		Assertions.assertEquals(true, !twofaRest.getData().getToken().equals(""));
		Assertions.assertEquals(true, !twofaRest.getData().getRefreshToken().equals(""));

		result = mvc.perform(MockMvcRequestBuilders.post("/logout")
				.param("refreshToken", twofaRest.getData().getRefreshToken())
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> logoutRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, logoutRest.getCode());
		LoginRecord delLoginRecord = loginRecordDio.findLastByPerson(Builder.set("person", data.accountList.get(0).getPerson()).to(new LoginRecord()));
		loginRecordDio.delete(delLoginRecord);
		twofaDio.update(data.twofaList.get(0));
	}

	@Test
	public void loginFail() throws Exception {
		// wrong pass
		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123e")
		).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.USERNAME_PASSWORD_MISMATCH, loginRest.getCode());
	}

	@Test
	public void loginFailInactive() throws Exception {
		Account account = Builder.set("username", "controller-test").set("password", "abcdefg").set("active", false).to(new Account());
		service.save(account);

		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "controller-test").param("password", "abcdefg")
		).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.ACCOUNT_INACTIVE, loginRest.getCode());

		dio.delete(account);
	}

	@Test
	public void checkUsername() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/account/username/check")
				.param("username", "ou")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(false, rest.getData());

		result = mvc.perform(MockMvcRequestBuilders.get("/account/username/check")
				.param("username", "ch")
		).andReturn().getResponse().getContentAsString();
		rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());
	}

	@Test
	public void forgetPassword() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/forgetPassword/1")
				.param("username", "ou")
			).andReturn().getResponse().getContentAsString();
		Rest<AccountRecover> forgetRest1 = Rest.from(result, AccountRecover.class);
		Assertions.assertEquals(BasicRestCode.OK, forgetRest1.getCode());
		Assertions.assertEquals("********00", forgetRest1.getData().getPhone());
		Assertions.assertEquals("a***@a***", forgetRest1.getData().getEmail());

		// fail wrong a1
		result = mvc.perform(MockMvcRequestBuilders.post("/forgetPassword/2")
				.param("id", forgetRest1.getData().getId().toString()).param("a1", "a2")
			).andReturn().getResponse().getContentAsString();
		Rest<String> failRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.ACCOUNT_NOT_MATCH, failRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.post("/forgetPassword/2")
				.param("id", forgetRest1.getData().getId().toString()).param("phone", "9790000000")
			).andReturn().getResponse().getContentAsString();
		Rest<String> forgetRest2 = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, forgetRest2.getCode());
		Assertions.assertEquals(null, forgetRest2.getData());

		AccountRecover indb = accountRecoverDio.findById(Builder.set("id", forgetRest1.getData().getId()).to(new AccountRecover()));

		result = mvc.perform(MockMvcRequestBuilders.post("/forgetPassword/3")
				.param("id", indb.getId().toString()).param("code", indb.getCode()).param("password", "321")
			).andReturn().getResponse().getContentAsString();
		Rest<String> forgetRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, forgetRest.getCode());
		Assertions.assertEquals(null, forgetRest.getData());

		// fail code used
		result = mvc.perform(MockMvcRequestBuilders.post("/forgetPassword/3")
				.param("id", indb.getId().toString()).param("code", indb.getCode()).param("password", "321")
			).andReturn().getResponse().getContentAsString();
		failRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.CODE_NOT_MATCH, failRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "321")
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, loginRest.getCode());
		Assertions.assertEquals(true, !loginRest.getData().getToken().equals(""));
		Assertions.assertEquals(true, !loginRest.getData().getRefreshToken().equals(""));

		result = mvc.perform(MockMvcRequestBuilders.post("/logout")
				.param("refreshToken", loginRest.getData().getRefreshToken())
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> logoutRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, logoutRest.getCode());
		LoginRecord delLoginRecord = loginRecordDio.findLastByPerson(Builder.set("person", data.accountList.get(0).getPerson()).to(new LoginRecord()));
		loginRecordDio.delete(delLoginRecord);
		service.updateByKey(Builder.set("person", data.accountList.get(0).getPerson()).set("password", "123").to(new Account()));
		accountRecoverDio.update(data.accountRecoverList.get(0));
		CodeSendRecord delCodeSendRecord = codeSendRecordDio.findLastByUsername(Builder.set("username", "9790000000").to(new CodeSendRecord()));
		codeSendRecordDio.delete(delCodeSendRecord);
	}

	@BeforeEach
	public void before() throws FailException {
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
