package chengweiou.universe.andromeda.model;

import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import lombok.Data;

@Data
public class SearchCondition extends AbstractSearchCondition {
    private Person person;
}
