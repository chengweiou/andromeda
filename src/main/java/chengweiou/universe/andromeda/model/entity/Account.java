// package chengweiou.universe.andromeda.model.entity;


// import chengweiou.universe.andromeda.model.Person;
// import chengweiou.universe.blackhole.model.Builder;
// import chengweiou.universe.blackhole.model.NotNullObj;
// import chengweiou.universe.blackhole.model.NullObj;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import lombok.Data;

// import java.io.Serializable;
// import java.time.LocalDateTime;
// import java.time.ZoneId;

// @Data
// public class Account implements NotNullObj, Serializable {
//     private Long id;
//     private AccountType type;
//     private String username;
//     @JsonIgnore
//     private String password;
//     private Person person;
//     private Boolean active;
//     private String extra;
//     private LocalDateTime createAt;
//     private LocalDateTime updateAt;
//     public void fillNotRequire() {
//         type = type!=null ? type : AccountType.NORMAL;
//         active = active!=null ? active : person != null;
//         person = person!=null ? person : Builder.set("id", "0").to(new Person());
//         extra = extra!=null ? extra : "";
//     }

//     public void createAt() {
//         createAt = LocalDateTime.now(ZoneId.of("UTC"));
//     }
//     public void updateAt() {
//         updateAt = LocalDateTime.now(ZoneId.of("UTC"));
//     }

//     public static final Account NULL = new Null();
//     private static class Null extends Account implements NullObj {
//     }
// }
