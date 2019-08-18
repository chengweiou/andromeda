package chengweiou.universe.andromeda.controller.api;


import chengweiou.universe.andromeda.model.Auth;
import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.entity.Account;
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

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AccountTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void saveDelete() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/api/account")
				.param("username", "oresttest").param("password", "abcdefg").param("person.id", "3")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());
		Assertions.assertEquals(true, saveRest.getData() > 0);

		result = mvc.perform(MockMvcRequestBuilders.delete("/api/account/" + saveRest.getData())
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
		Assertions.assertEquals(true, delRest.getData());
	}

	@Test
	public void update() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/api/account/1")
				.param("username", "otest1")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		mvc.perform(MockMvcRequestBuilders.put("/api/account/1")
				.param("username", "ou")
		).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void updateByPerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/api/account/person/1")
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

		mvc.perform(MockMvcRequestBuilders.put("/api/account/person/1")
				.param("active", "true")
		).andReturn().getResponse().getContentAsString();

		result = mvc.perform(MockMvcRequestBuilders.post("/login")
				.param("username", "ou").param("password", "123")
		).andReturn().getResponse().getContentAsString();
		loginRest = Rest.from(result, Auth.class);
		Assertions.assertEquals(BasicRestCode.OK, loginRest.getCode(), loginRest.getMessage());
	}

	@Test
	public void updatePerson() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/api/account")
				.param("username", "oresttest").param("password", "abcdefg").param("person.id", "3")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.put("/api/account/" + saveRest.getData() + "/person")
				.param("person.id", "3")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		result = mvc.perform(MockMvcRequestBuilders.get("/api/account/" + saveRest.getData())
		).andReturn().getResponse().getContentAsString();
		Rest<Account> findRest = Rest.from(result, Account.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("3", findRest.getData().getPerson().getId());
		Assertions.assertEquals(true, findRest.getData().getActive());

		result = mvc.perform(MockMvcRequestBuilders.delete("/api/account/" + saveRest.getData())
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
	}

	@Test
	public void findById() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/account/1")
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result, Account.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals("ou", rest.getData().getUsername());
	}

	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/account/count")
				.param("k", "o")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(Long.valueOf(1), rest.getData());
	}

	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/account")
				.param("k", "c")
		).andReturn().getResponse().getContentAsString();
		Rest<List<Account>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(1, rest.getData().size());
	}

	@Test
	public void saveFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/api/account")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void deleteFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.delete("/api/account/0")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void updateFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/api/account/1")
			).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void findByIdFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/api/account/0")
			).andReturn().getResponse().getContentAsString();
		Rest<Account> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
}
