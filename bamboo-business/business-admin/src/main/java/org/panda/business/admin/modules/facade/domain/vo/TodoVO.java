package org.panda.business.admin.modules.facade.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.facade.domain.Todo;

import java.util.ArrayList;
import java.util.List;

/**
 * 代办事项视图
 *
 * @author fangen
 * @since JDK 11 2022/5/10
 */
@Setter
@Getter
public class TodoVO {
    private List<Todo> todoList = new ArrayList<>();

    private List<Todo> workingList = new ArrayList<>();

    private List<Todo> doneList = new ArrayList<>();

    private List<Todo> discardList = new ArrayList<>();
}
