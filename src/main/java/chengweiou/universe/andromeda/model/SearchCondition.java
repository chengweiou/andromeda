package chengweiou.universe.andromeda.model;

import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class SearchCondition extends AbstractSearchCondition {
    private List<String> accountIdList;
}
