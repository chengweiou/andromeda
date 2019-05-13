package chengweiou.universe.andromeda.controller.all;


import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.andromeda.model.Auth;
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
public class AccountTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void login() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
			).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, loginRest.getCode());
		Assertions.assertEquals(true, !loginRest.getData().getToken().equals(""));
		Assertions.assertEquals(true, !loginRest.getData().getRefreshToken().equals(""));

//		result = mvc.perform(MockMvcRequestBuilders.post("/logout")
//				.param("refreshToken", "")
//		).andReturn().getResponse().getContentAsString();
//		Rest<Auth> logoutRest = Rest.from(result, Auth.class);
//		Assertions.assertEquals(BasicRestCode.OK, logoutRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.post("/logout")
				.param("refreshToken", "")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> logoutRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, logoutRest.getCode());
	}

	@Test
	public void loginFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123e")
		).andReturn().getResponse().getContentAsString();
		Rest<Auth> loginRest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.USERNAME_PASSWORD_MISMATCH, loginRest.getCode());
	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
}
