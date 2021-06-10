package chengweiou.universe.andromeda.model.entity.codesendrecord;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

@Data
public class CodeSendRecord implements NotNullObj, Serializable {
    private Long id;
    private CodeSendRecordType type;
    private String username;
    private String code;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    public void fillNotRequire() {
    }

    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final CodeSendRecord NULL = new Null();
    private static class Null extends CodeSendRecord implements NullObj {
    }
}
