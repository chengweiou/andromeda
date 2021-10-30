package chengweiou.universe.andromeda.model.entity.codesendrecord;


import org.springframework.beans.BeanUtils;

import chengweiou.universe.blackhole.model.entity.DtoEntity;
import chengweiou.universe.blackhole.model.entity.ServiceEntity;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CodeSendRecord extends ServiceEntity {
    private CodeSendRecordType type;
    private String username;
    private String code;
    public void fillNotRequire() {
    }

    public static final CodeSendRecord NULL = new Null();
    private static class Null extends CodeSendRecord implements NullObj {
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        private CodeSendRecordType type;
        private String username;
        private String code;

        public CodeSendRecord toBean() {
            CodeSendRecord result = new CodeSendRecord();
            BeanUtils.copyProperties(this, result);
            return result;
        }
    }
}
