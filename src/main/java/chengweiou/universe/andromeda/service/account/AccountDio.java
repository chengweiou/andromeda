// package chengweiou.universe.andromeda.service.account;


// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import chengweiou.universe.andromeda.dao.AccountDao;
// import chengweiou.universe.andromeda.model.SearchCondition;
// import chengweiou.universe.andromeda.model.entity.Account;
// import chengweiou.universe.blackhole.exception.FailException;
// import chengweiou.universe.blackhole.exception.ProjException;
// import chengweiou.universe.blackhole.model.BasicRestCode;


// @Component
// public class AccountDio {
//     @Autowired
//     private AccountDao dao;

//     public void save(Account e) throws ProjException, FailException {
//         long count = dao.countByUsername(e);
//         if (count != 0) throw new ProjException("dup key: " + e.getUsername() + " exists", BasicRestCode.EXISTS);
//         e.fillNotRequire();
//         e.createAt();
//         e.updateAt();
//         count = dao.save(e);
//         if (count != 1) throw new FailException();
//     }

//     public void delete(Account e) throws FailException {
//         long count = dao.delete(e);
//         if (count != 1) throw new FailException();
//     }

//     public long update(Account e) {
//         e.updateAt();
//         return dao.update(e);
//     }

//     // cannot update username, type, will let all account become the same
//     public long updateByPerson(Account e) {
//         e.setUsername(null);
//         e.setType(null);
//         e.updateAt();
//         return dao.updateByPerson(e);
//     }
//     public long updateByPersonAndType(Account e) {
//         e.updateAt();
//         return dao.updateByPersonAndType(e);
//     }

//     public Account findById(Account e) {
//         Account result = dao.findById(e);
//         return result != null ? result : Account.NULL;
//     }

//     public Account findByUsername(Account e) {
//         Account result = dao.findByUsername(e);
//         return result != null ? result : Account.NULL;
//     }

//     public long count(SearchCondition searchCondition, Account sample) {
//         return dao.count(searchCondition, sample);
//     }

//     public List<Account> find(SearchCondition searchCondition, Account sample) {
//         searchCondition.setDefaultSort("createAt");
//         return dao.find(searchCondition, sample);
//     }

//     public long countByUsername(Account e) {
//         return dao.countByUsername(e);
//     }

// }
