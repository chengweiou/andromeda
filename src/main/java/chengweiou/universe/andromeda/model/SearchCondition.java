package chengweiou.universe.andromeda.model;

import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import org.springframework.data.domain.Sort;

public class SearchCondition extends AbstractSearchCondition {
    public Sort getMongoSort() {
        Sort result = Sort.by(super.isDefaultSortAz() ? Sort.Direction.ASC : Sort.Direction.DESC, super.getDefaultSort());
        if (super.getSort() != null) {
            result = Sort.by(super.isSortAz() ? Sort.Direction.ASC : Sort.Direction.DESC, super.getSort()).and(result);
        }
        return result;
    }
}
