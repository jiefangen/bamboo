package org.panda.modules.facade.service;

import org.panda.modules.facade.domain.Todo;
import org.panda.modules.facade.domain.vo.TodoVO;

import java.math.BigInteger;
import java.util.List;

public interface TodoService {

    TodoVO getTodoList(BigInteger userId);

    void sortTodo(BigInteger userId, Integer workStatus, List<Todo> list);

    void addTodo(Todo todo);

    int delTodo(Todo todo);

    int editTodo(Todo todo);
}
