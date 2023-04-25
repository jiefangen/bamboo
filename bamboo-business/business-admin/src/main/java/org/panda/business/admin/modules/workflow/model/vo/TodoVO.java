package org.panda.business.admin.modules.workflow.model.vo;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.workflow.model.WorkTodo;

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
    private List<WorkTodo> todoList = new ArrayList<>();

    private List<WorkTodo> workingList = new ArrayList<>();

    private List<WorkTodo> doneList = new ArrayList<>();

    private List<WorkTodo> discardList = new ArrayList<>();
}
