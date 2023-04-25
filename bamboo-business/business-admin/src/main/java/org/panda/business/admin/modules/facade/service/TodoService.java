package org.panda.business.admin.modules.facade.service;

import org.panda.business.admin.modules.facade.model.Todo;
import org.panda.business.admin.modules.facade.model.vo.TodoVO;

import java.math.BigInteger;
import java.util.List;

public interface TodoService {

    TodoVO getTodoList(BigInteger userId);

    void sortTodo(BigInteger userId, Integer workStatus, List<Todo> list);

    void addTodo(Todo todo);

    int delTodo(Todo todo);

    int editTodo(Todo todo);
}
