package chengweiou.universe.andromeda.controller.all;


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
import chengweiou.universe.andromeda.model.entity.LoginRecord;
import chengweiou.universe.andromeda.model.entity.Twofa;
import chengweiou.universe.andromeda.model.entity.TwofaType;
import chengweiou.universe.andromeda.service.account.TwofaDio;
import chengweiou.universe.andromeda.service.loginrecord.LoginRecordDio;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;

@SpringBootTest
@ActiveProfiles("test")
public class AccountTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Autowired
	private LoginRecordDio loginRecordDio;
	@Autowired
	private TwofaDio twofaDio;

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
		LoginRecord delLoginRecord = loginRecordDio.findLast(data.accountList.get(0));
		loginRecordDio.delete(delLoginRecord);
	}

	@Test
	public void loginWithPhoneCode() throws Exception {
		Twofa updateTwofa = Builder.set("id", data.twofaList.get(0).getId()).set("type", TwofaType.PHONE_MSG).set("codeTo", "9790000000").to(new Twofa());
		twofaDio.update(updateTwofa);

		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.TWOFA_WAITING, loginRest.getCode());
		
		Twofa codedTwofa = twofaDio.findById(data.twofaList.get(0));

		String result2 = mvc.perform(MockMvcRequestBuilders.post("/checkTwofa")
				.param("token", codedTwofa.getToken()).param("code", codedTwofa.getCode())
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> twofaRest = Rest.from(result2, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, twofaRest.getCode());
		Assertions.assertEquals(true, !twofaRest.getData().getToken().equals(""));
		Assertions.assertEquals(true, !twofaRest.getData().getRefreshToken().equals(""));

		result = mvc.perform(MockMvcRequestBuilders.post("/logout")
				.param("refreshToken", twofaRest.getData().getRefreshToken())
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> logoutRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, logoutRest.getCode());
		LoginRecord delLoginRecord = loginRecordDio.findLast(data.accountList.get(0));
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
		// inactive account not set person
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/account")
				.header("inServer", "true")
				.param("username", "oresttest_inactive").param("password", "abcdefg")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());
		Assertions.assertEquals(true, saveRest.getData() > 0);

		result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "oresttest_inactive").param("password", "abcdefg")
		).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.ACCOUNT_INACTIVE, loginRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.delete("/mg/account/" + saveRest.getData())
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
		Assertions.assertEquals(true, delRest.getData());
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

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	@BeforeEach
	public void init() {
		data.init();
	}
}
