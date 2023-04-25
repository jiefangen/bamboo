package org.panda.business.admin.modules.workflow.service;

import org.panda.business.admin.modules.workflow.model.WorkTodo;
import org.panda.business.admin.modules.workflow.model.vo.TodoVO;

import java.math.BigInteger;
import java.util.List;

public interface TodoService {

    TodoVO getTodoList(BigInteger userId);

    void sortTodo(BigInteger userId, Integer workStatus, List<WorkTodo> list);

    void addTodo(WorkTodo todo);

    int delTodo(WorkTodo todo);

    int editTodo(WorkTodo todo);
}
