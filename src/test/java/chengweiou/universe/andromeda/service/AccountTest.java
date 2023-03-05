package chengweiou.universe.andromeda.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.andromeda.data.Data;
import chengweiou.universe.andromeda.model.Person;
import chengweiou.universe.andromeda.model.ProjectRestCode;
import chengweiou.universe.andromeda.model.SearchCondition;
import chengweiou.universe.andromeda.model.entity.Account;
import chengweiou.universe.andromeda.service.account.AccountDio;
import chengweiou.universe.andromeda.service.account.AccountService;
import chengweiou.universe.andromeda.util.SecurityUtil;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;

@SpringBootTest
@ActiveProfiles("test")
public class AccountTest {
	@Autowired
	private AccountService service;
	@Autowired
	private AccountDio dio;
	@Autowired
	private Data data;
	@Autowired
	private SecurityUtil securityUtil;

	@Test
	public void saveDelete() throws FailException, ProjException {
		Account e = Builder.set("username", "testusernamechengweiou").set("password", "test_account-service").to(new Account());
		service.save(e);
		Assertions.assertEquals(true, e.getId() > 0);
		service.delete(e);
	}

	@Test
	public void saveDeleteFailSameName() {
		Account e = Builder.set("username", data.accountList.get(0).getUsername()).set("password", data.accountList.get(0).getPassword()).to(new Account());
		Assertions.assertThrows(FailException.class, () -> service.save(e));
	}

	@Test
	public void update() throws ProjException, FailException {
		Account e = Builder.set("id", 1).set("username", "ou11112").to(new Account());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = dio.findById(e);
		Assertions.assertEquals("ou11112", indb.getUsername());

		Builder.set("username", data.accountList.get(0).getUsername()).to(e);
		service.update(e);
	}

	@Test
	public void updatePassword() throws ProjException, FailException {
		String old = "123aaa";
		Account e = Builder.set("id", 1).set("password", "123456abc").to(new Account());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = dio.findById(e);
		Assertions.assertEquals(true, securityUtil.check("123456abc", indb.getPassword()));

		Builder.set("password", old).to(e);
		service.update(e);
	}
	@Test
	public void changePasswordByMe() throws ProjException, FailException {
		String old = "123aaa";
		Account e = Builder.set("person", data.accountList.get(0).getPerson()).set("oldPassword", old).set("password", "123456abc").to(new Account());
		long count = service.changePassword(e);
		Assertions.assertEquals(1, count);
		Account indb = dio.findById(data.accountList.get(0));
		Assertions.assertEquals(true, securityUtil.check("123456abc", indb.getPassword()));

		service.update(data.accountList.get(0));
	}

	@Test
	public void changePasswordByMeFail() throws ProjException {
		String old = "123aaa";
		Account e = Builder.set("person", data.accountList.get(0).getPerson()).set("oldPassword", "123456").set("password", old).to(new Account());
		Assertions.assertThrows(ProjException.class, () -> service.changePassword(e));
	}

	@Test
	public void updateByPerson() throws ProjException, FailException {
		Account e = Builder.set("person", data.accountList.get(0).getPerson()).set("extra", "extra by person").to(new Account());
		long count = dio.updateByKey(e);
		Assertions.assertEquals(1, count);
		Account indb = dio.findById(data.accountList.get(0));
		Assertions.assertEquals("extra by person", indb.getExtra());

		service.update(data.accountList.get(0));
	}

	@Test
	public void updatePerson() throws ProjException, FailException {
		Account e = Builder.set("id", data.accountList.get(0).getId()).set("person", Builder.set("id", "666").to(new Person())).to(new Account());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Account indb = dio.findById(data.accountList.get(0));
		Assertions.assertEquals(666, indb.getPerson().getId());
		service.update(data.accountList.get(0));
	}

	@Test
	public void findByPerson() throws ProjException {
		Account e = Builder.set("person", data.accountList.get(0).getPerson()).to(new Account());
		Account indb = dio.findByKey(e);
		Assertions.assertEquals(data.accountList.get(0).getEmail(), indb.getEmail());
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		SearchCondition searchCondition = Builder.set("k", "o").to(new SearchCondition());
		List<Account> list = dio.find(searchCondition, null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.accountList.get(0).getUsername(), list.get(0).getUsername());
	}

