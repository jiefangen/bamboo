package org.panda.modules.facade.domain.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.modules.facade.domain.Todo;

import java.math.BigInteger;
import java.util.List;

/**
 * 待办事项排序参数
 *
 * @author fangen
 * @since JDK 11 2022/5/10
 */
@Setter
@Getter
public class SortParam {

    private BigInteger userId;

    private Integer workStatus;

    private List<Todo> list;
}