	@Test
	public void countByLoginUsername() {
		// todo 是否合法，在家一个config名单,不允许注册的系统名，比如管理员xxx。这边检查要加，注册也要处理。
		String username = data.accountList.get(0).getUsername();
		long count = dio.countByLoginUsername(Builder.set("username", username).to(new Account()));
		Assertions.assertEquals(1, count);
		count = dio.countByLoginUsername(Builder.set("username", username.substring(0, username.length() - 1)).to(new Account()));
		Assertions.assertEquals(0, count);
	}

	@Test
	public void findByLoginUsername() {
		// todo 是否合法，在家一个config名单,不允许注册的系统名，比如管理员xxx。这边检查要加，注册也要处理。
		String username = data.accountList.get(0).getPhone();
		Account indb = dio.findByLoginUsername(Builder.set("username", username).to(new Account()));
		Assertions.assertEquals(data.accountList.get(0).getId(), indb.getId());
	}

	@Test
	public void countByUsernameOfOther() {
		// todo 是否合法，在家一个config名单,不允许注册的系统名，比如管理员xxx。这边检查要加，注册也要处理。
		String username = data.accountList.get(0).getUsername();
		long count = dio.countByUsernameOfOther(Builder.set("username", username).to(new Account()));
		Assertions.assertEquals(1, count);
		count = dio.countByUsernameOfOther(Builder.set("username", username.substring(0, username.length() - 1)).to(new Account()));
		Assertions.assertEquals(0, count);
	}
	@Test
	public void countByPhoneOfOther() {
		// todo 是否合法，在家一个config名单,不允许注册的系统名，比如管理员xxx。这边检查要加，注册也要处理。
		String phone = data.accountList.get(0).getPhone();
		long count = dio.countByPhoneOfOther(Builder.set("phone", phone).to(new Account()));
		Assertions.assertEquals(1, count);
		count = dio.countByPhoneOfOther(Builder.set("phone", phone.substring(0, phone.length() - 1)).to(new Account()));
		Assertions.assertEquals(0, count);
	}
	@Test
	public void countByEmailOfOther() {
		// todo 是否合法，在家一个config名单,不允许注册的系统名，比如管理员xxx。这边检查要加，注册也要处理。
		String email = data.accountList.get(0).getEmail();
		long count = dio.countByEmailOfOther(Builder.set("email", email).to(new Account()));
		Assertions.assertEquals(1, count);
		count = dio.countByEmailOfOther(Builder.set("email", email.substring(0, email.length() - 1)).to(new Account()));
		Assertions.assertEquals(0, count);
	}

	@Test
	public void login() throws ProjException {
		Account indb = service.login(Builder.set("username", data.accountList.get(0).getUsername()).set("password", data.accountList.get(0).getPassword()).to(new Account()), "127.0.0.1");
		Assertions.assertEquals(true, indb.notNull());
		Assertions.assertEquals(data.accountList.get(0).getUsername(), indb.getUsername());
		data.accountList.get(0).setPassword("123aaa");
	}

	@Test
	public void loginFail() throws ProjException {
		Account indb = service.login(Builder.set("username", data.accountList.get(0).getUsername()).set("password", data.accountList.get(0).getPassword()).to(new Account()), "127.0.0.1");
		Assertions.assertEquals(true, indb.notNull());
		Assertions.assertEquals(data.accountList.get(0).getUsername(), indb.getUsername());
		data.accountList.get(0).setPassword("123aaa");
	}

	@RepeatedTest(6)
	public void loginFailTryTooMany(RepetitionInfo repetitionInfo) throws ProjException {
		Account wrongAccount = Builder.set("username", "aaa").set("password", "bbb").to(new Account());
		String ip = "127.0.0.1";
		try {
			service.login(wrongAccount, ip);
		} catch (ProjException ex) {
			Assertions.assertEquals(repetitionInfo.getCurrentRepetition() < 6 ? ProjectRestCode.USERNAME_PASSWORD_MISMATCH : ProjectRestCode.TRY_TOO_MANY, ex.getCode());
		}
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}
